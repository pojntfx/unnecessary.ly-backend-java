package ly.unnecessary.backend.services;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.CommunityOuterClass.Community;
import ly.unnecessary.backend.api.CommunityOuterClass.Invitation;
import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.api.CommunityOuterClass.NewCommunity;
import ly.unnecessary.backend.api.CommunityServiceGrpc.CommunityServiceImplBase;
import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.converters.CommunityConverter;
import ly.unnecessary.backend.converters.InvitationConverter;
import ly.unnecessary.backend.converters.UserConverter;
import ly.unnecessary.backend.core.CommunityCore;
import ly.unnecessary.backend.interceptors.UserInterceptor;

public class CommunityService extends CommunityServiceImplBase {
    private CommunityCore core;
    private CommunityConverter converter;
    private UserConverter userConverter;
    private InvitationConverter invitationConverter;

    public CommunityService(CommunityCore core, CommunityConverter converter, UserConverter userConverter,
            InvitationConverter invitationConverter) {
        this.core = core;
        this.converter = converter;
        this.userConverter = userConverter;
        this.invitationConverter = invitationConverter;
    }

    @Override
    public void createCommunity(NewCommunity request, StreamObserver<Community> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalCommunity = this.converter.fromNewCommunityToInternal(request);

        var updatedCommunity = this.core.createCommunity(internalCommunity, internalUser);

        var externalCommunity = this.converter.toExternal(updatedCommunity);

        responseObserver.onNext(externalCommunity);

        responseObserver.onCompleted();
    }

    @Override
    public void createInvitation(InvitationCreateRequest request, StreamObserver<Invitation> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalCommunity = this.converter.fromInvitationCreateRequestToInternal(request);
        var internalInvitation = this.invitationConverter.fromInvitationCreateRequestToInternal(request);

        var updatedInvitation = this.core.createInvitationForCommunity(internalCommunity, internalInvitation,
                internalUser);

        var externalInvitation = this.invitationConverter.toExternal(updatedInvitation);

        responseObserver.onNext(externalInvitation.toBuilder().setCommunityId(internalCommunity.getId()).build());

        responseObserver.onCompleted();
    }
}