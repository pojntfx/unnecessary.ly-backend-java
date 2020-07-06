package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.entities.UserPasswordResetRequest;

/**
 * Password reset request converter
 */
public class UserPasswordResetRequestConverter {
    /**
     * Convert from sign up confirmation to internal
     * 
     * @param passwordResetConfirmation
     * @return UserPasswordResetRequest
     */
    public UserPasswordResetRequest fromUserSignUpConfirmationToInternal(
            ly.unnecessary.backend.api.UserOuterClass.UserPasswordResetConfirmation passwordResetConfirmation) {
        var userSignUpRequest = new UserPasswordResetRequest();

        userSignUpRequest.setToken(passwordResetConfirmation.getToken());

        return userSignUpRequest;
    }
}