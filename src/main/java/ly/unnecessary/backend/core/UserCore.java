package ly.unnecessary.backend.core;

import java.util.List;

import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.entities.UserPasswordResetRequest;
import ly.unnecessary.backend.entities.UserSignUpRequest;
import ly.unnecessary.backend.persisters.UserPersister;
import ly.unnecessary.backend.utilities.Hasher;

public class UserCore {
    private UserPersister persister;
    private UserSignUpRequestCore userSignUpRequestCore;
    private UserPasswordResetRequestCore userPasswordResetRequestCore;
    private Hasher hasher;

    public UserCore(UserPersister persister, UserSignUpRequestCore userSignUpRequestCore,
            UserPasswordResetRequestCore userPasswordResetRequestCore, Hasher hasher) {
        this.persister = persister;
        this.userSignUpRequestCore = userSignUpRequestCore;
        this.userPasswordResetRequestCore = userPasswordResetRequestCore;
        this.hasher = hasher;
    }

    public User requestSignUp(User user) {
        var userSignUpRequest = new UserSignUpRequest();
        this.userSignUpRequestCore.createRequest(userSignUpRequest, user.getEmail());

        user.setUserSignupRequest(userSignUpRequest);
        user.setPassword(this.hasher.hash(user.getPassword()));

        this.persister.saveUser(user);

        return user;
    }

    public User confirmSignUp(User user, UserSignUpRequest userSignUpRequest) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        var userSignUpRequestFromPersistence = this.userSignUpRequestCore
                .validateSignUpRequest(userFromPersistence.getUserSignupRequest(), userSignUpRequest.getToken());

        if (!userSignUpRequestFromPersistence.isConfirmed()) {
            throw new Error("Invalid sign up confirmation token");
        }

        return userFromPersistence;
    }

    public User signIn(User user) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        if (!userFromPersistence.getUserSignupRequest().isConfirmed()) {
            throw new Error("User has not yet confirmed sign up");
        }

        var valid = this.hasher.verify(userFromPersistence.getPassword(), user.getPassword());

        if (!valid) {
            throw new Error("Invalid username or password");
        }

        return userFromPersistence;
    }

    public User requestPasswordReset(User user) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        if (userFromPersistence == null) {
            throw new Error("No user with this email found");
        }

        var userPasswordResetRequest = new UserPasswordResetRequest();
        this.userPasswordResetRequestCore.createRequest(userPasswordResetRequest, userFromPersistence.getEmail());

        userFromPersistence.setUserPasswordResetRequest(userPasswordResetRequest);

        this.persister.saveUser(userFromPersistence);

        return userFromPersistence;
    }

    public User confirmPasswordReset(User user, UserPasswordResetRequest userPasswordResetRequest) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        var userSignUpRequestFromPersistence = this.userPasswordResetRequestCore.validatePasswordResetRequest(
                userFromPersistence.getUserPasswordResetRequest(), userPasswordResetRequest.getToken());

        if (!userSignUpRequestFromPersistence.isConfirmed()) {
            throw new Error("Invalid password reset confirmation token");
        }

        userFromPersistence.setPassword(this.hasher.hash(user.getPassword()));

        this.persister.saveUser(userFromPersistence);

        return userFromPersistence;
    }

    public List<Community> listOwnedCommunities(User user) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        return userFromPersistence.getOwnedCommunities();
    }

    public List<Community> listMemberCommunities(User user) {
        var userFromPersistence = this.persister.getUserByEmail(user.getEmail());

        return userFromPersistence.getMemberCommunities();
    }
}