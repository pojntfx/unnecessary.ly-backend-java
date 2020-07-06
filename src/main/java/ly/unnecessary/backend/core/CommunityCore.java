package ly.unnecessary.backend.core;

import java.util.List;
import java.util.function.Function;

import ly.unnecessary.backend.entities.Channel;
import ly.unnecessary.backend.entities.Chat;
import ly.unnecessary.backend.entities.Community;
import ly.unnecessary.backend.entities.Invitation;
import ly.unnecessary.backend.entities.User;
import ly.unnecessary.backend.persisters.CommunityPersister;

/**
 * Community business logic
 */
public class CommunityCore {
    private CommunityPersister persister;
    private UserCore userCore;
    private InvitationCore invitationCore;
    private ChannelCore channelCore;
    private ChatCore chatCore;

    public CommunityCore(CommunityPersister persister, UserCore userCore, InvitationCore invitationCore,
            ChannelCore channelCore, ChatCore chatCore) {
        this.persister = persister;
        this.userCore = userCore;
        this.invitationCore = invitationCore;
        this.channelCore = channelCore;
        this.chatCore = chatCore;
    }

    /**
     * Create community
     * 
     * @param community
     * @param user
     * @return Community
     */
    public Community createCommunity(Community community, User user) {
        var userFromPersistence = this.userCore.signIn(user);

        community.setOwner(userFromPersistence);

        this.persister.save(community);

        return community;
    }

    /**
     * Create invitation to community
     * 
     * @param community
     * @param invitation
     * @param user
     * @return Invitation
     */
    public Invitation createInvitationForCommunity(Community community, Invitation invitation, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var communityFromPersistence = this.persister.getCommunityById(community.getId());

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            throw new Error("User isn't the owner of this community");
        }

        var invitationFromPersistence = this.invitationCore.createInvitation(invitation);

        var invitations = community.getInvitations();
        invitations.add(invitationFromPersistence);
        community.setInvitations(invitations);

        this.persister.save(communityFromPersistence);

        return invitation;
    }

    /**
     * Accept invitation for community
     * 
     * @param community
     * @param invitation
     * @param user
     * @return Community
     */
    public Community acceptInvitationForCommunity(Community community, Invitation invitation, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var communityFromPersistence = this.persister.getCommunityById(community.getId());

        this.invitationCore.acceptInvitation(invitation, communityFromPersistence);

        var members = communityFromPersistence.getMembers();
        members.add(userFromPersistence);
        communityFromPersistence.setMembers(members);

        communityFromPersistence.setMembers(members);

        this.persister.save(communityFromPersistence);

        return communityFromPersistence;
    }

    /**
     * Create channel in community
     * 
     * @param community
     * @param channel
     * @param user
     * @return Channel
     */
    public Channel createChannel(Community community, Channel channel, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var communityFromPersistence = this.persister.getCommunityById(community.getId());

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            throw new Error("User isn't the owner of this community");
        }

        channel.setCommunity(communityFromPersistence);

        this.channelCore.createChannel(channel);

        return channel;
    }

    /**
     * Create chat in channel
     * 
     * @param channel
     * @param chat
     * @param user
     * @return Chat
     */
    public Chat createChat(Channel channel, Chat chat, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var channelFromPersistence = this.channelCore.getChannelById(channel.getId());
        var communityFromPersistence = channelFromPersistence.getCommunity();

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            var communityMember = this.persister.getMembersOfCommunity(communityFromPersistence.getId()).stream()
                    .filter(c -> c.getId() == userFromPersistence.getId()).findAny();

            if (communityMember == null) {
                throw new Error("User isn't the owner or member of this community");
            }
        }

        chat.setChannel(channelFromPersistence);
        chat.setUser(userFromPersistence);

        this.chatCore.createChat(chat);

        return chat;
    }

    /**
     * Subscribe to chats in channel
     * 
     * @param channel
     * @param handler
     * @param user
     */
    public void subscribeToChannelChats(Channel channel, Function<Chat, Integer> handler, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var channelFromPersistence = this.channelCore.getChannelById(channel.getId());
        var communityFromPersistence = channelFromPersistence.getCommunity();

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            var communityMember = this.persister.getMembersOfCommunity(communityFromPersistence.getId()).stream()
                    .filter(c -> c.getId() == userFromPersistence.getId()).findAny();

            if (communityMember == null) {
                throw new Error("User isn't the owner or member of this community");
            }
        }

        this.chatCore.subscribeToChats((chat) -> {
            if (chat.getChannel().getId() == channelFromPersistence.getId()) {
                handler.apply(chat);
            }

            return 0;
        });
    }

    /**
     * Get communities for owner
     * 
     * @param user
     * @return List<Community>
     */
    public List<Community> listCommunitiesForOwner(User user) {
        var userFromPersistence = this.userCore.signIn(user);

        return this.userCore.listOwnedCommunities(userFromPersistence);
    }

    /**
     * Get communities for member
     * 
     * @param user
     * @return List<Community>
     */
    public List<Community> listCommunitiesForMember(User user) {
        var userFromPersistence = this.userCore.signIn(user);

        return this.userCore.listMemberCommunities(userFromPersistence);
    }

    /**
     * Get channels for community
     * 
     * @param community
     * @param user
     * @return List<Channel>
     */
    public List<Channel> listChannelsForCommunity(Community community, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var communityFromPersistence = this.persister.getCommunityById(community.getId());

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            var communityMember = this.persister.getMembersOfCommunity(communityFromPersistence.getId()).stream()
                    .filter(c -> c.getId() == userFromPersistence.getId()).findAny();

            if (communityMember == null) {
                throw new Error("User isn't the owner or member of this community");
            }
        }

        return communityFromPersistence.getChannels();
    }

    /**
     * Get chats for channel
     * 
     * @param channel
     * @param user
     * @return List<Chat>
     */
    public List<Chat> listChatsForChannel(Channel channel, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var channelFromPersistence = this.channelCore.getChannelById(channel.getId());
        var communityFromPersistence = channelFromPersistence.getCommunity();

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            var communityMember = this.persister.getMembersOfCommunity(communityFromPersistence.getId()).stream()
                    .filter(c -> c.getId() == userFromPersistence.getId()).findAny();

            if (communityMember == null) {
                throw new Error("User isn't the owner or member of this community");
            }
        }

        return channelFromPersistence.getChats();
    }

    /**
     * Get community
     * 
     * @param community
     * @param user
     * @return Community
     */
    public Community getCommunity(Community community, User user) {
        var userFromPersistence = this.userCore.signIn(user);
        var communityFromPersistence = this.persister.getCommunityById(community.getId());

        var communityOwner = this.persister.getOwnerOfCommunity(communityFromPersistence.getId(),
                userFromPersistence.getId());

        if (communityOwner == null) {
            var communityMember = this.persister.getMembersOfCommunity(communityFromPersistence.getId()).stream()
                    .filter(c -> c.getId() == userFromPersistence.getId()).findAny();

            if (communityMember == null) {
                throw new Error("User isn't the owner or member of this community");
            }
        }

        return communityFromPersistence;
    }
}