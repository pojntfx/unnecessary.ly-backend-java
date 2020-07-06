package ly.unnecessary.backend.core;

import java.util.List;

import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Chat;
import ly.unnecessary.backend.persisters.ChannelPersister;

public class ChannelCore {
    private ChannelPersister persister;

    public ChannelCore(ChannelPersister persister) {
        this.persister = persister;
    }

    public Channel createChannel(Channel channel) {
        return this.persister.save(channel);
    }

    public Channel getChannelById(long id) {
        return this.persister.getChannelById(id);
    }

    public List<Chat> getChatsOfChannel(Channel channel) {
        var channelFromPersistence = this.persister.getChannelById(channel.getId());

        return channelFromPersistence.getChats();
    }
}