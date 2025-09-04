package chzzkutils.connection;

import io.github.r2turntrue.chzzk4j.ChzzkChat;
import io.github.r2turntrue.chzzk4j.ChzzkClient;
import io.github.r2turntrue.chzzk4j.ChzzkClientBuilder;
import io.github.r2turntrue.chzzk4j.ChzzkChatBuilder;
import io.github.r2turntrue.chzzk4j.event.ChatMessageEvent;
import io.github.r2turntrue.chzzk4j.event.ConnectEvent;
import io.github.r2turntrue.chzzk4j.chat.ChatMessage;

import java.util.function.BiConsumer;
import java.util.ArrayList;
import java.util.List;

public class ChzzkUtilsConnection {

    private ChzzkClient client;
    private ChzzkChat chat;
    private String channelId;
    private List<BiConsumer<String, String>> listeners = new ArrayList<>();

    public ChzzkUtilsConnection(String channelId) {
        this.channelId = channelId;
    }

    public void connect() {
        client = new ChzzkClientBuilder().build(); // Assuming no client ID/secret needed for anonymous chat reading
        chat = new ChzzkChatBuilder(client, channelId).build();

        chat.on(ConnectEvent.class, (evt) -> {
            System.out.println("connected to chat :)");
        });

        chat.on(ChatMessageEvent.class, (evt) -> {
            ChatMessage msg = evt.getMessage();
            String author = msg.getProfile() != null ? msg.getProfile().getNickname() : "Anonymous";
            String content = msg.getContent();
            for (BiConsumer<String, String> listener : listeners) {
                listener.accept(content, author);
            }
        });

        new Thread(() -> {
            try {
                chat.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (chat != null && chat.isConnected()) {
            chat.sendMessage(message);
        }
    }

    public void addListener(BiConsumer<String, String> listener) {
        listeners.add(listener);
    }
}
