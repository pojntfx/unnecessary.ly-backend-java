package ly.unnecessary.backend.core;

import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.persisters.ChannelPersister;

public class ChannelCore {
    private ChannelPersister persister;

    public ChannelCore(ChannelPersister persister) {
        this.persister = persister;
    }

    public Channel createChannel(Channel channel) {
        return this.persister.saveChannel(channel);
    }
}