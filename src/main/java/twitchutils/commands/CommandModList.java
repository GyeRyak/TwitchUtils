package twitchutils.commands;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.CustomModDescriptionConfigs;
import twitchutils.configs.commands.ModsConfigs;
import twitchutils.fetchers.ModListFetcher;
import twitchutils.helper.CommandHelper;

import java.util.*;


public class CommandModList {

    CustomModDescriptionConfigs descriptionConfigs;
    ModsConfigs modsConfigs;
    HashMap<String, ModInfo> modInfos = new HashMap<>();

    public HashMap<String, Long> timeStamp = new HashMap<>();
    public Long timeStampGlobal = 0L;
    public Long timeStampAll = 0L;


    public String getSimpleModName(ModInfo modInfo) {
        return getSimpleModName(modInfo.Name);
    }

    public String getSimpleModName(String modName) {
        return modName.toLowerCase()
                .replaceAll("_", "").replaceAll(" ", "")
                .replaceAll("\\n", "");
    }


    public CommandModList() {
        descriptionConfigs = new CustomModDescriptionConfigs().getConfig();
        modsConfigs = new ModsConfigs().getConfig();

        ArrayList<ModInfo> mods = ModListFetcher.getModInfos();
        for (ModInfo mod : mods) {
            descriptionConfigs.generateModDefault(getSimpleModName(mod));
            modInfos.put(getSimpleModName(mod), mod);
        }
        descriptionConfigs.generateNowStatus();
    }


    public void addCommand() {
        addCommandMods();
    }

    public void addCommandMods() {
        if (modsConfigs.MOD_ENABLED &&
                AbstractDungeon.topPanel.twitch.isPresent()) {
            ((List<TwitchMessageListener>)
                    (ReflectionHacks.getPrivate(
                            AbstractDungeon.topPanel.twitch.get().connection,
                            TwitchConnection.class,
                            "listeners"))).add((msg, user) -> { // add listener

                String leftOver = CommandHelper.getCommandLeftOver(modsConfigs.MOD_COMMAND, msg);
                if (leftOver != null) {
                    if (leftOver.isEmpty()) {
                        if(modsConfigs.MOD_ALL_ENABLED){
                            messageForAllMods(leftOver, user);
                        }
                    } else {
                        if(modsConfigs.MOD_ONE_ENABLED){
                            messageForOneMod(getSimpleModName(leftOver), leftOver, user);
                        }
                    }
                }

            });
        }
    }


    private void messageForAllMods(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, modsConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        if (CommandHelper.isDuringCoolDown(timeStampAll, modsConfigs.TIMEOUT_ALL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();
        timeStampAll = System.currentTimeMillis();

        ArrayList<String> modStrings = new ArrayList<>();
        for (String key : modInfos.keySet()) {
            ModInfo modInfo = modInfos.get(key);
            String simpleModName = getSimpleModName(modInfo);
            if (descriptionConfigs.isModHidden(simpleModName)) continue;
            if (descriptionConfigs.getModDescription(simpleModName).isEmpty()) {
                modStrings.add(getParsedModString(modsConfigs.MODS_ONE_MESSAGE_NO_CUSTOM_DEFAULT, modInfo));
            } else {
                modStrings.add(getParsedModString(modsConfigs.MODS_ONE_MESSAGE_CUSTOM_DEFAULT, modInfo));
            }
        }
        String modLists = String.join(modsConfigs.MODS_SEPARATOR_MESSAGE_DEFAULT, modStrings);

        String pendingMessage = modsConfigs.MODS_MESSAGE_DEFAULT // message build
                .replaceAll(ModsConfigs.DESCRIPTION_MODS_LIST, modLists)
                .replaceAll(ModsConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
            twitchPanel.connection.sendMessage(pendingMessage);
        });
    }


    public void messageForOneMod(String simpleModName, String msg, String user) {
        if (modInfos.containsKey(simpleModName)) {
            if (!timeStamp.containsKey(simpleModName)) {
                timeStamp.put(simpleModName, 0L);
            }

            if (CommandHelper.isDuringCoolDown(timeStampGlobal, modsConfigs.TIMEOUT_GLOBAL))
                return; // timeout check
            if (CommandHelper.isDuringCoolDown(timeStamp.get(simpleModName), modsConfigs.TIMEOUT_ONE))
                return; // timeout check
            timeStampGlobal = System.currentTimeMillis();
            timeStamp.replace(simpleModName, System.currentTimeMillis());

            ModInfo modInfo = modInfos.get(simpleModName);

            if (descriptionConfigs.isModHidden(simpleModName)) return;

            StringBuilder pendingMessageBuilder = new StringBuilder();

            if (descriptionConfigs.getModDescription(simpleModName).isEmpty()) {
                pendingMessageBuilder.append(getParsedModString(modsConfigs.MOD_MESSAGE_NO_CUSTOM_DEFAULT, modInfo, user));
            } else {
                pendingMessageBuilder.append(getParsedModString(modsConfigs.MOD_MESSAGE_CUSTOM_DEFAULT, modInfo, user));
            }

            final String pendingMessage = pendingMessageBuilder.toString();

            if (pendingMessage.isEmpty()) return;

            AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
                twitchPanel.connection.sendMessage(pendingMessage);
            });

        }
        else{ // if starts with one mod
            int matchCount = 0;
            String finalMatch = "";
            Set<String> modKeys = modInfos.keySet();
            for(String tarMod : modKeys){
                if(tarMod.startsWith(simpleModName)){
                    matchCount++;
                    finalMatch = tarMod;
                }
            }
            if(matchCount == 1) messageForOneMod(finalMatch, msg, user);
        }
    }

    public String getParsedModString(String origin, ModInfo mod) {
        return origin
                .replaceAll(ModsConfigs.DESCRIPTION_MOD_DESCRIPTION_CUSTOM,
                        CommandHelper.trimDescription(
                                descriptionConfigs.getModDescription(getSimpleModName(mod)),
                                modsConfigs.MAX_LEN))
                .replaceAll(ModsConfigs.DESCRIPTION_MOD_DESCRIPTION_FULL,
                        CommandHelper.trimDescription(mod.Description, modsConfigs.MAX_LEN))
                .replaceAll(ModsConfigs.DESCRIPTION_MOD_AUTHOR_MAIN, mod.Authors[0])
                .replaceAll(ModsConfigs.DESCRIPTION_MOD_AUTHOR_ALL, Arrays.toString(mod.Authors))
                .replaceAll(ModsConfigs.DESCRIPTION_MOD_CREDIT, mod.Credits)
                .replaceAll(ModsConfigs.DESCRIPTION_MOD_NAME, mod.Name);
    }

    public String getParsedModString(String origin, ModInfo mod, String user) {
        return getParsedModString(origin, mod)
                .replaceAll(ModsConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);
    }
}
