package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentChatCardBinding;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactRecyclerViewAdapter;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatViewHolder> {
    //Store all of the blogs to present
    private final List<Chat> mChats;


    public ChatListRecyclerViewAdapter(List<Chat> items) {

        this.mChats = items;
    }

    @NonNull
    @Override
    public ChatListRecyclerViewAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListRecyclerViewAdapter.ChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }


    @Override
    public int getItemCount() {
        return mChats.size();
    }


    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private Chat mChat;

        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);

        }



        void setChat(final Chat Chat) {
            mChat = Chat;
//                mView.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        mView.setBackgroundColor(0xFF00FF00);
//                    }
//                });
//                mView.setOnHoverListener(new View.OnHoverListener() {
//                    @Override
//                    public boolean onHover(View v, MotionEvent event) {
//                        mView.setBackgroundColor(0xFF00FF01);
//                        return false;
//                    }
//                });
            binding.textNames.setText("Test Name1, Test Name 2...");

            binding.tectRecentMessage.setText(Chat.getRecentMessage());
        }
    }
}
