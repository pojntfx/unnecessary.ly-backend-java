package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.User;

public class UserPersister extends BasePersister<User> {
    public UserPersister(Database database) {
        super(database);
    }

    public User getUserByEmail(String email) {
        return this.getDatabase().find(User.class).where().eq("email", email).findOne();
    }
}