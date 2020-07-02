package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.CommunityOuterClass.NewChannel;
import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Community;

public class ChannelConverter {
    public ly.unnecessary.backend.api.CommunityOuterClass.Channel toExternal(Channel internalChannel) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Channel.newBuilder().setId(internalChannel.getId())
                .setCommunityId(internalChannel.getCommunity().getId()).setDisplayName(internalChannel.getDisplayName())
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
}