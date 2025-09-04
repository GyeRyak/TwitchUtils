package chzzkutils.commands;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import chzzkutils.ChzzkUtils;
import chzzkutils.configs.commands.RelicInfoConfigs;
import chzzkutils.helper.CharacterHelper;
import chzzkutils.helper.CommandHelper;

import java.util.HashMap;


public class CommandRelicInfo {

    public RelicInfoConfigs relicInfoConfigs;
    public HashMap<String, RelicWithColor> relicInfos = new HashMap<>();

    public class RelicWithColor {
        AbstractRelic abstractRelic;
        boolean isShared;
        AbstractCard.CardColor cardColor;

        public RelicWithColor(AbstractRelic abstractRelic, boolean isShared) {
            this.abstractRelic = abstractRelic;
            this.isShared = isShared;
        }

        public RelicWithColor(AbstractRelic abstractRelic, AbstractCard.CardColor cardColor) {
            this.abstractRelic = abstractRelic;
            this.isShared = false;
            this.cardColor = cardColor;
        }
    }

    public Long timeStampGlobal = 0L;

    public CommandRelicInfo() {
        relicInfoConfigs = new RelicInfoConfigs().getConfig();

        HashMap<AbstractCard.CardColor, HashMap<String, AbstractRelic>> moddedRelics =
                BaseMod.getAllCustomRelics(); // only get modded character only relic
        for (AbstractCard.CardColor tarColorPool : moddedRelics.keySet()) {
            for (AbstractRelic tarRelic : moddedRelics.get(tarColorPool).values()) {
                relicInfos.put(tarRelic.name.replaceAll(" ", "").toLowerCase(),
                        new RelicWithColor(tarRelic, tarColorPool));
            }
        }

        // below also get modded relics too, Thank you Alchyr :)
        HashMap<String, AbstractRelic> sharedRelics =
                (HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class, "sharedRelics");
        for (AbstractRelic tarRelic : sharedRelics.values()) {
            relicInfos.put(tarRelic.name.replaceAll(" ", "").toLowerCase(),
                    new RelicWithColor(tarRelic, true));
        }

        HashMap<String, AbstractRelic> redRelics =
                (HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class, "redRelics");
        for (AbstractRelic tarRelic : redRelics.values()) {
            relicInfos.put(tarRelic.name.replaceAll(" ", "").toLowerCase(),
                    new RelicWithColor(tarRelic, AbstractCard.CardColor.RED));
        }

        HashMap<String, AbstractRelic> greenRelics =
                (HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class, "greenRelics");
        for (AbstractRelic tarRelic : greenRelics.values()) {
            relicInfos.put(tarRelic.name.replaceAll(" ", "").toLowerCase(),
                    new RelicWithColor(tarRelic, AbstractCard.CardColor.GREEN));
        }

        HashMap<String, AbstractRelic> blueRelics =
                (HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class, "blueRelics");
        for (AbstractRelic tarRelic : blueRelics.values()) {
            relicInfos.put(tarRelic.name.replaceAll(" ", "").toLowerCase(),
                    new RelicWithColor(tarRelic, AbstractCard.CardColor.BLUE));
        }

        HashMap<String, AbstractRelic> purpleRelics =
                (HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class, "purpleRelics");
        for (AbstractRelic tarRelic : purpleRelics.values()) {
            relicInfos.put(tarRelic.name.replaceAll(" ", "").toLowerCase(),
                    new RelicWithColor(tarRelic, AbstractCard.CardColor.PURPLE));
        }
    }


    public void addCommand() {
        addCommandRelicInfo();
    }

    public void addCommandRelicInfo() {
        if (relicInfoConfigs.RELICINFO_ENABLED && ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.addListener((msg, user) -> {
                String leftOver = CommandHelper.getCommandLeftOver(relicInfoConfigs.RELICINFO_COMMAND, msg);
                if (leftOver != null) {
                    if (leftOver.isEmpty()) {
                        return; // do nothing
                    } else {
                        messageForRelic(leftOver, user);
                    }
                }
            });
        }
    }

    public void messageForRelic(String msg, String user) {
        String originalMsg = msg;

        if (CommandHelper.isDuringCoolDown(timeStampGlobal, relicInfoConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();

        msg = msg.replaceAll(" ", "").toLowerCase();

        if (relicInfos.containsKey(msg)) {
            RelicWithColor tarRelicWithColor = relicInfos.get(msg);
            AbstractRelic tarRelic = tarRelicWithColor.abstractRelic.makeCopy();

            String targetMessage = "";
            if (tarRelicWithColor.isShared) {
                targetMessage = getParsedRelicString(relicInfoConfigs.RELICINFO_INFO_PUBLIC_MESSAGE, tarRelic, user);
            } else {
                targetMessage = getParsedRelicString(relicInfoConfigs.RELICINFO_INFO_PRIVATE_MESSAGE, tarRelic, user)
                        .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_COLOR,
                                CharacterHelper.getCharacterHelper().getLocalizedCharacterName(tarRelicWithColor.cardColor));
            }

            final String pendingMessage = targetMessage;
            if (ChzzkUtils.chzzkConnection != null) {
                ChzzkUtils.chzzkConnection.sendMessage(pendingMessage);
            }
        } else {
            final String pendingMessage = relicInfoConfigs.RELICINFO_NONE_MESSAGE
                    .replaceAll(RelicInfoConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user)
                    .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_REQUEST, originalMsg);

            if (ChzzkUtils.chzzkConnection != null) {
                ChzzkUtils.chzzkConnection.sendMessage(pendingMessage);
            }
        }
    }

    public String getParsedRelicString(String origin, AbstractRelic relic) {
        return origin
                .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_NAME, relic.name)
                .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_ID, relic.relicId)
                .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_DESCRIPTION,
                        CommandHelper.trimDescription(relic.description, relicInfoConfigs.MAX_LEN))
                .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_FLAVOR,
                        CommandHelper.trimDescription(relic.flavorText, relicInfoConfigs.MAX_LEN))
                .replaceAll(RelicInfoConfigs.DESCRIPTION_RELICINFO_TIER,
                        CharacterHelper.getCharacterHelper().getLocalizedRelicTier(relic.tier));
    }

    public String getParsedRelicString(String origin, AbstractRelic relic, String user) {
        return getParsedRelicString(origin, relic)
                .replaceAll(RelicInfoConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);
    }

}
