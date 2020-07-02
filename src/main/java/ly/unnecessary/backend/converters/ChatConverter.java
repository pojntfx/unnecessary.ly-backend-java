package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.CommunityOuterClass.NewChat;
import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Chat;

public class ChatConverter {
    public ly.unnecessary.backend.api.CommunityOuterClass.Chat toExternal(Chat internalChat) {
        return ly.unnecessary.backend.api.CommunityOuterClass.Chat.newBuilder().setId(internalChat.getId())
                .setChannelId(internalChat.getChannel().getId()).setMessage(internalChat.getMessage())
                .setUserId(internalChat.getUser().getId()).build();
    }

    public Chat fromNewChatToInternal(NewChat newChat) {
        var chat = new Chat();

        var channel = new Channel();
        channel.setId(newChat.getChannelId());

        chat.setChannel(channel);
        chat.setMessage(newChat.getMessage());

        return chat;
    }
}