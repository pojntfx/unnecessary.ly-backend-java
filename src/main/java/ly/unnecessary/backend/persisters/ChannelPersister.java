package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Channel;

public class ChannelPersister {
    private Database database;

    public ChannelPersister(Database database) {
        this.database = database;
    }

    public Channel saveChannel(Channel channel) {
        this.database.save(channel);

        return channel;
    }
}