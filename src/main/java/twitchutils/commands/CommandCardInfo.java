package twitchutils.commands;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.CardInfoConfigs;
import twitchutils.helper.CharacterHelper;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommandCardInfo {

    public CardInfoConfigs cardInfoConfigs;
    public HashMap<String, AbstractCard> cardInfos = new HashMap<>();

    public Long timeStampGlobal = 0L;

    public CommandCardInfo() {
        cardInfoConfigs = new CardInfoConfigs().getConfig();

        ArrayList<AbstractCard> cardDatum = CardLibrary.getAllCards(); // load all cards
        for(AbstractCard tarCard: cardDatum){
            cardInfos.put(tarCard.name.replaceAll(" ", "").toLowerCase(), tarCard);
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
        msg = msg.replaceAll(" ", "").toLowerCase();

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
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_COLOR,
                        CharacterHelper.getCharacterHelper().getLocalizedCharacterName(card.color))
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_RARITY,
                        CharacterHelper.getCharacterHelper().getLocalizedCardRarity(card.rarity))
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_COST,
                        (card.cost==-2?"-":(card.cost==-1?"X":String.valueOf(card.cost))))
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_NAME, card.name)
                .replaceAll(CardInfoConfigs.DESCRIPTION_CARDINFO_ID, card.cardID);
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
