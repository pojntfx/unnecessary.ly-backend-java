package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.UserPasswordResetRequest;

public class UserPasswordResetRequestPersister extends BasePersister<UserPasswordResetRequest> {
    public UserPasswordResetRequestPersister(Database database) {
        super(database);
    }
}