package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;

import java.util.ArrayList;
import java.util.Arrays;

public class KeywordInfoConfigs extends ConfigBasis<KeywordInfoConfigs> {

    public ArrayList<String> KEYWORDINFO_COMMAND = new ArrayList<>(Arrays.asList("!keywordinfo", "!ki")); // command
    public String KEYWORDINFO_INFO_MESSAGE =
            "@<T_VIEWER> <KI_REQUEST> -> <KI_NAME>: <KI_DESC>";
    public String KEYWORDINFO_NONE_MESSAGE = "@<T_VIEWER> No such keyword with '<KI_REQUEST>'";

    public static final String DESCRIPTION_KEYWORDINFO_NAME = "<KI_NAME>";
    public static final String DESCRIPTION_KEYWORDINFO_DESCRIPTION = "<KI_DESC>";
    public static final String DESCRIPTION_KEYWORDINFO_REQUEST = "<KI_REQUEST>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean KEYWORDINFO_ENABLED = true;

    public long TIMEOUT_GLOBAL = 100L;

    public int MAX_LEN = 5000;

    public KeywordInfoConfigs() {

    }


}
