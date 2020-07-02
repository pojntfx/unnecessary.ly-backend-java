package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Chat;

public class ChatPersister {
    private Database database;

    public ChatPersister(Database database) {
        this.database = database;
    }

    public Chat saveChat(Chat chat) {
        this.database.save(chat);

        return chat;
    }
}