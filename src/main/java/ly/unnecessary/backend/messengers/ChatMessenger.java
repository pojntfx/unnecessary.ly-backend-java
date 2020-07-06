package ly.unnecessary.backend.messengers;

import java.util.function.Function;

import com.google.protobuf.InvalidProtocolBufferException;

import io.nats.client.Connection;
import ly.unnecessary.backend.converters.ChatConverter;
import ly.unnecessary.backend.entities.Chat;

/**
 * Chat subscription manager
 */
public class ChatMessenger {
    private Connection bus;
    private ChatConverter converter;
    private static String TOPIC = "uly.chats";

    public ChatMessenger(Connection bus, ChatConverter converter) {
        this.bus = bus;
        this.converter = converter;
    }

    /**
     * Broadcast chat
     * 
     * @param chat
     */
    public void publishChat(Chat chat) {
        this.bus.publish(TOPIC, this.converter.toByteArray(chat));
    }

    /**
     * Receive chats from broadcast
     * 
     * @param handler
     */
    public void receiveChats(Function<Chat, Integer> handler) {
        this.bus.createDispatcher((msg) -> {
            final Chat chat;
            try {
                chat = this.converter.fromByteArrayToInternal(msg.getData());
            } catch (InvalidProtocolBufferException e) {
                return;
            }

            handler.apply(chat);
        }).subscribe(TOPIC);
    }
}