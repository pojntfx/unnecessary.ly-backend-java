package ly.unnecessary.backend.services;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.CommunityOuterClass.Channel;
import ly.unnecessary.backend.api.CommunityOuterClass.Community;
import ly.unnecessary.backend.api.CommunityOuterClass.Invitation;
import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.api.CommunityOuterClass.NewCommunity;
import ly.unnecessary.backend.api.CommunityServiceGrpc.CommunityServiceImplBase;
import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.converters.ChannelConverter;
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
    private CommunityConverter communityConverter;
    private ChannelConverter channelConverter;

    public CommunityService(CommunityCore core, CommunityConverter converter, UserConverter userConverter,
            InvitationConverter invitationConverter, CommunityConverter communityConverter,
            ChannelConverter channelConverter) {
        this.core = core;
        this.converter = converter;
        this.userConverter = userConverter;
        this.invitationConverter = invitationConverter;
        this.communityConverter = communityConverter;
        this.channelConverter = channelConverter;
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

        responseObserver.onNext(externalInvitation);

        responseObserver.onCompleted();
    }

    @Override
    public void acceptInvitation(Invitation request, StreamObserver<Community> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalCommunity = this.communityConverter.fromInvitationToInternal(request);
        var internalInvitation = this.invitationConverter.toInternal(request);

        var updatedCommunity = this.core.acceptInvitationForCommunity(internalCommunity, internalInvitation,
                internalUser);

        var externalCommunity = this.converter.toExternal(updatedCommunity);

        responseObserver.onNext(externalCommunity);

        responseObserver.onCompleted();
    }

    @Override
    public void createChannel(NewChannel request, StreamObserver<Channel> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalCommunity = this.communityConverter.fromNewChannelToInternal(request);
        var internalChannel = this.channelConverter.fromNewChannelToInternal(request);

        var updatedChannel = this.core.createChannel(internalCommunity, internalChannel, internalUser);

        var externalChannel = this.channelConverter.toExternal(updatedChannel);

        responseObserver.onNext(externalChannel);

        responseObserver.onCompleted();
    }
}