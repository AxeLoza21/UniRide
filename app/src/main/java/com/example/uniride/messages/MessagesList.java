package com.example.uniride.messages;

public class MessagesList {
    private String name,mobile,lastMessages,profilePic,chatKey;
    private int unseenMessages;

    public MessagesList(String name, String mobile, String lastMessages,String profilePic, int unseenMessages,String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessages = lastMessages;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLastMessages() {
        return lastMessages;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
