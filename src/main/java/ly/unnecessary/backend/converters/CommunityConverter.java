package ly.unnecessary.backend.converters;

import java.util.List;
import java.util.stream.Collectors;

import ly.unnecessary.backend.api.CommunityOuterClass.Communities;
import ly.unnecessary.backend.api.CommunityOuterClass.CommunityFilter;
import ly.unnecessary.backend.api.CommunityOuterClass.Invitation;
import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.entities.Community;

/**
 * Community converter
 */
public class CommunityConverter {
    private UserConverter userConverter;
    private ChannelConverter channelConverter;

    public CommunityConverter(UserConverter userConverter, ChannelConverter channelConverter) {
        this.userConverter = userConverter;
        this.channelConverter = channelConverter;
    }

    /**
     * Convert to external
     * 
     * @param internalCommunity
     * @return Community
     */
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

    /**
     * Convert from new community to internal
     * 
     * @param newCommunity
     * @return Community
     */
    public Community fromNewCommunityToInternal(
            ly.unnecessary.backend.api.CommunityOuterClass.NewCommunity newCommunity) {
        var community = new Community();

        community.setDisplayName(newCommunity.getDisplayName());

        return community;
    }

    /**
     * Convert from invitation create request to internal
     * 
     * @param invitationCreateRequest
     * @return Community
     */
    public Community fromInvitationCreateRequestToInternal(InvitationCreateRequest invitationCreateRequest) {
        var community = new Community();

        community.setId(invitationCreateRequest.getCommunityId());

        return community;
    }

    /**
     * Create from invitation to internal
     * 
     * @param invitation
     * @return Community
     */
    public Community fromInvitationToInternal(Invitation invitation) {
        var community = new Community();

        community.setId(invitation.getCommunityId());

        return community;
    }

    /**
     * Create from new channel to internal
     * 
     * @param newChannel
     * @return Community
     */
    public Community fromNewChannelToInternal(NewChannel newChannel) {
        var community = new Community();

        community.setId(newChannel.getCommunityId());

        return community;
    }

    /**
     * Convert from many internal to external
     * 
     * @param internalCommunities
     * @return Communities
     */
    public Communities fromManyToExternal(List<Community> internalCommunities) {
        return Communities.newBuilder().addAllCommunities(
                internalCommunities.stream().map(c -> this.toExternal(c)).collect(Collectors.toList())).build();
    }

    /**
     * Convert from community filter to internal
     * 
     * @param communityFilter
     * @return Community
     */
    public Community fromCommunityFilter(CommunityFilter communityFilter) {
        var community = new Community();

        community.setId(communityFilter.getCommunityId());

        return community;
    }
}