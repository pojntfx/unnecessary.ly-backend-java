package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.UserSignUpRequest;

/**
 * Sign up persister
 */
public class UserSignUpRequestPersister extends BasePersister<UserSignUpRequest> {
    public UserSignUpRequestPersister(Database database) {
        super(database);
    }
}