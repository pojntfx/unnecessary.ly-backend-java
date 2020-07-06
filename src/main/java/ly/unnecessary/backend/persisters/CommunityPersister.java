package ly.unnecessary.backend.persisters;

import java.util.List;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.User;

/**
 * Community persister
 */
public class CommunityPersister extends BasePersister<Community> {
    public CommunityPersister(Database database) {
        super(database);
    }

    /**
     * Get a community by it's id
     * 
     * @param id
     * @return Community
     */
    public Community getCommunityById(long id) {
        return this.getDatabase().find(Community.class).where().eq("id", id).findOne();
    }

    /**
     * Get the owner of a community
     * 
     * @param id
     * @param ownerId
     * @return User
     */
    public User getOwnerOfCommunity(long id, long ownerId) {
        var owner = this.getDatabase().find(Community.class).where().eq("id", id).findOne().getOwner();

        if (owner.getId() != ownerId) {
            return null;
        }

        return owner;
    }

    /**
     * Get the members of a community
     * 
     * @param id
     * @return List<User>
     */
    public List<User> getMembersOfCommunity(long id) {
        return this.getDatabase().find(Community.class).where().eq("id", id).findOne().getMembers();
    }
}