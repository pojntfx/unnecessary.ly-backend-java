package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.CommunityOuterClass.InvitationCreateRequest;
import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.Invitation;

public class InvitationConverter {
    public ly.unnecessary.backend.api.CommunityOuterClass.Invitation toExternal(Invitation internalInvitation) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Invitation.newBuilder().setId(internalInvitation.getId())
                .setCommunityId(internalInvitation.getCommunity().getId()).setToken(internalInvitation.getToken())
                .build();
    }

    public Invitation fromInvitationCreateRequestToInternal(InvitationCreateRequest invitationCreateRequest) {
        var invitation = new Invitation();
        var community = new Community();

        community.setId(invitationCreateRequest.getCommunityId());

        invitation.setCommunity(community);

        return invitation;
    }

    public Invitation toInternal(ly.unnecessary.backend.api.CommunityOuterClass.Invitation externalInvitation) {
        var invitation = new Invitation();
        var community = new Community();
        community.setId(externalInvitation.getId());

        invitation.setId(externalInvitation.getId());
        invitation.setCommunity(community);
        invitation.setToken(externalInvitation.getToken());

        return invitation;
    }
}