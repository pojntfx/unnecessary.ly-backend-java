package ly.unnecessary.backend.core;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.persisters.UserPersister;

public class UserCore {
    private UserPersister persister;

    public UserCore(UserPersister persister) {
        this.persister = persister;
    }

    public User signUp(User user) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));

        this.persister.createUser(user);

        return user;
    }

    public User signIn(User user) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        var valid = BCrypt.verifyer().verify(user.getPassword().toCharArray(),
                userFromPersistence.getPassword().toCharArray());

        if (!valid.verified) {
            throw new Error("Invalid username or password");
        }

        return userFromPersistence;
    }
}