package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.api.UserOuterClass.UserSignUpRequest;
import ly.unnecessary.backend.entities.User;

public class UserConverter {
    public ly.unnecessary.backend.api.UserOuterClass.User toExternal(User internalUser) {
        return ly.unnecessary.backend.api.UserOuterClass.User.newBuilder().setId(internalUser.getId())
                .setDisplayName(internalUser.getDisplayName()).setEmail(internalUser.getEmail()).build();
    }

    public User fromSignUpRequestToInternal(UserSignUpRequest signUpRequest) {
        var user = new User();

        user.setDisplayName(signUpRequest.getDisplayName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());

        return user;
    }

    public User fromSignInRequestToInternal(UserSignInRequest signInRequest) {
        var user = new User();

        user.setEmail(signInRequest.getEmail());
        user.setPassword(signInRequest.getPassword());

        return user;
    }
}