package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.entities.UserPasswordResetRequest;

public class UserPasswordResetRequestConverter {
    public UserPasswordResetRequest fromUserSignUpConfirmationToInternal(
            ly.unnecessary.backend.api.UserOuterClass.UserPasswordResetConfirmation passwordResetConfirmation) {
        var userSignUpRequest = new UserPasswordResetRequest();

        userSignUpRequest.setToken(passwordResetConfirmation.getToken());

        return userSignUpRequest;
    }
}