package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;

public class CurrentRelicConfigs extends ConfigBasis<CurrentRelicConfigs> {

    public ArrayList<String> CURRENT_RELIC_COMMAND = new ArrayList<>();
    public String CURRENT_RELIC_MESSAGE_DEFAULT = "@<T_VIEWER> (<CR_NUM>) <CR_NAME>: <CR_DESC>";
    public String CURRENT_RELICS_ONE_MESSAGE_DEFAULT = "<CR_NAME>(<CR_NUM>)";
    public String CURRENT_RELICS_MESSAGE_DEFAULT = "@<T_VIEWER> <CR_LIST>";
    public String CURRENT_RELICS_SEPARATOR_MESSAGE_DEFAULT = ", ";

    public static final String DESCRIPTION_CURRENT_RELICS_LIST = "<CR_LIST>";
    public static final String DESCRIPTION_CURRENT_RELIC_NAME = "<CR_NAME>";
    public static final String DESCRIPTION_CURRENT_RELIC_DESCRIPTION = "<CR_DESC>";
    public static final String DESCRIPTION_CURRENT_RELIC_NUMBER = "<CR_NUM>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean CURRENT_RELIC_ENABLED = true;
    public boolean CURRENT_RELIC_ALL_ENABLED = true;
    public boolean CURRENT_RELIC_ONE_ENABLED = true;

    public long TIMEOUT_GLOBAL = 1L;
    public long TIMEOUT_ALL = 20000L;
    public long TIMEOUT_ONE = 10000L;

    public int MAX_LEN = CommandHelper.MAX_LEN_INFINITE;

    public CurrentRelicConfigs() {

    }


}
