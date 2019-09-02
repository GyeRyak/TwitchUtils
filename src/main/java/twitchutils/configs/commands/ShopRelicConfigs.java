package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;

public class ShopRelicConfigs extends ConfigBasis<ShopRelicConfigs> {

    public ArrayList<String> SHOP_RELIC_COMMAND = new ArrayList<>();
    public String SHOP_RELIC_MESSAGE_DEFAULT = "@<T_VIEWER> (<SR_NUM>) <SR_NAME>: <SR_DESC>";
    public String SHOP_RELICS_ONE_MESSAGE_DEFAULT = "<SR_NAME>(<SR_NUM>)";
    public String SHOP_RELICS_MESSAGE_DEFAULT = "@<T_VIEWER> <SR_LIST>";
    public String SHOP_RELICS_SEPARATOR_MESSAGE_DEFAULT = ", ";

    public static final String DESCRIPTION_SHOP_RELICS_LIST = "<SR_LIST>";
    public static final String DESCRIPTION_SHOP_RELIC_NAME = "<SR_NAME>";
    public static final String DESCRIPTION_SHOP_RELIC_DESCRIPTION = "<SR_DESC>";
    public static final String DESCRIPTION_SHOP_RELIC_NUMBER = "<SR_NUM>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean SHOP_RELIC_ENABLED = true;
    public boolean SHOP_RELIC_ALL_ENABLED = true;
    public boolean SHOP_RELIC_ONE_ENABLED = true;

    public Long TIMEOUT_GLOBAL = 1L;
    public Long TIMEOUT_ONE = 10000L;
    public Long TIMEOUT_ALL = 20000L;

    public int MAX_LEN = CommandHelper.MAX_LEN_INFINITE;


    public ShopRelicConfigs() {

    }


}
