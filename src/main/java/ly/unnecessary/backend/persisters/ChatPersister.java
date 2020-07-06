package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Chat;

public class ChatPersister extends BasePersister<Chat> {
    public ChatPersister(Database database) {
        super(database);
    }
}