package twitchutils.helper;

import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import twitchutils.TwitchUtils;
import twitchutils.fetchers.LanguageFetcher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResourcesManager {
    public static final String RESOURCES_NAME = TwitchUtils.MOD_NAME +"Resources";
    public static final String EXT = "json";
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static final String CATEGORY_LOCALIZATION = "localization";
    public static final String CATEGORY_SETTINGS = "settings";
    public static final String CATEGORY_IMAGE = "img";
    public static final String CATEGORY_UI = "ui";

    public static String makeFilePath(String modName, String fileName, String ext) {
        String dirPath;
        if (modName == null) {
            dirPath = ConfigUtils.CONFIG_DIR + File.separator;
        } else {
            dirPath = ConfigUtils.CONFIG_DIR + File.separator + modName + File.separator;
        }

        String filePath = dirPath + fileName + "." + ext;
        File dir = new File(dirPath);
        dir.mkdirs();
        return filePath;
    }

    public static String getFilePath() {
        String dirPath;
        dirPath = ConfigUtils.CONFIG_DIR + File.separator;

        File dir = new File(dirPath);
        dir.mkdirs();
        return dirPath;
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, Charset encoding, String content)
            throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(content);
        Files.write(Paths.get(path), lines, encoding);
    }

    public static String getLocalizationPath(String type, String name) {
        return RESOURCES_NAME + "/" + CATEGORY_LOCALIZATION + "/" + LanguageFetcher.getLanaguage() + "/"
                + type + "/" + name + "." + EXT;
    }

    public static String getInnerPath(String category, String name, String ext){
        return RESOURCES_NAME + "/" + category + "/" + name + "." + ext;
    }

}
