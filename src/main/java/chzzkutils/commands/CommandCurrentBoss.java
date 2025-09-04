package chzzkutils.commands;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import chzzkutils.ChzzkUtils;
import chzzkutils.configs.commands.CurrentBossConfigs;
import chzzkutils.configs.commands.DeckConfigs;
import chzzkutils.helper.CommandHelper;


public class CommandCurrentBoss {

    public CurrentBossConfigs currentBossConfigs;

    public Long timeStampGlobal = 0L;

    public static int ACT_UNKNOWN = -(0xff);


    public CommandCurrentBoss() {
        currentBossConfigs = new CurrentBossConfigs().getConfig();
    }

    public void addCommand() {
        addCommandCurrentBoss();
    }

    public void addCommandCurrentBoss() {
        if (currentBossConfigs.CURRENTBOSS_ENABLED && ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.addListener((msg, user) -> {
                if (CardCrawlGame.isInARun()) { // game run check
                    String leftOver = CommandHelper.getCommandLeftOver(currentBossConfigs.CURRENTBOSS_COMMAND, msg);
                    if (leftOver != null) {
                        messageForDeck(msg, user);
                    }
                }
            });
        }
    }


    private void messageForDeck(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, currentBossConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();

        String bossName = getCurrentBoss();
        int actNumber = getCurrentAct();

        String pendingMessage = currentBossConfigs.CURRENTBOSS_MESSAGE // message build
                .replaceAll(CurrentBossConfigs.DESCRIPTION_CURRENTBOSS_BOSS, bossName)
                .replaceAll(CurrentBossConfigs.DESCRIPTION_CURRENTBOSS_ACT, actNumber + "")
                .replaceAll(DeckConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (bossName.isEmpty() || actNumber == ACT_UNKNOWN || pendingMessage.isEmpty()) return;

        if (ChzzkUtils.chzzkConnection != null) {
            ChzzkUtils.chzzkConnection.sendMessage(pendingMessage);
        }
    }

    public String getCurrentBoss() {
        String encounterName = MonsterHelper.getEncounterName(AbstractDungeon.bossKey);
        String actualName = BaseMod.getMonsterName(encounterName);

        if (actualName.isEmpty()) return encounterName;
        else return actualName;
    }

    public int getCurrentAct() {
        return AbstractDungeon.actNum;
    }


}
