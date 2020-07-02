package ly.unnecessary.backend.converters;

import java.util.stream.Collectors;

import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.entities.Community;

public class CommunityConverter {
    private UserConverter userConverter;

    public CommunityConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public ly.unnecessary.backend.api.CommunityOuterClass.Community toExternal(Community internalCommunity) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Community.newBuilder().setId(internalCommunity.getId())
                .setDisplayName(internalCommunity.getDisplayName())
                .setOwner(this.userConverter.toExternal(internalCommunity.getOwner())).addAllMembers(internalCommunity
                        .getMembers().stream().map(u -> this.userConverter.toExternal(u)).collect(Collectors.toList()))
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
}