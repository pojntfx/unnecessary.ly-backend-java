package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.persisters.CommunityPersister;

public class CommunityCore {
    private CommunityPersister persister;
    private UserCore userCore;

    public CommunityCore(CommunityPersister persister, UserCore userCore) {
        this.persister = persister;
        this.userCore = userCore;
    }

    public Community createCommunity(Community community, User user) {
        var userFromPersistence = this.userCore.signIn(user);

        community.setOwner(userFromPersistence);

        this.persister.saveCommunity(community);

        return community;
    }
}