package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;

import java.util.ArrayList;

public class RelicInfoConfigs extends ConfigBasis<RelicInfoConfigs> {

    public ArrayList<String> RELICINFO_COMMAND = new ArrayList<>(); // command
    public String RELICINFO_INFO_PUBLIC_MESSAGE = "@<T_VIEWER> <RI_NAME>, <RI_ID> (All Character, <RI_TIER>): <RI_DESC> / <RI_FLAVOR>";
    public String RELICINFO_INFO_PRIVATE_MESSAGE = "@<T_VIEWER> <RI_NAME>, <RI_ID> (<RI_COLOR>, <RI_TIER>): <RI_DESC> / <RI_FLAVOR>";
    public String RELICINFO_NONE_MESSAGE = "@<T_VIEWER> No such relic named '<RI_REQUEST>'";

    public static final String DESCRIPTION_RELICINFO_NAME = "<RI_NAME>";
    public static final String DESCRIPTION_RELICINFO_COLOR = "<RI_COLOR>";
    public static final String DESCRIPTION_RELICINFO_TIER = "<RI_TIER>";
    public static final String DESCRIPTION_RELICINFO_DESCRIPTION = "<RI_DESC>";
    public static final String DESCRIPTION_RELICINFO_FLAVOR = "<RI_FLAVOR>";
    public static final String DESCRIPTION_RELICINFO_ID = "<RI_ID>";
    public static final String DESCRIPTION_RELICINFO_REQUEST = "<RI_REQUEST>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean RELICINFO_ENABLED = true;

    public long TIMEOUT_GLOBAL = 100L;

    public int MAX_LEN = 5000;

    public RelicInfoConfigs() {

    }


}
