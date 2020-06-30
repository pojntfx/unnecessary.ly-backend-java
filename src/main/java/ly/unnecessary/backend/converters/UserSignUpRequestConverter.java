package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.entities.UserSignUpRequest;

public class UserSignUpRequestConverter {
    public UserSignUpRequest fromUserSignUpConfirmationToInternal(
            ly.unnecessary.backend.api.UserOuterClass.UserSignUpConfirmation signUpConfirmation) {
        var userSignUpRequest = new UserSignUpRequest();

        userSignUpRequest.setToken(signUpConfirmation.getToken());

        return userSignUpRequest;
    }
}