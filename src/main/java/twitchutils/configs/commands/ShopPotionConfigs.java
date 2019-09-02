package twitchutils.configs.commands;

import twitchutils.configs.core.ConfigBasis;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;

public class ShopPotionConfigs extends ConfigBasis<ShopPotionConfigs> {

    public ArrayList<String> SHOP_POTION_COMMAND = new ArrayList<>();
    public String SHOP_POTION_MESSAGE_DEFAULT = "@<T_VIEWER> (<SP_NUM>) <SP_NAME>: <SP_DESC>";
    public String SHOP_POTIONS_ONE_MESSAGE_DEFAULT = "<SP_NAME>(<SP_NUM>)";
    public String SHOP_POTIONS_MESSAGE_DEFAULT = "@<T_VIEWER> <SP_LIST>";
    public String SHOP_POTIONS_SEPARATOR_MESSAGE_DEFAULT = ", ";

    public static final String DESCRIPTION_SHOP_POTIONS_LIST = "<SP_LIST>";
    public static final String DESCRIPTION_SHOP_POTION_NAME = "<SP_NAME>";
    public static final String DESCRIPTION_SHOP_POTION_DESCRIPTION = "<SP_DESC>";
    public static final String DESCRIPTION_SHOP_POTION_NUMBER = "<SP_NUM>";
    public static final String DESCRIPTION_TWITCH_VIEWER_NAME = "<T_VIEWER>";

    public boolean SHOP_POTION_ENABLED = true;
    public boolean SHOP_POTION_ALL_ENABLED = true;
    public boolean SHOP_POTION_ONE_ENABLED = true;

    public Long TIMEOUT_GLOBAL = 1L;
    public Long TIMEOUT_ONE = 10000L;
    public Long TIMEOUT_ALL = 20000L;

    public int MAX_LEN = CommandHelper.MAX_LEN_INFINITE;


    public ShopPotionConfigs() {

    }


}
