package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.Invitation;
import ly.unnecessary.backend.persisters.InvitationPersister;
import ly.unnecessary.backend.utilities.Hasher;
import ly.unnecessary.backend.utilities.TokenGenerator;

public class InvitationCore {
    private InvitationPersister persister;
    private Hasher hasher;
    private TokenGenerator tokenGenerator;

    public InvitationCore(InvitationPersister persister, Hasher hasher, TokenGenerator tokenGenerator) {
        this.persister = persister;
        this.hasher = hasher;
        this.tokenGenerator = tokenGenerator;
    }

    public Invitation createInvitation(Invitation invitation) {
        var token = this.tokenGenerator.generateToken();

        invitation.setToken(this.hasher.hash(token)); // Save the hashed token

        this.persister.saveInvitation(invitation);

        invitation.setToken(token); // Return the unhashed token

        return invitation;
    }

    public Invitation acceptInvitation(Invitation invitation, Community community) {
        var invitationFromPersistence = this.persister.getInvitationByIdAndCommunityId(invitation.getId(),
                community.getId());

        if (invitationFromPersistence.isAccepted()) {
            throw new Error("This invitation has already been used");
        }

        if (!this.hasher.verify(invitationFromPersistence.getToken(), invitation.getToken())) {
            throw new Error("Invalid invitation token for this community");
        }

        invitationFromPersistence.setAccepted(true);

        this.persister.saveInvitation(invitationFromPersistence);

        return invitationFromPersistence;
    }
}