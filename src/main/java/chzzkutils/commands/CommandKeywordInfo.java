package chzzkutils.commands;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import chzzkutils.ChzzkUtils;
import chzzkutils.configs.commands.DeckConfigs;
import chzzkutils.configs.commands.KeywordInfoConfigs;
import chzzkutils.helper.CommandHelper;

import java.util.HashSet;
import java.util.Set;


public class CommandKeywordInfo {

    public KeywordInfoConfigs keywordinfoConfigs;

    public Long timeStampGlobal = 0L;

    Set<String> modPrefixes = new HashSet<>();


    public CommandKeywordInfo() {
        keywordinfoConfigs = new KeywordInfoConfigs().getConfig();

        Set<String> keywordSets = GameDictionary.parentWord.keySet();
        for(String keyword: keywordSets){
            if(BaseMod.keywordIsUnique(keyword.toLowerCase())){
                String prefix = BaseMod.getKeywordPrefix(keyword.toLowerCase());
                modPrefixes.add(prefix);
            }
        } // register all keyword prefix
    }

    public void addCommand() {
        addCommandKeyword();
    }

    public void addCommandKeyword() {
        if (keywordinfoConfigs.KEYWORDINFO_ENABLED && ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.addListener((msg, user) -> {
                if (CardCrawlGame.isInARun()) { // game run check
                    String leftOver = CommandHelper.getCommandLeftOver(keywordinfoConfigs.KEYWORDINFO_COMMAND, msg);
                    if (leftOver != null) {
                        messageForKeyword(leftOver, user);
                    }
                }
            });
        }
    }


    private void messageForKeyword(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, keywordinfoConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();

        String parsedMsg = msg.toLowerCase();
        String keywordID = GameDictionary.parentWord.get(parsedMsg);
        if(keywordID == null){
            for(String tarMod: modPrefixes){ // deal with modID:keyword
                keywordID = GameDictionary.parentWord.get(tarMod + parsedMsg);
                if(keywordID != null) break;
            }
        }
        // (String)GameDictionary.parentWord.get(parsedMsg); for vanilla only

        String pendingMessage;
        if (keywordID == null) { // not found
            pendingMessage = keywordinfoConfigs.KEYWORDINFO_NONE_MESSAGE // message build
                    .replaceAll(KeywordInfoConfigs.DESCRIPTION_KEYWORDINFO_REQUEST, msg)
                    .replaceAll(DeckConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);
        } else { // found
            String keywordName = BaseMod.getKeywordTitle(keywordID);
            String keywordDescription = BaseMod.getKeywordDescription(keywordID);
            // (String)GameDictionary.keywords.get(keywordID);
            // for vanilla only (actually it works)

            pendingMessage = keywordinfoConfigs.KEYWORDINFO_INFO_MESSAGE // message build
                    .replaceAll(KeywordInfoConfigs.DESCRIPTION_KEYWORDINFO_NAME, keywordName)
                    .replaceAll(KeywordInfoConfigs.DESCRIPTION_KEYWORDINFO_DESCRIPTION,
                            CommandHelper.trimDescription(keywordDescription, keywordinfoConfigs.MAX_LEN))
                    .replaceAll(KeywordInfoConfigs.DESCRIPTION_KEYWORDINFO_REQUEST, msg)
                    .replaceAll(DeckConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);
        }

        if (pendingMessage.isEmpty()) return;

        if (ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.sendMessage(pendingMessage);
        }
    }

}
