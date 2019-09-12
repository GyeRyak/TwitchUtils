package twitchutils.helper;

import com.megacrit.cardcrawl.cards.DescriptionLine;
import twitchutils.configs.GenericConfigs;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandHelper {
    public static int MAX_LEN_INFINITE = Integer.MAX_VALUE;

    public static final ArrayList<String> ENERGY_ORIGIN = new ArrayList<String>(
            Arrays.asList("\\[E\\]", "\\[R\\]", "\\[G\\]", "\\[B\\]", "\\[P\\]", "\\[W\\]"));
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

    public static String trimDescription(ArrayList<DescriptionLine> tarDescription, int maxLen) {
        StringBuilder pendingDescription = new StringBuilder();

        pendingDescription.append(tarDescription.get(0).getText());

        int descLen = tarDescription.size();
        for(int i=1; i<descLen; i++){
            pendingDescription.append(" ");
            pendingDescription.append(tarDescription.get(i).getText());
        }

        return trimDescription(pendingDescription.toString(), maxLen);
    }

    public static boolean isDuringCoolDown(Long tarVariable, Long timeOutDelay) {
        Long nowTime = System.currentTimeMillis();
        return tarVariable + timeOutDelay > nowTime;
    }


    public static String convertEnergyToText(String tarDescription) {
        GenericConfigs genericConfigs = new GenericConfigs().getConfig();
        String ret = tarDescription;

        for(String energyOrigin: ENERGY_ORIGIN){
            ArrayList<String> energyStrings = new ArrayList<>();

            energyStrings.add("");
            energyStrings.add(energyOrigin);
            for (int i = 2; i < 2 + ENERGY_MAX_PARSE - 1; i++) {
                energyStrings.add(energyStrings.get(i - 1) + ENERGY_SEPARATOR + energyOrigin);
            }

            for (int i = ENERGY_MAX_PARSE; i > 1; i--) {
                ret = ret.replaceAll(energyStrings.get(i),
                        genericConfigs.ENERGY_TEXT_PLURAL.replaceAll(
                                GenericConfigs.DESCRIPTION_ENERGY_COUNT, "" + i));
            }
            ret = ret.replaceAll(energyStrings.get(1),
                    genericConfigs.ENERGY_TEXT_SINGLE.replaceAll(
                            GenericConfigs.DESCRIPTION_ENERGY_COUNT, "" + 1));
        }




        return ret;
    }
}
