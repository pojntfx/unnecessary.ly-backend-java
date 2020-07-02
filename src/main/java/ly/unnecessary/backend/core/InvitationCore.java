package ly.unnecessary.backend.core;

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

        invitation.setToken(this.hasher.hash(token));

        this.persister.saveInvitation(invitation);

        return invitation;
    }
}