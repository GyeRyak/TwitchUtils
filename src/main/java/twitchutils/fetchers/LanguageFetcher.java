package twitchutils.fetchers;

import com.megacrit.cardcrawl.core.Settings;

public class LanguageFetcher {
    public static String getLanguage() {
        switch (Settings.language) {
            case KOR:
                return "kor";
            /*case ZHS:
                return "zhs";
            case ZHT:
                return "zht";
            case FRA:
                return "fra";
            case JPN:
                return "jpn";*/
            default:
                return "eng";
        }
    }
}
