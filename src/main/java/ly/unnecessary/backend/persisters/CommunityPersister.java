package ly.unnecessary.backend.persisters;

import java.util.List;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.User;

public class CommunityPersister extends BasePersister<Community> {
    public CommunityPersister(Database database) {
        super(database);
    }

    public Community getCommunityById(long id) {
        return this.getDatabase().find(Community.class).where().eq("id", id).findOne();
    }

    public User getOwnerOfCommunity(long id, long ownerId) {
        var owner = this.getDatabase().find(Community.class).where().eq("id", id).findOne().getOwner();

        if (owner.getId() != ownerId) {
            return null;
        }

        return owner;
    }

    public List<User> getMembersOfCommunity(long id) {
        return this.getDatabase().find(Community.class).where().eq("id", id).findOne().getMembers();
    }
}