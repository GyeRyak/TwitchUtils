package chzzkutils.commands;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import chzzkutils.ChzzkUtils;
import chzzkutils.configs.commands.ShopPotionConfigs;
import chzzkutils.helper.CommandHelper;

import java.util.ArrayList;


public class CommandShopPotion {

    public ShopPotionConfigs shopPotionConfigs;

    public ArrayList<Long> timeStamp = new ArrayList<>();
    public Long timeStampGlobal = 0L;
    public Long timeStampAll = 0L;

    public int mMaxLen = 500;


    public CommandShopPotion() {
        shopPotionConfigs = new ShopPotionConfigs().getConfig();
    }


    public void addCommand() {
        addCommandPotion();
    }

    public void addCommandPotion() {
        if (shopPotionConfigs.SHOP_POTION_ENABLED && ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.addListener((msg, user) -> {
                if (CardCrawlGame.isInARun() && // game run check
                        AbstractDungeon.shopScreen.isActive &&
                        AbstractDungeon.getCurrRoom() instanceof ShopRoom) { // shop screen check
                    String leftOver = CommandHelper.getCommandLeftOver(shopPotionConfigs.SHOP_POTION_COMMAND, msg);
                    if (leftOver != null) {
                        if (leftOver.isEmpty()) {
                            if (shopPotionConfigs.SHOP_POTION_ALL_ENABLED) {
                                messageForAllPotion(leftOver, user);
                            }
                        } else {
                            if (shopPotionConfigs.SHOP_POTION_ONE_ENABLED) {
                                int potionNum = CommandHelper.convertToInt(leftOver);
                                if (potionNum > 0 && potionNum <= 3) {
                                    messageForOnePotion(potionNum, leftOver, user);
                                }
                            }
                        }
                    }
                }
            });
        }
    }


    private void messageForAllPotion(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, shopPotionConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        if (CommandHelper.isDuringCoolDown(timeStampAll, shopPotionConfigs.TIMEOUT_ALL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();
        timeStampAll = System.currentTimeMillis();

        ArrayList<StorePotion> potions = new ArrayList<StorePotion>(
                (ArrayList<StorePotion>) ReflectionHacks.getPrivate(
                        AbstractDungeon.shopScreen,
                        ShopScreen.class, "potions")
        );
        ArrayList<String> potionStrings = new ArrayList<>();

        int potionLen = potions.size();
        for (int i = 0; i < potionLen; i++) {
            StorePotion potion = potions.get(i);
            if (potion.isPurchased) continue;

            potionStrings.add(getParsedStorePotionString(shopPotionConfigs.SHOP_POTIONS_ONE_MESSAGE_DEFAULT, potion));
        }

        String potionLists = String.join(shopPotionConfigs.SHOP_POTIONS_SEPARATOR_MESSAGE_DEFAULT, potionStrings);

        String pendingMessage = shopPotionConfigs.SHOP_POTIONS_MESSAGE_DEFAULT // message build
                .replaceAll(ShopPotionConfigs.DESCRIPTION_SHOP_POTIONS_LIST, potionLists)
                .replaceAll(ShopPotionConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        if (ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.sendMessage(pendingMessage);
        }
    }


    public void messageForOnePotion(int potionNum, String msg, String user) {
        int slot = potionNum - 1;

        if (CommandHelper.isDuringCoolDown(timeStampGlobal, shopPotionConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        while (timeStamp.size() < potionNum) timeStamp.add(0L);
        if (CommandHelper.isDuringCoolDown(timeStamp.get(slot), shopPotionConfigs.TIMEOUT_ONE)) return;

        timeStampGlobal = System.currentTimeMillis();
        timeStamp.set(slot, System.currentTimeMillis()); // time mark

        ArrayList<StorePotion> potions = new ArrayList<StorePotion>(
                (ArrayList<StorePotion>) ReflectionHacks.getPrivate(
                        AbstractDungeon.shopScreen,
                        ShopScreen.class, "potions")
        );

        int potionLen = potions.size();
        StorePotion chosenPotion = null;
        for (int i = 0; i < potionLen; i++) {
            StorePotion potion = potions.get(i);
            if (potion.isPurchased) continue;
            if (getPotionSlot(potion) == slot) { // find potion
                chosenPotion = potion;
                break;
            }
        }
        if (chosenPotion == null) return; // cannot find

        String pendingMessage = getParsedStorePotionString(shopPotionConfigs.SHOP_POTION_MESSAGE_DEFAULT, chosenPotion) // message build
                .replaceAll(ShopPotionConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        if (ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.sendMessage(pendingMessage);
        }
    }


    public String getParsedStorePotionString(String originString, StorePotion tarPotion) {
        return originString
                .replaceAll(ShopPotionConfigs.DESCRIPTION_SHOP_POTION_NUMBER, "" + (getPotionSlot(tarPotion) + 1))
                .replaceAll(ShopPotionConfigs.DESCRIPTION_SHOP_POTION_NAME, tarPotion.potion.name)
                .replaceAll(ShopPotionConfigs.DESCRIPTION_SHOP_POTION_DESCRIPTION,
                        CommandHelper.trimDescription(tarPotion.potion.description, mMaxLen));
    }

    public int getPotionSlot(StorePotion tarPotion) {
        return (int) ReflectionHacks.getPrivate(tarPotion, StorePotion.class, "slot");
    }
}
