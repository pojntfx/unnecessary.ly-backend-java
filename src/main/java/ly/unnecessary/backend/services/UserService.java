package ly.unnecessary.backend.services;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.UserOuterClass.User;
import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.api.UserOuterClass.UserSignUpRequest;
import ly.unnecessary.backend.api.UserServiceGrpc.UserServiceImplBase;
import ly.unnecessary.backend.converters.UserConverter;
import ly.unnecessary.backend.core.UserCore;

public class UserService extends UserServiceImplBase {
    private UserCore core;
    private UserConverter converter;

    public UserService(UserCore core, UserConverter converter) {
        this.core = core;
        this.converter = converter;
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
    public void signUp(UserSignUpRequest request, StreamObserver<User> responseObserver) {
        var internalUser = this.converter.fromSignUpRequestToInternal(request);

        var updatedUser = this.core.signUp(internalUser);

        var externalUser = this.converter.toExternal(updatedUser);

        responseObserver.onNext(externalUser);

        responseObserver.onCompleted();
    }
}