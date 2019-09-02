package twitchutils.configs.twitch;

import twitchutils.configs.core.ConfigBasis;

import java.util.ArrayList;

public class VanillaVoteConfigs extends ConfigBasis<VanillaVoteConfigs> {

    public boolean NORMAL_VOTE_DISABLE = true;
    public ArrayList<String> VOTE_PREFIX = new ArrayList<>();

    public VanillaVoteConfigs(){

    }

}
