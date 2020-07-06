package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.User;

/**
 * User persister
 */
public class UserPersister extends BasePersister<User> {
    public UserPersister(Database database) {
        super(database);
    }

    /**
     * Get a user by their email
     * 
     * @param email
     * @return User
     */
    public User getUserByEmail(String email) {
        return this.getDatabase().find(User.class).where().eq("email", email).findOne();
    }
}