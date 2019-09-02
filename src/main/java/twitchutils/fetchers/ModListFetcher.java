package twitchutils.fetchers;

import com.evacipated.cardcrawl.modthespire.ModInfo;

import java.util.ArrayList;
import java.util.Arrays;

import static com.evacipated.cardcrawl.modthespire.Loader.MODINFOS;

public class ModListFetcher {

    public static ArrayList<ModInfo> getModInfos() {
        return new ArrayList<>(Arrays.asList(MODINFOS));
    }


}

