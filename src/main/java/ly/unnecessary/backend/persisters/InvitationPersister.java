package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Invitation;

/**
 * Invitation persister
 */
public class InvitationPersister extends BasePersister<Invitation> {
    public InvitationPersister(Database database) {
        super(database);
    }

    /**
     * Get invitation by id and it's community's id
     * 
     * @param id
     * @param communityId
     * @return Invitation
     */
    public Invitation getInvitationByIdAndCommunityId(long id, long communityId) {
        return this.getDatabase().find(Invitation.class).where().eq("id", id).and().eq("community_id", communityId)
                .findOne();
    }
}