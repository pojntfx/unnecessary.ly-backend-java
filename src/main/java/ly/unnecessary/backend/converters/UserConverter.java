package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.UserOuterClass.UserPasswordResetRequest;
import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.api.UserOuterClass.UserSignUpRequest;
import ly.unnecessary.backend.entities.User;

/**
 * User converter
 */
public class UserConverter {

    /**
     * Convert to internal
     * 
     * @param internalUser
     * @return User
     */
    public ly.unnecessary.backend.api.UserOuterClass.User toExternal(User internalUser) {
        return ly.unnecessary.backend.api.UserOuterClass.User.newBuilder().setId(internalUser.getId())
                .setDisplayName(internalUser.getDisplayName()).setEmail(internalUser.getEmail()).build();
    }

    /**
     * Convert from sign up request to internal
     * 
     * @param signUpRequest
     * @return User
     */
    public User fromSignUpRequestToInternal(UserSignUpRequest signUpRequest) {
        var user = new User();

        user.setDisplayName(signUpRequest.getDisplayName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());

        return user;
    }

    /**
     * Convert from sign in request to internal
     * 
     * @param signInRequest
     * @return User
     */
    public User fromSignInRequestToInternal(UserSignInRequest signInRequest) {
        var user = new User();

        user.setEmail(signInRequest.getEmail());
        user.setPassword(signInRequest.getPassword());

        return user;
    }

    /**
     * Convert from sign up confirmation to internal
     * 
     * @param signUpConfirmation
     * @return User
     */
    public User fromUserSignUpConfirmationToInternal(
            ly.unnecessary.backend.api.UserOuterClass.UserSignUpConfirmation signUpConfirmation) {
        var user = new User();

        user.setEmail(signUpConfirmation.getEmail());

        return user;
    }

    /**
     * Convert from password reset confirmation to internal
     * 
     * @param passwordResetConfirmation
     * @return User
     */
    public User fromUserPasswordResetConfirmationToInternal(
            ly.unnecessary.backend.api.UserOuterClass.UserPasswordResetConfirmation passwordResetConfirmation) {
        var user = new User();

        user.setEmail(passwordResetConfirmation.getEmail());
        user.setPassword(passwordResetConfirmation.getNewPassword());

        return user;
    }

    /**
     * Convert from password reset request to internal
     * 
     * @param passwordResetRequest
     * @return User
     */
    public User fromPasswordResetRequestToInternal(UserPasswordResetRequest passwordResetRequest) {
        var user = new User();

        user.setEmail(passwordResetRequest.getEmail());

        return user;
    }
}