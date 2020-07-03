package ly.unnecessary.backend.services;

import com.google.protobuf.Empty;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.CommunityOuterClass.Channel;
import ly.unnecessary.backend.api.CommunityOuterClass.ChannelFilter;
import ly.unnecessary.backend.api.CommunityOuterClass.Chat;
import ly.unnecessary.backend.api.CommunityOuterClass.Communities;
import ly.unnecessary.backend.api.CommunityOuterClass.Community;
import ly.unnecessary.backend.api.CommunityOuterClass.Invitation;
import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChat;
import ly.unnecessary.backend.api.CommunityOuterClass.NewCommunity;
import ly.unnecessary.backend.api.CommunityServiceGrpc.CommunityServiceImplBase;
import ly.unnecessary.backend.api.UserOuterClass.UserSignInRequest;
import ly.unnecessary.backend.converters.ChannelConverter;
import ly.unnecessary.backend.converters.ChatConverter;
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
    private ChatConverter chatConverter;

    public CommunityService(CommunityCore core, CommunityConverter converter, UserConverter userConverter,
            InvitationConverter invitationConverter, CommunityConverter communityConverter,
            ChannelConverter channelConverter, ChatConverter chatConverter) {
        this.core = core;
        this.converter = converter;
        this.userConverter = userConverter;
        this.invitationConverter = invitationConverter;
        this.communityConverter = communityConverter;
        this.channelConverter = channelConverter;
        this.chatConverter = chatConverter;
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

    @Override
    public void createChat(NewChat request, StreamObserver<Chat> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalChannel = this.channelConverter.fromNewChatToInternal(request);
        var internalChat = this.chatConverter.fromNewChatToInternal(request);

        var updatedChat = this.core.createChat(internalChannel, internalChat, internalUser);

        var externalChannel = this.chatConverter.toExternal(updatedChat);

        responseObserver.onNext(externalChannel);

        responseObserver.onCompleted();
    }

    @Override
    public void subscribeToChannelChats(ChannelFilter request, StreamObserver<Chat> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalChannel = this.channelConverter.fromChannelFilter(request);

        this.core.subscribeToChannelChats(internalChannel, (c) -> {
            var externalChat = this.chatConverter.toExternal(c);

            responseObserver.onNext(externalChat);

            return 0;
        }, internalUser);
    }

    @Override
    public void listCommunitiesForOwner(Empty request, StreamObserver<Communities> responseObserver) {
        var email = UserInterceptor.USER_EMAIL.get();
        var password = UserInterceptor.USER_PASSWORD.get();

        var externalUser = UserSignInRequest.newBuilder().setEmail(email).setPassword(password).build();

        var internalUser = this.userConverter.fromSignInRequestToInternal(externalUser);

        var internalCommunities = this.core.listCommunitiesForOwner(internalUser);

        var externalCommunities = this.communityConverter.fromManyToExternal(internalCommunities);

        responseObserver.onNext(externalCommunities);

        responseObserver.onCompleted();
    }
}