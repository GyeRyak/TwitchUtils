package twitchutils.commands;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.CurrentPotionConfigs;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;
import java.util.List;


public class CommandCurrentPotion {

    public CurrentPotionConfigs currentPotionConfigs;

    public ArrayList<Long> timeStamp = new ArrayList<>();
    public Long timeStampGlobal = 0L;
    public Long timeStampAll = 0L;


    public CommandCurrentPotion() {
        currentPotionConfigs = new CurrentPotionConfigs().getConfig();
    }


    public void addCommand() {
        addCommandPotion();
    }

    public void addCommandPotion() {
        if (currentPotionConfigs.CURRENT_POTION_ENABLED &&
                AbstractDungeon.topPanel.twitch.isPresent()) {
            ((List<TwitchMessageListener>)
                    (ReflectionHacks.getPrivate(
                            AbstractDungeon.topPanel.twitch.get().connection,
                            TwitchConnection.class,
                            "listeners"))).add((msg, user) -> { // add listener
                if (CardCrawlGame.isInARun()) { // game run check
                    String leftOver = CommandHelper.getCommandLeftOver(
                            currentPotionConfigs.CURRENT_POTION_COMMAND, msg);
                    if (leftOver != null) {
                        if (leftOver.isEmpty()) {
                            if (currentPotionConfigs.CURRENT_POTION_ALL_ENABLED) {
                                messageForAllPotion(leftOver, user);
                            }
                        } else {
                            if (currentPotionConfigs.CURRENT_POTION_ONE_ENABLED) {
                                int potionNum = CommandHelper.convertToInt(leftOver);
                                if (potionNum != 0) messageForOnePotion(potionNum, leftOver, user);
                            }
                        }
                    }
                }
            });
        }
    }


    private void messageForAllPotion(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, currentPotionConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        if (CommandHelper.isDuringCoolDown(timeStampAll, currentPotionConfigs.TIMEOUT_ALL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();
        timeStampAll = System.currentTimeMillis();

        ArrayList<AbstractPotion> potions = new ArrayList<>(AbstractDungeon.player.potions);
        ArrayList<String> potionStrings = new ArrayList<>();
        int potionLen = potions.size();
        for (int i = 0; i < potionLen; i++) {
            AbstractPotion potion = potions.get(i);

            if (potion.rarity == AbstractPotion.PotionRarity.PLACEHOLDER) continue;

            potionStrings.add(currentPotionConfigs.CURRENT_POTIONS_ONE_MESSAGE_DEFAULT
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTION_NUMBER,
                            "" + (i + 1))
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTION_NAME,
                            potion.name)
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTION_DESCRIPTION,
                            CommandHelper.trimDescription(potion.description, currentPotionConfigs.MAX_LEN)));
        }
        String potionLists = String.join(
                currentPotionConfigs.CURRENT_POTIONS_SEPARATOR_MESSAGE_DEFAULT, potionStrings);

        String pendingMessage = currentPotionConfigs.CURRENT_POTIONS_MESSAGE_DEFAULT // message build
                .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTIONS_LIST, potionLists)
                .replaceAll(CurrentPotionConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
            twitchPanel.connection.sendMessage(pendingMessage);
        });
    }


    public void messageForOnePotion(int potionNum, String msg, String user) {
        int potionCount = AbstractDungeon.player.potions.size();
        if (potionNum < 0) potionNum = potionCount + potionNum + 1;
        if (potionCount >= potionNum && potionNum >= 1) {
            int pos = potionNum - 1;
            AbstractPotion potion = AbstractDungeon.player.potions.get(pos);

            if (potion.rarity == AbstractPotion.PotionRarity.PLACEHOLDER) return;

            if (CommandHelper.isDuringCoolDown(timeStampGlobal,
                    currentPotionConfigs.TIMEOUT_GLOBAL))
                return; // timeout check
            while (timeStamp.size() < potionNum) timeStamp.add(0L);
            if (CommandHelper.isDuringCoolDown(timeStamp.get(pos),
                    currentPotionConfigs.TIMEOUT_ONE)) return;

            timeStampGlobal = System.currentTimeMillis();
            timeStamp.set(pos, System.currentTimeMillis()); // time mark

            String pendingMessage = currentPotionConfigs.CURRENT_POTION_MESSAGE_DEFAULT // message build
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTION_DESCRIPTION,
                            CommandHelper.trimDescription(potion.description, currentPotionConfigs.MAX_LEN))
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTION_NAME,
                            potion.name)
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_TWITCH_VIEWER_NAME,
                            user)
                    .replaceAll(CurrentPotionConfigs.DESCRIPTION_CURRENT_POTION_NUMBER,
                            "" + potionNum);

            if (pendingMessage.isEmpty()) return;

            AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
                twitchPanel.connection.sendMessage(pendingMessage);
            });
        }
    }
}
