package twitchutils.commands;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.ShopRelicConfigs;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;
import java.util.List;


public class CommandShopRelic {

    public ShopRelicConfigs shopRelicConfigs;

    public ArrayList<Long> timeStamp = new ArrayList<>();
    public Long timeStampGlobal = 0L;
    public Long timeStampAll = 0L;


    public CommandShopRelic() {
        shopRelicConfigs = new ShopRelicConfigs().getConfig();
    }


    public void addCommand() {
        addCommandRelic();
    }

    public void addCommandRelic() {
        if (shopRelicConfigs.SHOP_RELIC_ENABLED &&
                AbstractDungeon.topPanel.twitch.isPresent()) {
            ((List<TwitchMessageListener>)
                    (ReflectionHacks.getPrivate(
                            AbstractDungeon.topPanel.twitch.get().connection,
                            TwitchConnection.class,
                            "listeners"))).add((msg, user) -> { // add listener
                if (CardCrawlGame.isInARun() && // game run check
                        AbstractDungeon.shopScreen.isActive &&
                        AbstractDungeon.getCurrRoom() instanceof ShopRoom) { // shop screen check
                    String leftOver = CommandHelper.getCommandLeftOver(shopRelicConfigs.SHOP_RELIC_COMMAND, msg);
                    if (leftOver != null) {
                        if (leftOver.isEmpty()) {
                            if (shopRelicConfigs.SHOP_RELIC_ALL_ENABLED) {
                                messageForAllRelic(leftOver, user);
                            }
                        } else {
                            if (shopRelicConfigs.SHOP_RELIC_ONE_ENABLED) {
                                int relicNum = CommandHelper.convertToInt(leftOver);
                                if (relicNum > 0 && relicNum <= 3) {
                                    messageForOneRelic(relicNum, leftOver, user);
                                }
                            }
                        }
                    }
                }
            });
        }
    }


    private void messageForAllRelic(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, shopRelicConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        if (CommandHelper.isDuringCoolDown(timeStampAll, shopRelicConfigs.TIMEOUT_ALL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();
        timeStampAll = System.currentTimeMillis();

        ArrayList<StoreRelic> relics = new ArrayList<StoreRelic>(
                (ArrayList<StoreRelic>) ReflectionHacks.getPrivate(
                        AbstractDungeon.shopScreen,
                        ShopScreen.class, "relics")
        );
        ArrayList<String> relicStrings = new ArrayList<>();

        int relicLen = relics.size();
        for (int i = 0; i < relicLen; i++) {
            StoreRelic relic = relics.get(i);
            if (relic.isPurchased) continue;

            relicStrings.add(getParsedStoreRelicString(shopRelicConfigs.SHOP_RELICS_ONE_MESSAGE_DEFAULT, relic));
        }

        String relicLists = String.join(shopRelicConfigs.SHOP_RELICS_SEPARATOR_MESSAGE_DEFAULT, relicStrings);

        String pendingMessage = shopRelicConfigs.SHOP_RELICS_MESSAGE_DEFAULT // message build
                .replaceAll(ShopRelicConfigs.DESCRIPTION_SHOP_RELICS_LIST, relicLists)
                .replaceAll(ShopRelicConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
            twitchPanel.connection.sendMessage(pendingMessage);
        });
    }


    public void messageForOneRelic(int relicNum, String msg, String user) {
        int slot = relicNum - 1;

        if (CommandHelper.isDuringCoolDown(timeStampGlobal, shopRelicConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        while (timeStamp.size() < relicNum) timeStamp.add(0L);
        if (CommandHelper.isDuringCoolDown(timeStamp.get(slot), shopRelicConfigs.TIMEOUT_ONE)) return;

        timeStampGlobal = System.currentTimeMillis();
        timeStamp.set(slot, System.currentTimeMillis()); // time mark

        ArrayList<StoreRelic> relics = new ArrayList<StoreRelic>(
                (ArrayList<StoreRelic>) ReflectionHacks.getPrivate(
                        AbstractDungeon.shopScreen,
                        ShopScreen.class, "relics")
        );

        int relicLen = relics.size();
        StoreRelic chosenRelic = null;
        for (int i = 0; i < relicLen; i++) {
            StoreRelic relic = relics.get(i);
            if (relic.isPurchased) continue;
            if (getRelicSlot(relic) == slot) { // find relic
                chosenRelic = relic;
                break;
            }
        }
        if (chosenRelic == null) return; // cannot find

        String pendingMessage = getParsedStoreRelicString(shopRelicConfigs.SHOP_RELIC_MESSAGE_DEFAULT, chosenRelic) // message build
                .replaceAll(ShopRelicConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
            twitchPanel.connection.sendMessage(pendingMessage);
        });
    }


    public String getParsedStoreRelicString(String originString, StoreRelic tarRelic) {
        return originString
                .replaceAll(ShopRelicConfigs.DESCRIPTION_SHOP_RELIC_NUMBER, "" + (getRelicSlot(tarRelic) + 1))
                .replaceAll(ShopRelicConfigs.DESCRIPTION_SHOP_RELIC_NAME, tarRelic.relic.name)
                .replaceAll(ShopRelicConfigs.DESCRIPTION_SHOP_RELIC_DESCRIPTION,
                        CommandHelper.trimDescription(tarRelic.relic.description, shopRelicConfigs.MAX_LEN));
    }

    public int getRelicSlot(StoreRelic tarRelic) {
        return (int) ReflectionHacks.getPrivate(tarRelic, StoreRelic.class, "slot");
    }
}
