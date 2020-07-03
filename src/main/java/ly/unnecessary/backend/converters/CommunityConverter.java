package ly.unnecessary.backend.converters;

import java.util.List;
import java.util.stream.Collectors;

import ly.unnecessary.backend.api.CommunityOuterClass.Communities;
import ly.unnecessary.backend.api.CommunityOuterClass.Invitation;
import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.entities.Community;

public class CommunityConverter {
    private UserConverter userConverter;
    private ChannelConverter channelConverter;

    public CommunityConverter(UserConverter userConverter, ChannelConverter channelConverter) {
        this.userConverter = userConverter;
        this.channelConverter = channelConverter;
    }

    public ly.unnecessary.backend.api.CommunityOuterClass.Community toExternal(Community internalCommunity) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Community.newBuilder().setId(internalCommunity.getId())
                .setDisplayName(internalCommunity.getDisplayName())
                .setOwner(this.userConverter.toExternal(internalCommunity.getOwner()))
                .addAllMembers(internalCommunity.getMembers().stream().map(u -> this.userConverter.toExternal(u))
                        .collect(Collectors.toList()))
                .addAllChannels(internalCommunity.getChannels().stream().map(c -> this.channelConverter.toExternal(c))
                        .collect(Collectors.toList()))
                .build();
    }

    public Community fromNewCommunityToInternal(
            ly.unnecessary.backend.api.CommunityOuterClass.NewCommunity newCommunity) {
        var community = new Community();

        community.setDisplayName(newCommunity.getDisplayName());

        return community;
    }

    public Community fromInvitationCreateRequestToInternal(InvitationCreateRequest invitationCreateRequest) {
        var community = new Community();

        community.setId(invitationCreateRequest.getCommunityId());

        return community;
    }

    public Community fromInvitationToInternal(Invitation invitation) {
        var community = new Community();

        community.setId(invitation.getCommunityId());

        return community;
    }

    public Community fromNewChannelToInternal(NewChannel newChannel) {
        var community = new Community();

        community.setId(newChannel.getCommunityId());

        return community;
    }

    public Communities fromManyToExternal(List<Community> internalCommunities) {
        return Communities.newBuilder().addAllCommunities(
                internalCommunities.stream().map(c -> this.toExternal(c)).collect(Collectors.toList())).build();
    }
}