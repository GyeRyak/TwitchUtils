package twitchutils;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitchutils.commands.*;
import twitchutils.configs.twitch.VanillaVoteConfigs;
import twitchutils.helper.ResourcesManager;

import java.io.IOException;

@SpireInitializer
public class TwitchUtils implements PostInitializeSubscriber {

    public static final String MOD_NAME = "TwitchUtils";
    public static final String MOD_ID = "twitchutils";
    public static final String MOD_AUTHOR = "GyeRyak";
    public static final String MOD_CREDIT = "GyeRyak";
    public static final String MOD_DESC = "Bunches of Configs for Twitch Streamer and Viewer!";

    public CommandCurrentRelic mCommandCurrentRelic;
    public CommandModList mCommandModList;
    public CommandCurrentPotion mCommandCurrentPotion;
    public CommandShopRelic mCommandShopRelic;
    public CommandShopPotion mCommandShopPotion;
    public CommandDeck mCommandDeck;

    public static boolean vanillaVote_Disable = false;
    public static final Logger twitchUtilsLogger = LogManager.getLogger(MOD_ID);


    public TwitchUtils() {
        BaseMod.subscribe(this);

    }


    public static void initialize() {
        new TwitchUtils();
    }

    public static void loadValues() {
        vanillaVote_Disable = new VanillaVoteConfigs().getConfig().NORMAL_VOTE_DISABLE;
    }

    public static boolean isVanillaVoteDisabled() {
        vanillaVote_Disable = new VanillaVoteConfigs().getConfig().NORMAL_VOTE_DISABLE;
        return vanillaVote_Disable;
    }

    public void settingModPanel() {
        ModPanel modPanel = new ModPanel();
        ModLabeledToggleButton vanillaVoteDisableToggleButton =
                new ModLabeledToggleButton(
                        CardCrawlGame.languagePack.getUIString(TwitchUtils.makeID(TwitchUtils.UI_OPTIONS)).TEXT[0],
                        350, 700,
                        Settings.CREAM_COLOR,
                        FontHelper.charDescFont,
                        isVanillaVoteDisabled(),
                        modPanel,
                        I -> {
                        },
                        button -> {
                            VanillaVoteConfigs vanillaVoteConfigs = new VanillaVoteConfigs().getConfig();
                            vanillaVoteConfigs.NORMAL_VOTE_DISABLE = button.enabled;
                            TwitchUtils.vanillaVote_Disable = button.enabled;
                            try {
                                vanillaVoteConfigs.makeConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
        modPanel.addUIElement(vanillaVoteDisableToggleButton);

        BaseMod.registerModBadge(ImageMaster.loadImage(ResourcesManager.getInnerPath(
                ResourcesManager.CATEGORY_IMAGE, "modBadge", "png"
        )), MOD_ID, MOD_AUTHOR, MOD_DESC, modPanel);
    }

    public void receivePostInitialize() {
        BaseMod.loadCustomStringsFile(UIStrings.class, ResourcesManager.getLocalizationPath(
                ResourcesManager.CATEGORY_UI, "ui"
        ));
        loadValues();

        settingModPanel();

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