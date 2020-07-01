package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Community;

public class CommunityPersister {
    private Database database;

    public CommunityPersister(Database database) {
        this.database = database;
    }

    public Community saveCommunity(Community community) {
        this.database.save(community);

        return community;
    }
}