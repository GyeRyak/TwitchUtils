package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;

public class CurrentPotionConfigs extends ConfigBasis<CurrentPotionConfigs> {

    public ArrayList<String> CURRENT_POTION_COMMAND = new ArrayList<>();
    public String CURRENT_POTION_MESSAGE_DEFAULT = "@<T_VIEWER> (<CP_NUM>) <CP_NAME>: <CP_DESC>";
    public String CURRENT_POTIONS_ONE_MESSAGE_DEFAULT = "<CP_NAME>(<CP_NUM>)";
    public String CURRENT_POTIONS_MESSAGE_DEFAULT = "@<T_VIEWER> <CP_LIST>";
    public String CURRENT_POTIONS_SEPARATOR_MESSAGE_DEFAULT = ", ";


    public static final String DESCRIPTION_CURRENT_POTIONS_LIST = "<CP_LIST>";
    public static final String DESCRIPTION_CURRENT_POTION_NAME = "<CP_NAME>";
    public static final String DESCRIPTION_CURRENT_POTION_DESCRIPTION = "<CP_DESC>";
    public static final String DESCRIPTION_CURRENT_POTION_NUMBER = "<CP_NUM>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean CURRENT_POTION_ENABLED = true;
    public boolean CURRENT_POTION_ALL_ENABLED = true;
    public boolean CURRENT_POTION_ONE_ENABLED = true;

    public long TIMEOUT_GLOBAL = 1L;
    public long TIMEOUT_ALL = 20000L;
    public long TIMEOUT_ONE = 10000L;

    public int MAX_LEN = CommandHelper.MAX_LEN_INFINITE;


    public CurrentPotionConfigs() {

    }


}
