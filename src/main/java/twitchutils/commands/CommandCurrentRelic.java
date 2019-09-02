package twitchutils.commands;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.CurrentRelicConfigs;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;
import java.util.List;


public class CommandCurrentRelic {

    public CurrentRelicConfigs currentRelicConfigs;

    public ArrayList<Long> timeStamp = new ArrayList<>();
    public Long timeStampGlobal = 0L;
    public Long timeStampAll = 0L;

    public CommandCurrentRelic() {
        currentRelicConfigs = new CurrentRelicConfigs().getConfig();
    }


    public void addCommand() {
        addCommandRelic();
    }

    public void addCommandRelic() {
        if (currentRelicConfigs.CURRENT_RELIC_ENABLED &&
                AbstractDungeon.topPanel.twitch.isPresent()) {
            ((List<TwitchMessageListener>)
                    (ReflectionHacks.getPrivate(
                            AbstractDungeon.topPanel.twitch.get().connection,
                            TwitchConnection.class,
                            "listeners"))).add((msg, user) -> { // add listener
                if (CardCrawlGame.isInARun()) { // game run check
                    String leftOver = CommandHelper.getCommandLeftOver(
                            currentRelicConfigs.CURRENT_RELIC_COMMAND, msg);
                    if (leftOver != null) {
                        if (leftOver.isEmpty()) {
                            if (currentRelicConfigs.CURRENT_RELIC_ALL_ENABLED) {
                                messageForAllRelic(leftOver, user);
                            }
                        } else {
                            if (currentRelicConfigs.CURRENT_RELIC_ONE_ENABLED) {
                                int relicNum = CommandHelper.convertToInt(leftOver);
                                if (relicNum != 0) messageForOneRelic(relicNum, leftOver, user);
                            }
                        }
                    }
                }
            });
        }
    }


    private void messageForAllRelic(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, currentRelicConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        if (CommandHelper.isDuringCoolDown(timeStampAll, currentRelicConfigs.TIMEOUT_ALL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();
        timeStampAll = System.currentTimeMillis();

        ArrayList<AbstractRelic> relics = new ArrayList<>(AbstractDungeon.player.relics);
        ArrayList<String> relicStrings = new ArrayList<>();
        int relicLen = relics.size();
        for (int i = 0; i < relicLen; i++) {
            AbstractRelic relic = relics.get(i);
            relicStrings.add(currentRelicConfigs.CURRENT_RELICS_ONE_MESSAGE_DEFAULT
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELIC_NUMBER,
                            "" + (i + 1))
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELIC_NAME,
                            relic.name)
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELIC_DESCRIPTION,
                            CommandHelper.trimDescription(relic.description, currentRelicConfigs.MAX_LEN)));
        }
        String relicLists = String.join(currentRelicConfigs.CURRENT_RELICS_SEPARATOR_MESSAGE_DEFAULT,
                relicStrings);

        String pendingMessage = currentRelicConfigs.CURRENT_RELICS_MESSAGE_DEFAULT // message build
                .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELICS_LIST, relicLists)
                .replaceAll(CurrentRelicConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
            twitchPanel.connection.sendMessage(pendingMessage);
        });
    }


    public void messageForOneRelic(int relicNum, String msg, String user) {
        int relicCount = AbstractDungeon.player.relics.size();
        if (relicNum < 0) relicNum = relicCount + relicNum + 1;
        if (relicCount >= relicNum && relicNum >= 1) {
            int pos = relicNum - 1;
            AbstractRelic relic = AbstractDungeon.player.relics.get(pos);

            if (CommandHelper.isDuringCoolDown(timeStampGlobal, currentRelicConfigs.TIMEOUT_GLOBAL))
                return; // timeout check
            while (timeStamp.size() < relicNum) timeStamp.add(0L);
            if (CommandHelper.isDuringCoolDown(timeStamp.get(pos), currentRelicConfigs.TIMEOUT_ONE)) return;

            timeStampGlobal = System.currentTimeMillis();
            timeStamp.set(pos, System.currentTimeMillis()); // time mark

            String pendingMessage = currentRelicConfigs.CURRENT_RELIC_MESSAGE_DEFAULT // message build
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELIC_DESCRIPTION,
                            CommandHelper.trimDescription(relic.description, currentRelicConfigs.MAX_LEN))
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELIC_NAME, relic.name)
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user)
                    .replaceAll(CurrentRelicConfigs.DESCRIPTION_CURRENT_RELIC_NUMBER, "" + relicNum);

            if (pendingMessage.isEmpty()) return;

            AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
                twitchPanel.connection.sendMessage(pendingMessage);
            });
        }
    }
}
