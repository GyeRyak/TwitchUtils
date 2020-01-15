package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrentBossConfigs extends ConfigBasis<CurrentBossConfigs> {

    public ArrayList<String> CURRENTBOSS_COMMAND = new ArrayList<>(Arrays.asList("!cb", "!currentboss")); // command
    public String CURRENTBOSS_MESSAGE = "@<T_VIEWER> Act <CB_ACT>: <CB_BOSS>";

    public static final String DESCRIPTION_CURRENTBOSS_BOSS = "<CB_BOSS>";
    public static final String DESCRIPTION_CURRENTBOSS_ACT = "<CB_ACT>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>"; // command caller

    public boolean CURRENTBOSS_ENABLED = true;

    public long TIMEOUT_GLOBAL = 1000L;

    public int MAX_LEN = 500;

    public CurrentBossConfigs() {

    }


}
