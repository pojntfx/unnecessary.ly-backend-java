package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.Chat;
import ly.unnecessary.backend.persisters.ChatPersister;

public class ChatCore {
    private ChatPersister persister;

    public ChatCore(ChatPersister persister) {
        this.persister = persister;
    }

    public Chat createChat(Chat chat) {
        this.persister.saveChat(chat);

        // TODO: Send createChat notification to all subscribers of a channel

        return chat;
    }
}