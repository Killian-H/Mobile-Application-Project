/*
 * TCSS 450
 *
 * Recycler view for chat rooms.
 */
package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentChatCardBinding;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragmentDirections;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactRecyclerViewAdapter;

/**
 * Recycler view for the ChatListFragment displaying the chatrooms.
 *
 * @author Charles Bryan
 * @author Group 7
 * Commented by: Killian Hickey
 */
public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatViewHolder> {

    /* Store all of the chats to present */
    private final List<Chat> mChats;

    /**
     * View adapter for the list of chats.
     *
     * @param items List of chats.
     */
    public ChatListRecyclerViewAdapter(List<Chat> items) {

        this.mChats = items;
    }

    /**
     * Holds the view adapter created for viewing the chat list. Also inflates
     * the xml for the chat cards.
     *
     * @param parent Container for the cha list.
     * @param viewType TBA
     *
     * @return Returns the inflated chat list.
     */
    @NonNull
    @Override
    public ChatListRecyclerViewAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListRecyclerViewAdapter.ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }


    /**
     * Returns the number of chats.
     *
     * @return The number of chats.
     */
    @Override
    public int getItemCount() {
        return mChats.size();
    }


    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ChatViewHolder extends RecyclerView.ViewHolder {

        /* The current view. */
        public final View mView;

        /* Binding to the chat card fragment. */
        public FragmentChatCardBinding mBinding;

        /* The chatroom. */
        private Chat mChat;

        /**
         * Sets the view to the parent view.
         * @param view
         */
        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentChatCardBinding.bind(view);

        }

        /**
         * Sets the color of the chat cards when hovered over, and when it looses
         * focus. Also sets the name of the chat and displays the lest message sent.
         *
         * @param chat The chat room.
         */
        void setChat(final Chat chat) {
            mChat = chat;
                mView.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        mView.setBackgroundColor(0xFF00FF00);
                    }
                });
                mView.setOnHoverListener(new View.OnHoverListener() {
                    @Override
                    public boolean onHover(View v, MotionEvent event) {
                        mView.setBackgroundColor(0xFF00FF01);
                        return false;
                    }
                });
                mView.setOnClickListener(view -> {
                    Navigation.findNavController(mView).navigate(
                            ChatListFragmentDirections
                                    .actionNavigationChatToChatFragment(chat));
                });

            mBinding.textNames.setText(mChat.getChatName());

            mBinding.tectRecentMessage.setText(chat.getRecentMessage());
        }
    }
}
