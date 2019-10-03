package twitchutils.helper;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterHelper {

    private static CharacterHelper characterHelper = new CharacterHelper();

    private HashMap<AbstractCard.CardColor, AbstractPlayer> colorToPlayer = new HashMap<>();

    private CharacterHelper(){
        ArrayList<AbstractCard.CardColor> allColor = new ArrayList<>(BaseMod.getCardColors());
        ArrayList<AbstractPlayer> allCharacter = new ArrayList<>();
        allCharacter.addAll(BaseMod.getModdedCharacters());

        for(AbstractPlayer.PlayerClass tarPlayerClass: AbstractPlayer.PlayerClass.values()){
            allCharacter.add(BaseMod.findCharacter(tarPlayerClass));
        }

        for(AbstractPlayer tarPlayer: allCharacter){
            colorToPlayer.put(tarPlayer.getCardColor(), tarPlayer);
        }
    }

    public static CharacterHelper getCharacterHelper(){
        return characterHelper;
    }

    public String getLocalizedCharacterName(AbstractCard.CardColor tarColor){
        switch(tarColor){
            case CURSE:
                return CardLibraryScreen.TEXT[5];
            case COLORLESS:
                return CardLibraryScreen.TEXT[4];
            default:
                if(colorToPlayer.containsKey(tarColor)){
                    return colorToPlayer.get(tarColor).getLocalizedCharacterName();
                }
                break;
        }
        return "";
    }

    public String getLocalizedCardRarity(AbstractCard.CardRarity tarRarity) {
        switch (tarRarity) {
            case BASIC:
                return RunHistoryScreen.TEXT[11];
            case SPECIAL:
                return RunHistoryScreen.TEXT[15];
            case COMMON:
                return RunHistoryScreen.TEXT[12];
            case UNCOMMON:
                return RunHistoryScreen.TEXT[13];
            case RARE:
                return RunHistoryScreen.TEXT[14];
            case CURSE:
                return RunHistoryScreen.TEXT[16];
        }
        return "?";
    }

    public String getLocalizedRelicTier(AbstractRelic.RelicTier tarTier){
        switch (tarTier){
            case SPECIAL:
                return RunHistoryScreen.TEXT[15];
            case DEPRECATED:
                return "DEPRECATED";
            case STARTER:
                return RunHistoryScreen.TEXT[11];
            case COMMON:
                return RunHistoryScreen.TEXT[12];
            case UNCOMMON:
                return RunHistoryScreen.TEXT[13];
            case RARE:
                return RunHistoryScreen.TEXT[14];
            case BOSS:
                return RunHistoryScreen.TEXT[17];
            case SHOP:
                return RunHistoryScreen.TEXT[18];
        }
        return "?";
    }
}
