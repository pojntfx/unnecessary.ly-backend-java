package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Invitation;

public class InvitationPersister extends BasePersister<Invitation> {
    public InvitationPersister(Database database) {
        super(database);
    }

    public Invitation getInvitationByIdAndCommunityId(long id, long communityId) {
        return this.getDatabase().find(Invitation.class).where().eq("id", id).and().eq("community_id", communityId)
                .findOne();
    }
}