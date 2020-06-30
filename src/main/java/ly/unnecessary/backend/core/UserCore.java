package ly.unnecessary.backend.core;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.entities.UserSignUpRequest;
import ly.unnecessary.backend.persisters.UserPersister;

public class UserCore {
    private UserPersister persister;
    private UserSignUpRequestCore userSignUpRequestCore;

    public UserCore(UserPersister persister, UserSignUpRequestCore userSignUpRequestCore) {
        this.persister = persister;
        this.userSignUpRequestCore = userSignUpRequestCore;
    }

    public User requestSignUp(User user) {
        var userSignUpRequest = new UserSignUpRequest();
        this.userSignUpRequestCore.createRequest(userSignUpRequest, user.getEmail());

        user.setUserSignUpRequest(userSignUpRequest);
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));

        this.persister.createUser(user);

        return user;
    }

    public User confirmSignUp(User user, UserSignUpRequest userSignUpRequest) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        var userSignUpRequestFromPersistence = this.userSignUpRequestCore
                .validateSignUpRequest(userFromPersistence.getUserSignUpRequest(), userSignUpRequest.getToken());

        if (!userSignUpRequestFromPersistence.isConfirmed()) {
            throw new Error("Invalid confirmation token");
        }

        return userFromPersistence;
    }

    public User signIn(User user) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        if (!userFromPersistence.getUserSignUpRequest().isConfirmed()) {
            throw new Error("User has not yet confirmed sign up");
        }

        var valid = BCrypt.verifyer().verify(user.getPassword().toCharArray(),
                userFromPersistence.getPassword().toCharArray());

        if (!valid.verified) {
            throw new Error("Invalid username or password");
        }

        return userFromPersistence;
    }
}