package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Channel;

public class ChannelPersister extends BasePersister<Channel> {
    public ChannelPersister(Database database) {
        super(database);
    }

    public Channel getChannelById(long id) {
        return this.getDatabase().find(Channel.class).where().eq("id", id).findOne();
    }
}