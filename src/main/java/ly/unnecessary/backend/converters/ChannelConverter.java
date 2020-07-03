package ly.unnecessary.backend.converters;

import java.util.stream.Collectors;

import ly.unnecessary.backend.api.CommunityOuterClass.ChannelFilter;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.api.CommunityOuterClass.NewChat;
import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Community;

public class ChannelConverter {
    private ChatConverter chatConverter;

    public ChannelConverter(ChatConverter chatConverter) {
        this.chatConverter = chatConverter;
    }

    public ly.unnecessary.backend.api.CommunityOuterClass.Channel toExternal(Channel internalChannel) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Channel.newBuilder().setId(internalChannel.getId())
                .setCommunityId(internalChannel.getCommunity().getId()).setDisplayName(internalChannel.getDisplayName())
                .addAllChats(internalChannel.getChats().stream().map(c -> this.chatConverter.toExternal(c))
                        .collect(Collectors.toList()))
                .build();
    }

    public Channel fromNewChannelToInternal(NewChannel newChannel) {
        var channel = new Channel();

        var community = new Community();
        community.setId(newChannel.getCommunityId());

        channel.setCommunity(community);
        channel.setDisplayName(newChannel.getDisplayName());

        return channel;
    }

    public Channel fromNewChatToInternal(NewChat newChat) {
        var channel = new Channel();

        channel.setId(newChat.getChannelId());

        return channel;
    }

    public Channel fromChannelFilter(ChannelFilter channelFilter) {
        var channel = new Channel();

        channel.setId(channelFilter.getChannelId());

        return channel;
    }
}