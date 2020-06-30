package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.UserPasswordResetRequest;
import ly.unnecessary.backend.persisters.UserPasswordResetRequestPersister;
import ly.unnecessary.backend.utilities.Hasher;
import ly.unnecessary.backend.utilities.Emailer;

public class UserPasswordResetRequestCore {
    private UserPasswordResetRequestPersister persister;
    private Emailer emailer;
    private Hasher hasher;

    public UserPasswordResetRequestCore(UserPasswordResetRequestPersister persister, Emailer emailer, Hasher hasher) {
        this.persister = persister;
        this.emailer = emailer;
        this.hasher = hasher;
    }

    public UserPasswordResetRequest createRequest(UserPasswordResetRequest userPasswordResetRequest, String email) {
        var token = this.emailer.requestConfirmation(email);

        userPasswordResetRequest.setToken(this.hasher.hash(token));

        this.persister.saveUserPasswordResetRequest(userPasswordResetRequest);

        return userPasswordResetRequest;
    }

    public UserPasswordResetRequest validatePasswordResetRequest(UserPasswordResetRequest userPasswordResetRequest,
            String token) {
        if (this.hasher.verify(userPasswordResetRequest.getToken(), token)) {
            userPasswordResetRequest.setConfirmed(true);

            this.persister.saveUserPasswordResetRequest(userPasswordResetRequest);

            return userPasswordResetRequest;
        }

        userPasswordResetRequest.setConfirmed(false);

        return userPasswordResetRequest;
    }
}