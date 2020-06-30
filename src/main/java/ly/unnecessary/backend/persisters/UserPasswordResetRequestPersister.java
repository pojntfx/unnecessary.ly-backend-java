package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.UserPasswordResetRequest;

public class UserPasswordResetRequestPersister {
    private Database database;

    public UserPasswordResetRequestPersister(Database database) {
        this.database = database;
    }

    public UserPasswordResetRequest saveUserPasswordResetRequest(UserPasswordResetRequest userPasswordResetRequest) {
        this.database.save(userPasswordResetRequest);

        return userPasswordResetRequest;
    }
}