package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.Invitation;
import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.persisters.CommunityPersister;

public class CommunityCore {
    private CommunityPersister persister;
    private UserCore userCore;
    private InvitationCore invitationCore;

    public CommunityCore(CommunityPersister persister, UserCore userCore, InvitationCore invitationCore) {
        this.persister = persister;
        this.userCore = userCore;
        this.invitationCore = invitationCore;
    }

    public Community createCommunity(Community community, User user) {
        var userFromPersistence = this.userCore.signIn(user);

        community.setOwner(userFromPersistence);

        this.persister.saveCommunity(community);

        return community;
    }

    public Invitation createInvitationForCommunity(Community community, Invitation invitation, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var communityFromPersistence = this.persister.getCommunityById(community.getId());

        // TODO: Check if the user is mod as well (would have the same permissions)
        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            throw new Error("User isn't the owner of this community");
        }

        var invitationFromPersistence = this.invitationCore.createInvitation(invitation);

        var invitations = community.getInvitations();
        invitations.add(invitationFromPersistence);
        community.setInvitations(invitations);

        this.persister.saveCommunity(communityFromPersistence);

        return invitation;
    }
}