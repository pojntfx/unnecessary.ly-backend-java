package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.entities.UserSignUpRequest;

/**
 * Sign up request converter
 */
public class UserSignUpRequestConverter {
    /**
     * Convert from sign up confirmation to internal
     * 
     * @param signUpConfirmation
     * @return UserSignUpRequest
     */
    public UserSignUpRequest fromUserSignUpConfirmationToInternal(
            ly.unnecessary.backend.api.UserOuterClass.UserSignUpConfirmation signUpConfirmation) {
        var userSignUpRequest = new UserSignUpRequest();

        userSignUpRequest.setToken(signUpConfirmation.getToken());

        return userSignUpRequest;
    }
}