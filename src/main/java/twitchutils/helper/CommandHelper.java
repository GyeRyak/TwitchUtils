package twitchutils.helper;

import twitchutils.configs.GenericConfigs;

import java.util.ArrayList;

public class CommandHelper {
    public static int MAX_LEN_INFINITE = Integer.MAX_VALUE;

    public static final String ENERGY_ORIGIN = "\\[E\\]";
    public static final String ENERGY_SEPARATOR = " ";
    public static final int ENERGY_MAX_PARSE = 10;

    public static String getCommandLeftOver(String commandPrefix, String originalCommand) {
        if (!originalCommand.startsWith(commandPrefix)) return null;
        return originalCommand.replace(commandPrefix + " ", "")
                .replace(commandPrefix, "");
    }

    public static String getCommandLeftOver(ArrayList<String> commandPrefix, String originalCommand) {
        for (String s : commandPrefix) {
            if (originalCommand.startsWith(s)) {
                return originalCommand.replace(s + " ", "")
                        .replace(s, "");
            }
        }
        return null;

    }

    public static int convertToInt(String tarString) {
        int ret;
        try {
            ret = Integer.parseInt(tarString);
        } catch (NumberFormatException e) {
            return -1;
        }
        return ret;
    }

    public static String trimDescription(String tarDescription) {
        return convertEnergyToText(tarDescription)
                .replace(" [REMOVE_SPACE]", "")
                .replace("[]", "")
                .replace("_", " ")
                .replaceAll("\\[#......]", "")
                .replaceAll("#.", "")
                .replaceAll("NL", "");
    }

    public static String trimDescription(String tarDescription, int maxLen) {
        if (maxLen == MAX_LEN_INFINITE) return trimDescription(tarDescription);

        String ret = trimDescription(tarDescription);
        if (ret.length() <= maxLen) return ret;
        else return ret.substring(0, maxLen - 1) + "...";
    }

    public static boolean isDuringCoolDown(Long tarVariable, Long timeOutDelay) {
        Long nowTime = System.currentTimeMillis();
        return tarVariable + timeOutDelay > nowTime;
    }


    public static String convertEnergyToText(String tarDescription) {
        ArrayList<String> energyStrings = new ArrayList<>();
        String ret = tarDescription;

        energyStrings.add("");
        energyStrings.add(ENERGY_ORIGIN);
        for (int i = 2; i < 2 + ENERGY_MAX_PARSE - 1; i++) {
            energyStrings.add(energyStrings.get(i - 1) + ENERGY_SEPARATOR + ENERGY_ORIGIN);
        }

        GenericConfigs genericConfigs = new GenericConfigs().getConfig();
        for (int i = ENERGY_MAX_PARSE; i > 1; i--) {
            ret = ret.replaceAll(energyStrings.get(i),
                    genericConfigs.ENERGY_TEXT_PLURAL.replaceAll(
                            GenericConfigs.DESCRIPTION_ENERGY_COUNT, "" + i));
        }
        ret = ret.replaceAll(energyStrings.get(1),
                genericConfigs.ENERGY_TEXT_SINGLE.replaceAll(
                        GenericConfigs.DESCRIPTION_ENERGY_COUNT, "" + 1));

        return ret;
    }
}
