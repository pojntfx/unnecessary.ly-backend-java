package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.entities.UserSignUpRequest;
import ly.unnecessary.backend.persisters.UserPersister;
import ly.unnecessary.backend.utilities.Hasher;

public class UserCore {
    private UserPersister persister;
    private UserSignUpRequestCore userSignUpRequestCore;
    private Hasher hasher;

    public UserCore(UserPersister persister, UserSignUpRequestCore userSignUpRequestCore, Hasher hasher) {
        this.persister = persister;
        this.userSignUpRequestCore = userSignUpRequestCore;
        this.hasher = hasher;
    }

    public User requestSignUp(User user) {
        var userSignUpRequest = new UserSignUpRequest();
        this.userSignUpRequestCore.createRequest(userSignUpRequest, user.getEmail());

        user.setUserSignUpRequest(userSignUpRequest);
        user.setPassword(this.hasher.hash(user.getPassword()));

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

        var valid = this.hasher.verify(userFromPersistence.getPassword(), user.getPassword());

        if (!valid) {
            throw new Error("Invalid username or password");
        }

        return userFromPersistence;
    }
}