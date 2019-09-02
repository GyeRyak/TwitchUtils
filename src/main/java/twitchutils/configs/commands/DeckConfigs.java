package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;

public class DeckConfigs extends ConfigBasis<DeckConfigs> {

    public ArrayList<String> DECK_COMMAND = new ArrayList<>();
    public String DECK_MESSAGE_DEFAULT = "@<T_VIEWER> <C_LIST>";
    public String CARD_SINGLE_MESSAGE_DEFAULT = "<C_NAME>";
    public String CARD_PLURAL_MESSAGE_DEFAULT = "<C_COUNT>x<C_NAME>";
    public String CARD_SEPARATOR_MESSAGE_DEFAULT = ", ";

    public static final String DESCRIPTION_DECK_LIST = "<C_LIST>";
    public static final String DESCRIPTION_CARD_NAME = "<C_NAME>";
    public static final String DESCRIPTION_CARD_DESCRIPTION = "<C_DESC>";
    public static final String DESCRIPTION_CARD_COUNT = "<C_COUNT>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean DECK_ENABLED = true;

    public long TIMEOUT_GLOBAL = 20000L;

    public int MAX_LEN = CommandHelper.MAX_LEN_INFINITE;

    public DeckConfigs() {

    }


}
