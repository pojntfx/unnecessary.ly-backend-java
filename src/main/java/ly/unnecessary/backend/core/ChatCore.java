package ly.unnecessary.backend.core;

import java.util.function.Function;

import ly.unnecessary.backend.entities.Chat;
import ly.unnecessary.backend.messengers.ChatMessenger;
import ly.unnecessary.backend.persisters.ChatPersister;

public class ChatCore {
    private ChatPersister persister;
    private ChatMessenger messenger;

    public ChatCore(ChatPersister persister, ChatMessenger messenger) {
        this.persister = persister;
        this.messenger = messenger;
    }

    public Chat createChat(Chat chat) {
        this.persister.saveChat(chat);

        this.messenger.publishChat(chat);

        return chat;
    }

    public void subscribeToChats(Function<Chat, Integer> handler) {
        this.messenger.receiveChats(handler);
    }
}