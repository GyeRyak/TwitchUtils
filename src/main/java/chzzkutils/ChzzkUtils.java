package chzzkutils;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ModTextBox;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import chzzkutils.commands.*;
import chzzkutils.configs.ChzzkConfigs;
import chzzkutils.connection.ChzzkUtilsConnection;
import chzzkutils.helper.ResourcesManager;

import java.io.IOException;

@SpireInitializer
public class ChzzkUtils implements PostInitializeSubscriber {

    public static final String MOD_NAME = "ChzzkUtils";
    public static final String MOD_ID = "chzzkutils";
    public static final String MOD_AUTHOR = "GyeRyak";
    public static final String MOD_CREDIT = "GyeRyak";
    public static final String MOD_DESC = "Bunches of Configs for Chzzk Streamer and Viewer!";

    public CommandCurrentRelic mCommandCurrentRelic;
    public CommandModList mCommandModList;
    public CommandCurrentPotion mCommandCurrentPotion;
    public CommandShopRelic mCommandShopRelic;
    public CommandShopPotion mCommandShopPotion;
    public CommandDeck mCommandDeck;
    public CommandCardInfo mCommandCardInfo;
    public CommandRelicInfo mCommandRelicInfo;
    public CommandCurrentBoss mCommandCurrentBoss;
    public CommandKeywordInfo mCommandKeywordInfo;

    public static ChzzkUtilsConnection chzzkConnection;
    public static ChzzkConfigs chzzkConfigs;

    public static final Logger chzzkUtilsLogger = LogManager.getLogger(MOD_ID);


    public ChzzkUtils() {
        BaseMod.subscribe(this);

    }


    public static void initialize() {
        new ChzzkUtils();
    }

    public void settingModPanel() {
        ModPanel modPanel = new ModPanel();

        ModLabeledToggleButton enableChzzkButton =
                new ModLabeledToggleButton(
                        CardCrawlGame.languagePack.getUIString(makeID(UI_OPTIONS)).TEXT[1],
                        350, 700,
                        Settings.CREAM_COLOR,
                        FontHelper.charDescFont,
                        chzzkConfigs.CHZZK_ENABLED,
                        modPanel,
                        (label) -> {},
                        (button) -> {
                            chzzkConfigs.CHZZK_ENABLED = button.enabled;
                            try {
                                chzzkConfigs.makeConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
        modPanel.addUIElement(enableChzzkButton);

        ModTextBox channelIdTextBox = new ModTextBox(
                CardCrawlGame.languagePack.getUIString(makeID(UI_OPTIONS)).TEXT[2],
                350, 600,
                modPanel,
                (box) -> {
                    chzzkConfigs.CHZZK_CHANNEL_ID = box.text;
                    try {
                        chzzkConfigs.makeConfig();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        channelIdTextBox.text = chzzkConfigs.CHZZK_CHANNEL_ID;
        modPanel.addUIElement(channelIdTextBox);


        BaseMod.registerModBadge(ImageMaster.loadImage(ResourcesManager.getInnerPath(
                ResourcesManager.CATEGORY_IMAGE, "modBadge", "png"
        )), MOD_ID, MOD_AUTHOR, MOD_DESC, modPanel);
    }

    public void receivePostInitialize() {
        BaseMod.loadCustomStringsFile(UIStrings.class, ResourcesManager.getLocalizationPath(
                ResourcesManager.CATEGORY_UI, "ui"
        ));
        chzzkConfigs = new ChzzkConfigs().getConfig();

        settingModPanel();

        if (chzzkConfigs.CHZZK_ENABLED && chzzkConfigs.CHZZK_CHANNEL_ID != null && !chzzkConfigs.CHZZK_CHANNEL_ID.isEmpty()) {
            chzzkConnection = new ChzzkUtilsConnection(chzzkConfigs.CHZZK_CHANNEL_ID);
            chzzkConnection.connect();
        }

        mCommandCurrentRelic = new CommandCurrentRelic();
        mCommandCurrentRelic.addCommand();

        mCommandModList = new CommandModList();
        mCommandModList.addCommand();

        mCommandCurrentPotion = new CommandCurrentPotion();
        mCommandCurrentPotion.addCommand();

        mCommandShopRelic = new CommandShopRelic();
        mCommandShopRelic.addCommand();

        mCommandShopPotion = new CommandShopPotion();
        mCommandShopPotion.addCommand();

        mCommandDeck = new CommandDeck();
        mCommandDeck.addCommand();

        mCommandCardInfo = new CommandCardInfo();
        mCommandCardInfo.addCommand();

        mCommandRelicInfo = new CommandRelicInfo();
        mCommandRelicInfo.addCommand();

        mCommandCurrentBoss = new CommandCurrentBoss();
        mCommandCurrentBoss.addCommand();

        mCommandKeywordInfo = new CommandKeywordInfo();
        mCommandKeywordInfo.addCommand();


        //CardInfoConfigs cardInfoConfigs = new CardInfoConfigs().getConfig();

        //DeckConfigs deckConfigs = new DeckConfigs().getConfig();
    }
    /*
    public void setProperties(){
        Properties propDefault = new Properties();
        propDefault.setProperty("TEST2", "OKAY2");
        Prefs pref = new Prefs();
        Pattern.quote("not regex");
    }
     */

    public static String makeID(String tarName) {
        return MOD_ID + ":" + tarName;
    }

    public static final String UI_OPTIONS = "Options";
    public static final String UI_MOD_DESC = "Mods_Descriptions";


}