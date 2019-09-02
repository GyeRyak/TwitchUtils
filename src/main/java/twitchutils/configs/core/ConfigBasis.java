package twitchutils.configs.core;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import twitchutils.TwitchUtils;
import twitchutils.helper.ResourcesManager;

import java.io.IOException;
import java.util.ArrayList;

public class ConfigBasis<I extends ConfigBasis> {

    public ArrayList<String> COMMENTS = new ArrayList<>();

    public void makeConfig() throws IOException {
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

    public I tryReadConfig() throws IOException {
        String fileContent = ResourcesManager.readFile(
                ResourcesManager.makeFilePath(TwitchUtils.MOD_NAME,
                        this.getClass().asSubclass(this.getClass()).getSimpleName(),
                        ResourcesManager.EXT),
                ResourcesManager.UTF_8
        );
        Gson gson = new Gson();
        return gson.fromJson(fileContent, this.getClass().asSubclass(this.getClass()));
    }

    public void copyDefault() throws IOException {
        String origin = Gdx.files.internal
                (ResourcesManager.getLocalizationPath(ResourcesManager.CATEGORY_SETTINGS,
                        this.getClass().asSubclass(this.getClass()).getSimpleName()))
                .readString(String.valueOf(ResourcesManager.UTF_8));
        ResourcesManager.writeFile(
                ResourcesManager.makeFilePath(TwitchUtils.MOD_NAME,
                        this.getClass().asSubclass(this.getClass()).getSimpleName(),
                        ResourcesManager.EXT),
                ResourcesManager.UTF_8,
                origin
        );
    }

    public I getConfig() {
        try {
            return tryReadConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                copyDefault();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        try {
            return tryReadConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                makeConfig();
                return tryReadConfig();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }
    }
}
