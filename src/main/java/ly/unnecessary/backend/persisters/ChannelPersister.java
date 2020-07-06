package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Channel;

/**
 * Channel persister
 */
public class ChannelPersister extends BasePersister<Channel> {
    public ChannelPersister(Database database) {
        super(database);
    }

    /**
     * Get a channel by it's id
     * 
     * @param id
     * @return Channel
     */
    public Channel getChannelById(long id) {
        return this.getDatabase().find(Channel.class).where().eq("id", id).findOne();
    }
}