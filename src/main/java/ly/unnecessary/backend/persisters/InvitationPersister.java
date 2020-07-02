package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Invitation;

public class InvitationPersister {
    private Database database;

    public InvitationPersister(Database database) {
        this.database = database;
    }

    public Invitation saveInvitation(Invitation invitation) {
        this.database.save(invitation);

        return invitation;
    }
}