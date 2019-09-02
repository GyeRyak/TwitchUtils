package twitchutils.commands;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchMessageListener;
import twitchutils.configs.commands.DeckConfigs;
import twitchutils.helper.CommandHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommandDeck {

    public DeckConfigs deckConfigs;

    public Long timeStampGlobal = 0L;


    public CommandDeck() {
        deckConfigs = new DeckConfigs().getConfig();
    }

    public void addCommand() {
        addCommandDeck();
    }

    public void addCommandDeck() {
        if (deckConfigs.DECK_ENABLED &&
                AbstractDungeon.topPanel.twitch.isPresent()) {
            ((List<TwitchMessageListener>)
                    (ReflectionHacks.getPrivate(
                            AbstractDungeon.topPanel.twitch.get().connection,
                            TwitchConnection.class,
                            "listeners"))).add((msg, user) -> { // add listener
                if (CardCrawlGame.isInARun()) { // game run check
                    String leftOver = CommandHelper.getCommandLeftOver(deckConfigs.DECK_COMMAND, msg);
                    if (leftOver != null) {
                        messageForDeck(msg, user);
                    }
                }
            });
        }
    }


    private void messageForDeck(String msg, String user) {
        if (CommandHelper.isDuringCoolDown(timeStampGlobal, deckConfigs.TIMEOUT_GLOBAL))
            return; // timeout check
        timeStampGlobal = System.currentTimeMillis();

        ArrayList<AbstractCard> cards = new ArrayList<>(AbstractDungeon.player.masterDeck.group);
        HashMap<String, AbstractCard> cardInfoMap = new HashMap<>();
        HashMap<String, Integer> cardCountMap = new HashMap<>();
        for (AbstractCard card : cards) {
            String cardName = card.name;
            if (!cardInfoMap.containsKey(cardName)) {
                cardInfoMap.put(cardName, card);
            }
            if (!cardCountMap.containsKey(cardName)) {
                cardCountMap.put(cardName, 0);
            }
            cardCountMap.put(cardName, cardCountMap.get(cardName) + 1);
        }

        ArrayList<String> cardStrings = new ArrayList<>();
        for (String cardName : cardInfoMap.keySet()) {
            AbstractCard tarCard = cardInfoMap.get(cardName);
            if (cardCountMap.get(cardName) == 1) { // single
                cardStrings.add(
                        getParsedCardString(
                                deckConfigs.CARD_SINGLE_MESSAGE_DEFAULT,
                                tarCard,
                                cardCountMap.get(cardName)));
            } else {
                cardStrings.add(
                        getParsedCardString(
                                deckConfigs.CARD_PLURAL_MESSAGE_DEFAULT,
                                tarCard,
                                cardCountMap.get(cardName)));
            }
        }

        String cardLists = String.join(deckConfigs.CARD_SEPARATOR_MESSAGE_DEFAULT, cardStrings);

        String pendingMessage = deckConfigs.DECK_MESSAGE_DEFAULT // message build
                .replaceAll(DeckConfigs.DESCRIPTION_DECK_LIST, cardLists)
                .replaceAll(DeckConfigs.DESCRIPTION_TWITCH_VIEWER_NAME, user);

        if (pendingMessage.isEmpty()) return;

        AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> { // twitch panel check
            twitchPanel.connection.sendMessage(pendingMessage);
        });
    }


    public String getParsedCardString(String originString, AbstractCard tarCard, int count) {
        return originString
                .replaceAll(DeckConfigs.DESCRIPTION_CARD_COUNT, "" + count)
                .replaceAll(DeckConfigs.DESCRIPTION_CARD_NAME, tarCard.name)
                .replaceAll(DeckConfigs.DESCRIPTION_CARD_DESCRIPTION,
                        CommandHelper.trimDescription(tarCard.rawDescription, deckConfigs.MAX_LEN));
    }


}
