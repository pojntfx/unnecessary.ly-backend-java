package ly.unnecessary.backend.core;

import java.util.List;

import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Chat;
import ly.unnecessary.backend.persisters.ChannelPersister;

/**
 * Channel business logic
 */
public class ChannelCore {
    private ChannelPersister persister;

    public ChannelCore(ChannelPersister persister) {
        this.persister = persister;
    }

    /**
     * Create channel
     * 
     * @param channel
     * @return Channel
     */
    public Channel createChannel(Channel channel) {
        return this.persister.save(channel);
    }

    /**
     * Get channel by id
     * 
     * @param id
     * @return Channel
     */
    public Channel getChannelById(long id) {
        return this.persister.getChannelById(id);
    }

    /**
     * Get channel's chats
     * 
     * @param channel
     * @return List<Chat>
     */
    public List<Chat> getChatsOfChannel(Channel channel) {
        var channelFromPersistence = this.persister.getChannelById(channel.getId());

        return channelFromPersistence.getChats();
    }
}