package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.UserSignUpRequest;

public class UserSignUpRequestPersister {
    private Database database;

    public UserSignUpRequestPersister(Database database) {
        this.database = database;
    }

    public UserSignUpRequest saveUserSignUpRequest(UserSignUpRequest userSignupRequest) {
        this.database.save(userSignupRequest);

        return userSignupRequest;
    }
}