package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat;

/**
 * This class is used to create Dummy Blogs. Use it for development.
 * In future labs, connect to an API to gain actual Blog Data.
 */
public final class ChatRoomGenerator {

    private static final Chat[] CHATS;
    public static final int COUNT = 20;


    static {
        CHATS = new Chat[COUNT];
        for (int i = 0; i < CHATS.length; i++) {
            CHATS[i] = new Chat.Builder(-1,"test chat").build();
        }
    }

    public static List<Chat> getChatList() {
        return Arrays.asList(CHATS);
    }

    public static Chat[] getChats() {
        return Arrays.copyOf(CHATS, CHATS.length);
    }

    private ChatRoomGenerator() { }


}