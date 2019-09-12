package twitchutils.commands;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.CardInfoConfigs;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommandCardInfo {

    CardInfoConfigs cardInfoConfigs;
    HashMap<String, AbstractCard> cardInfos = new HashMap<>();

    public Long timeStampGlobal = 0L;

    public CommandCardInfo() {
        cardInfoConfigs = new CardInfoConfigs().getConfig();

        ArrayList<AbstractCard> cardDatum = CardLibrary.getAllCards(); // load all cards
        for(AbstractCard tarCard: cardDatum){
            cardInfos.put(tarCard.name.replaceAll(" ", ""), tarCard);
        }
    }


    public void addCommand() {
        addCommandCardInfo();
    }

    public void addCommandCardInfo() {
        if (cardInfoConfigs.CARDINFO_ENABLED &&
                AbstractDungeon.topPanel.twitch.isPresent()) {
            ((List<TwitchMessageListener>)
                    (ReflectionHacks.getPrivate(
                            AbstractDungeon.topPanel.twitch.get().connection,
                            TwitchConnection.class,
                            "listeners"))).add((msg, user) -> { // add listener

                String leftOver = CommandHelper.getCommandLeftOver(cardInfoConfigs.CARDINFO_COMMAND, msg);
                if (leftOver != null) {
                    if (leftOver.isEmpty()) {
                        return; // do nothing
                    } else {
                        messageForCard(leftOver, user);
                    }
                }

            });
        }
    }

    public void messageForCard(String msg, String user) {
        boolean isUpgraded = false;
        String originalMsg = msg;

        if (CommandHelper.isDuringCoolDown(timeStampGlobal, cardInfoConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();

        if(msg.contains("+")){
            isUpgraded = true;
            msg = msg.replaceAll("\\+", "");
        }
        msg = msg.replaceAll(" ", "");

        if (cardInfos.containsKey(msg)) {
            AbstractCard tarCard = cardInfos.get(msg).makeStatEquivalentCopy();
            if(tarCard.canUpgrade() && isUpgraded){
                tarCard.upgrade();
            }

            final String pendingMessage = getParsedCardString(cardInfoConfigs.CARDINFO_INFO_MESSAGE, tarCard, user);

            AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
                twitchPanel.connection.sendMessage(pendingMessage);
            });
        }
        else{
            final String pendingMessage = cardInfoConfigs.CARDINFO_NONE_MESSAGE
                    .replaceAll(CardInfoConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user)
                    .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_REQUEST, originalMsg);

            AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
                twitchPanel.connection.sendMessage(pendingMessage);
            });
        }
    }

    public String getParsedCardString(String origin, AbstractCard card) {
        return origin
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_DESCRIPTION,
                        CommandHelper.trimDescription(card.description, cardInfoConfigs.MAX_LEN)
                        .replaceAll("!D!", String.valueOf(card.baseDamage))
                        .replaceAll("!M!", String.valueOf(card.baseMagicNumber))
                        .replaceAll("!B!", String.valueOf(card.baseBlock))
                        .replaceAll("\\*", "")
                )
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_TYPE,
                        typeString(card.type))
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_COLOR, // 기존 캐릭터랑 모드캐릭(BaseMod.getModdedChar...) 해서 해당 캐릭터 이름이랑 해당 캐릭터의 CardColor을 Map에 넣어두고 찾는 방식으로
                        CardLibrary.LibraryType.valueOf(card.color.name()).name())
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_RARITY, // 그냥 무식하게 희귀도 해당하는 파일 찾아서
                        card.rarity.name()) 
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_COST,
                        (card.cost==-2?"-":(card.cost==-1?"X":String.valueOf(card.cost))))
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_NAME, card.name);
    }

    public static String typeString(AbstractCard.CardType type) {
        switch (type) {
            case ATTACK: {
                return AbstractCard.TEXT[0];
            }
            case SKILL: {
                return AbstractCard.TEXT[1];
            }
            case POWER: {
                return AbstractCard.TEXT[2];
            }
            case STATUS: {
                return AbstractCard.TEXT[7];
            }
            case CURSE: {
                return AbstractCard.TEXT[3];
            }
            default: {
                return AbstractCard.TEXT[5];
            }
        }
    }

    public String getParsedCardString(String origin, AbstractCard card, String user) {
        return getParsedCardString(origin, card)
                .replaceAll(CardInfoConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);
    }
}
