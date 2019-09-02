package twitchutils.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import de.robojumper.ststwitch.TwitchVoteOption;
import de.robojumper.ststwitch.TwitchVoter;
import twitchutils.TwitchUtils;

import java.util.function.Consumer;

public class NormalVoteOff {

    @SpirePatch(
            clz = TwitchVoter.class,
            method = "initiateVote"
    )
    public static class disableVote1 {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn<Boolean> Insert(TwitchVoter __self, TwitchVoteOption[] options, Consumer<Integer> voteCb) {
            if(TwitchUtils.vanillaVote_Disable) return SpireReturn.Return(false);
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = TwitchVoter.class,
            method = "onMessage"
    )
    public static class disableVote2 {
        @SpireInsertPatch(
                rloc = 0
        )
        public static SpireReturn Replace(TwitchVoter __self, String msg, String user) {
            if(TwitchUtils.vanillaVote_Disable) return SpireReturn.Return(null);
            else return SpireReturn.Continue();
        }
    }

}
