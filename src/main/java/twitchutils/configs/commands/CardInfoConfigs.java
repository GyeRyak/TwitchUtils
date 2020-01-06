package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;

import java.util.ArrayList;

public class CardInfoConfigs extends ConfigBasis<CardInfoConfigs> {

    public ArrayList<String> CARDINFO_COMMAND = new ArrayList<>(); // command
    public String CARDINFO_INFO_MESSAGE = "@<T_VIEWER> <CI_NAME>, <CI_ID> [<CI_COST>](<CI_TYPE>, <CI_COLOR>, <CI_RARITY>): <CI_DESC>"; // only one mod command
    public String CARDINFO_NONE_MESSAGE = "@<T_VIEWER> No such card named '<CI_REQUEST>'";

    public static final String DESCRIPTION_CARDINFO_NAME = "<CI_NAME>"; // mod lists string
    public static final String DESCRIPTION_CARDINFO_DESCRIPTION = "<CI_DESC>"; // mod name
    public static final String DESCRIPTION_CARDINFO_TYPE = "<CI_TYPE>"; // mod name
    public static final String DESCRIPTION_CARDINFO_COLOR = "<CI_COLOR>"; // mod name
    public static final String DESCRIPTION_CARDINFO_RARITY = "<CI_RARITY>"; // mod name
    public static final String DESCRIPTION_CARDINFO_COST = "<CI_COST>"; // mod description
    public static final String DESCRIPTION_CARDINFO_ID = "<CI_ID>"; // mod description
    public static final String DESCRIPTION_CARDINFO_REQUEST = "<CI_REQUEST>"; // custom mod description
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>"; // command caller

    public boolean CARDINFO_ENABLED = true;

    public long TIMEOUT_GLOBAL = 100L;

    public int MAX_LEN = 500;

    public CardInfoConfigs() {

    }


}
