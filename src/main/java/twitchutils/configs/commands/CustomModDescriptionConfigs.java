package twitchutils.configs.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import twitchutils.TwitchUtils;
import twitchutils.configs.GenericConfigs;
import twitchutils.helper.ResourcesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CustomModDescriptionConfigs {

    public HashMap<String, String> CUSTOM_MOD_DESCRIPTION = new HashMap<>();
    public HashMap<String, Boolean> CUSTOM_MOD_HIDE = new HashMap<>();
    public ArrayList<String> COMMENTS = new ArrayList<>();

    public static final String NONE_DESCRIPTION = "<NONE>";


    public void generateModDefault(String modName) {
        if (!CUSTOM_MOD_DESCRIPTION.containsKey(modName)) {
            CUSTOM_MOD_DESCRIPTION.put(modName, NONE_DESCRIPTION);
        }
        if (!CUSTOM_MOD_HIDE.containsKey(modName)) {
            CUSTOM_MOD_HIDE.put(modName, false);
        }
    }

    public String getModDescription(String modName) {
        if (CUSTOM_MOD_DESCRIPTION.containsKey(modName) &&
                !CUSTOM_MOD_DESCRIPTION.get(modName).equals(NONE_DESCRIPTION)) {
            return CUSTOM_MOD_DESCRIPTION.get(modName);
        }
        return "";
    }

    public boolean isModHidden(String modName) {
        if (CUSTOM_MOD_HIDE.containsKey(modName)) {
            return CUSTOM_MOD_HIDE.get(modName);
        }
        return false;
    }

    public Set<String> getKeySet() {
        return CUSTOM_MOD_DESCRIPTION.keySet();
    }

    public void generateNowStatus() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String json = gson.toJson(this);

        try {
            ResourcesManager.writeFile(
                    ResourcesManager.makeFilePath(TwitchUtils.MOD_NAME,
                            this.getClass().asSubclass(this.getClass()).getSimpleName(),
                            ResourcesManager.EXT),
                    ResourcesManager.UTF_8,
                    json
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateDefault() {
        GenericConfigs genericConfigs = new GenericConfigs().getConfig();

        CUSTOM_MOD_DESCRIPTION = new HashMap<>();
        CUSTOM_MOD_DESCRIPTION.put("twitchutils",
                CardCrawlGame.languagePack.getUIString(TwitchUtils.makeID(TwitchUtils.UI_MOD_DESC)).TEXT[3]);
        CUSTOM_MOD_DESCRIPTION.put("basemod",
                CardCrawlGame.languagePack.getUIString(TwitchUtils.makeID(TwitchUtils.UI_MOD_DESC)).TEXT[1]);
        CUSTOM_MOD_HIDE.put("twitchutils", false);
        CUSTOM_MOD_HIDE.put("basemod", false);

        generateNowStatus();
    }

    public CustomModDescriptionConfigs tryReadConfig() throws IOException {
        String fileContent = ResourcesManager.readFile(
                ResourcesManager.makeFilePath(TwitchUtils.MOD_NAME,
                        this.getClass().asSubclass(this.getClass()).getSimpleName(),
                        ResourcesManager.EXT),
                ResourcesManager.UTF_8
        );
        Gson gson = new Gson();
        return gson.fromJson(fileContent, this.getClass().asSubclass(this.getClass()));
    }


    public CustomModDescriptionConfigs getConfig() {
        try {
            return tryReadConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                generateDefault();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        try {
            generateDefault();
            return tryReadConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
