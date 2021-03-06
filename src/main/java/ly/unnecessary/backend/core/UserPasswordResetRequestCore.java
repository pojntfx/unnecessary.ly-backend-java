package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.UserPasswordResetRequest;
import ly.unnecessary.backend.persisters.UserPasswordResetRequestPersister;
import ly.unnecessary.backend.utilities.Hasher;
import ly.unnecessary.backend.utilities.Emailer;

/**
 * Password reset business logic
 */
public class UserPasswordResetRequestCore {
    private UserPasswordResetRequestPersister persister;
    private Emailer emailer;
    private Hasher hasher;

    public UserPasswordResetRequestCore(UserPasswordResetRequestPersister persister, Emailer emailer, Hasher hasher) {
        this.persister = persister;
        this.emailer = emailer;
        this.hasher = hasher;
    }

    /**
     * Request a password reset
     */
    public UserPasswordResetRequest createRequest(UserPasswordResetRequest userPasswordResetRequest, String email) {
        var token = this.emailer.requestConfirmation(email);

        userPasswordResetRequest.setToken(this.hasher.hash(token));

        this.persister.save(userPasswordResetRequest);

        return userPasswordResetRequest;
    }

    /**
     * Validate a password reset request
     */
    public UserPasswordResetRequest validatePasswordResetRequest(UserPasswordResetRequest userPasswordResetRequest,
            String token) {
        if (this.hasher.verify(userPasswordResetRequest.getToken(), token)) {
            userPasswordResetRequest.setConfirmed(true);

            this.persister.save(userPasswordResetRequest);

            return userPasswordResetRequest;
        }

        userPasswordResetRequest.setConfirmed(false);

        return userPasswordResetRequest;
    }
}