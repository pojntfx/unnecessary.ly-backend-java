package ly.unnecessary.backend.services;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.UserOuterClass.User;
import ly.unnecessary.backend.api.UserOuterClass.UserPasswordResetConfirmation;
import ly.unnecessary.backend.api.UserOuterClass.UserPasswordResetRequest;
import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.api.UserOuterClass.UserSignUpConfirmation;
import ly.unnecessary.backend.api.UserOuterClass.UserSignUpRequest;
import ly.unnecessary.backend.api.UserServiceGrpc.UserServiceImplBase;
import ly.unnecessary.backend.converters.UserConverter;
import ly.unnecessary.backend.converters.UserPasswordResetRequestConverter;
import ly.unnecessary.backend.converters.UserSignUpRequestConverter;
import ly.unnecessary.backend.core.UserCore;

public class UserService extends UserServiceImplBase {
    private UserCore core;
    private UserConverter converter;
    private UserSignUpRequestConverter userSignUpRequestConverter;
    private UserPasswordResetRequestConverter userPasswordResetRequestConverter;

    public UserService(UserCore core, UserConverter converter, UserSignUpRequestConverter userSignUpRequestConverter,
            UserPasswordResetRequestConverter userPasswordResetRequestConverter) {
        this.core = core;
        this.converter = converter;
        this.userSignUpRequestConverter = userSignUpRequestConverter;
        this.userPasswordResetRequestConverter = userPasswordResetRequestConverter;
    }

    @Override
    public void signIn(UserSignInRequest request, StreamObserver<User> responseObserver) {
        var internalUser = this.converter.fromSignInRequestToInternal(request);

        var updatedUser = this.core.signIn(internalUser);

        var externalUser = this.converter.toExternal(updatedUser);

        responseObserver.onNext(externalUser);

        responseObserver.onCompleted();
    }

    @Override
    public void requestSignUp(UserSignUpRequest request, StreamObserver<User> responseObserver) {
        var internalUser = this.converter.fromSignUpRequestToInternal(request);

        var updatedUser = this.core.requestSignUp(internalUser);

        var externalUser = this.converter.toExternal(updatedUser);

        responseObserver.onNext(externalUser);

        responseObserver.onCompleted();
    }

    @Override
    public void confirmSignUp(UserSignUpConfirmation request, StreamObserver<User> responseObserver) {
        var internalUser = this.converter.fromUserSignUpConfirmationToInternal(request);
        var internalSignUpConfirmation = this.userSignUpRequestConverter.fromUserSignUpConfirmationToInternal(request);

        var updatedUser = this.core.confirmSignUp(internalUser, internalSignUpConfirmation);

        var externalUser = this.converter.toExternal(updatedUser);

        responseObserver.onNext(externalUser);

        responseObserver.onCompleted();
    }

    @Override
    public void requestPasswordReset(UserPasswordResetRequest request, StreamObserver<User> responseObserver) {
        var internalUser = this.converter.fromPasswordResetRequestToInternal(request);

        var updatedUser = this.core.requestPasswordReset(internalUser);

        var externalUser = this.converter.toExternal(updatedUser);

        responseObserver.onNext(externalUser);

        responseObserver.onCompleted();
    }

    @Override
    public void confirmPasswordReset(UserPasswordResetConfirmation request, StreamObserver<User> responseObserver) {
        var internalUser = this.converter.fromUserPasswordResetConfirmationToInternal(request);
        var internalPasswordResetConfirmation = this.userPasswordResetRequestConverter
                .fromUserSignUpConfirmationToInternal(request);

        var updatedUser = this.core.confirmPasswordReset(internalUser, internalPasswordResetConfirmation);

        var externalUser = this.converter.toExternal(updatedUser);

        responseObserver.onNext(externalUser);

        responseObserver.onCompleted();
    }
}