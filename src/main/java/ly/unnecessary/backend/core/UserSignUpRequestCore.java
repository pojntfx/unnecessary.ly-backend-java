package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.UserSignUpRequest;
import ly.unnecessary.backend.persisters.UserSignUpRequestPersister;
import ly.unnecessary.backend.utilities.UserEmailer;

public class UserSignUpRequestCore {
    private UserSignUpRequestPersister persister;
    private UserEmailer emailer;

    public UserSignUpRequestCore(UserSignUpRequestPersister persister, UserEmailer emailer) {
        this.persister = persister;
        this.emailer = emailer;
    }

    public UserSignUpRequest createRequest(UserSignUpRequest userSignupRequest, String email) {
        var token = this.emailer.requestSignUpConfirmation(email);

        userSignupRequest.setToken(token);

        this.persister.saveUserSignUpRequest(userSignupRequest);

        return userSignupRequest;
    }

    public UserSignUpRequest validateSignUpRequest(UserSignUpRequest userSignupRequest, String token) {
        if (token.equals(userSignupRequest.getToken())) {
            userSignupRequest.setConfirmed(true);

            this.persister.saveUserSignUpRequest(userSignupRequest);

            return userSignupRequest;
        }

        userSignupRequest.setConfirmed(false);

        return userSignupRequest;
    }
}