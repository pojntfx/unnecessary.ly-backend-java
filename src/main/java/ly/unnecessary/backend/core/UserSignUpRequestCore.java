package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.UserSignUpRequest;
import ly.unnecessary.backend.persisters.UserSignUpRequestPersister;
import ly.unnecessary.backend.utilities.Hasher;
import ly.unnecessary.backend.utilities.Emailer;

/**
 * Sign up business logic
 */
public class UserSignUpRequestCore {
    private UserSignUpRequestPersister persister;
    private Emailer emailer;
    private Hasher hasher;

    public UserSignUpRequestCore(UserSignUpRequestPersister persister, Emailer emailer, Hasher hasher) {
        this.persister = persister;
        this.emailer = emailer;
        this.hasher = hasher;
    }

    /**
     * Create a sign up request
     */
    public UserSignUpRequest createRequest(UserSignUpRequest userSignupRequest, String email) {
        var token = this.emailer.requestConfirmation(email);

        userSignupRequest.setToken(this.hasher.hash(token));

        this.persister.save(userSignupRequest);

        return userSignupRequest;
    }

    /**
     * Validate a sign up request
     * 
     * @param userSignupRequest
     * @param token
     * @return
     */
    public UserSignUpRequest validateSignUpRequest(UserSignUpRequest userSignupRequest, String token) {
        if (this.hasher.verify(userSignupRequest.getToken(), token)) {
            userSignupRequest.setConfirmed(true);

            this.persister.save(userSignupRequest);

            return userSignupRequest;
        }

        userSignupRequest.setConfirmed(false);

        return userSignupRequest;
    }
}