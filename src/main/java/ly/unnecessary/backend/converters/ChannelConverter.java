package ly.unnecessary.backend.converters;

import java.util.List;
import java.util.stream.Collectors;

import ly.unnecessary.backend.api.CommunityOuterClass.ChannelFilter;
import ly.unnecessary.backend.api.CommunityOuterClass.Channels;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChat;
import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Community;

/**
 * Channel converter
 */
public class ChannelConverter {
    private ChatConverter chatConverter;

    public ChannelConverter(ChatConverter chatConverter) {
        this.chatConverter = chatConverter;
    }

    /**
     * Convert to external
     * 
     * @param internalChannel
     * @return Channel
     */
    public ly.unnecessary.backend.api.CommunityOuterClass.Channel toExternal(Channel internalChannel) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Channel.newBuilder().setId(internalChannel.getId())
                .setCommunityId(internalChannel.getCommunity().getId()).setDisplayName(internalChannel.getDisplayName())
                .addAllChats(internalChannel.getChats().stream().map(c -> this.chatConverter.toExternal(c))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Convert from new channel to internal
     * 
     * @param newChannel
     * @return Channel
     */
    public Channel fromNewChannelToInternal(NewChannel newChannel) {
        var channel = new Channel();

        var community = new Community();
        community.setId(newChannel.getCommunityId());

        channel.setCommunity(community);
        channel.setDisplayName(newChannel.getDisplayName());

        return channel;
    }

    /**
     * Convert from new chat to internal
     * 
     * @param newChat
     * @return Channel
     */
    public Channel fromNewChatToInternal(NewChat newChat) {
        var channel = new Channel();

        channel.setId(newChat.getChannelId());

        return channel;
    }

    /**
     * Convert from channel filter to internal
     * 
     * @param channelFilter
     * @return Channel
     */
    public Channel fromChannelFilter(ChannelFilter channelFilter) {
        var channel = new Channel();

        channel.setId(channelFilter.getChannelId());

        return channel;
    }

    /**
     * Convert from many internal to external
     * 
     * @param internalChannels
     * @return Channels
     */
    public Channels fromManyToExternal(List<Channel> internalChannels) {
        return Channels.newBuilder()
                .addAllChannels(internalChannels.stream().map(c -> this.toExternal(c)).collect(Collectors.toList()))
                .build();
    }
}