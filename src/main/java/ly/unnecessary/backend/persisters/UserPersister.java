package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.User;

public class UserPersister {
    private Database database;

    public UserPersister(Database database) {
        this.database = database;
    }

    public User createUser(User user) {
        this.database.save(user);

        return user;
    }

    public User getUserByEmail(String email) {
        return this.database.find(User.class).where().eq("email", email).findOne();
    }
}