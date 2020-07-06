package ly.unnecessary.backend.core;

import java.util.function.Function;

import ly.unnecessary.backend.entities.Chat;
import ly.unnecessary.backend.messengers.ChatMessenger;
import ly.unnecessary.backend.persisters.ChatPersister;

/**
 * Chat business logic
 */
public class ChatCore {
    private ChatPersister persister;
    private ChatMessenger messenger;

    public ChatCore(ChatPersister persister, ChatMessenger messenger) {
        this.persister = persister;
        this.messenger = messenger;
    }

    /**
     * Create chat
     * 
     * @param chat
     * @return Chat
     */
    public Chat createChat(Chat chat) {
        this.persister.save(chat);

        this.messenger.publishChat(chat);

        return chat;
    }

    /**
     * Subscribe to chats from broadcast
     * 
     * @param handler
     */
    public void subscribeToChats(Function<Chat, Integer> handler) {
        this.messenger.receiveChats(handler);
    }
}