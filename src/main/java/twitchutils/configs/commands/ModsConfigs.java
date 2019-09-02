package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;

import java.util.ArrayList;

public class ModsConfigs extends ConfigBasis<ModsConfigs> {

    public ArrayList<String> MOD_COMMAND = new ArrayList<>(); // command
    public String MOD_MESSAGE_NO_CUSTOM_DEFAULT = "@<T_VIEWER> <M_NAME>: <M_DESC_FULL>, <M_AUTHOR_ALL>"; // only one mod command
    public String MOD_MESSAGE_CUSTOM_DEFAULT = "@<T_VIEWER> <M_NAME>: <M_DESC_CUSTOM>, <M_AUTHOR_ALL>"; // only one mod command
    public String MODS_ONE_MESSAGE_NO_CUSTOM_DEFAULT = "<M_NAME>(<M_AUTHOR_MAIN>)"; // one mod for all mods command
    public String MODS_ONE_MESSAGE_CUSTOM_DEFAULT = "<M_NAME> - <M_DESC_CUSTOM>(<M_AUTHOR_MAIN>)"; // one mod for all mods command
    public String MODS_MESSAGE_DEFAULT = "@<T_VIEWER> <M_LIST>"; // all mods command
    public String MODS_SEPARATOR_MESSAGE_DEFAULT = ", "; // separator between mods

    public static final String DESCRIPTION_MODS_LIST = "<M_LIST>"; // mod lists string
    public static final String DESCRIPTION_MOD_NAME = "<M_NAME>"; // mod name
    public static final String DESCRIPTION_MOD_DESCRIPTION_FULL = "<M_DESC_FULL>"; // mod description
    public static final String DESCRIPTION_MOD_DESCRIPTION_CUSTOM = "<M_DESC_CUSTOM>"; // custom mod description
    public static final String DESCRIPTION_MOD_AUTHOR_MAIN = "<M_AUTHOR_MAIN>"; // mod description
    public static final String DESCRIPTION_MOD_AUTHOR_ALL = "<M_AUTHOR_ALL>"; // mod description
    public static final String DESCRIPTION_MOD_CREDIT = "<M_CREDIT>"; // mod description
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>"; // command caller

    public boolean MOD_ENABLED = true;
    public boolean MOD_ALL_ENABLED = true;
    public boolean MOD_ONE_ENABLED = true;

    public long TIMEOUT_GLOBAL = 1L;
    public long TIMEOUT_ALL = 20000L;
    public long TIMEOUT_ONE = 10000L;

    public int MAX_LEN = 500;

    public ModsConfigs() {

    }


}
