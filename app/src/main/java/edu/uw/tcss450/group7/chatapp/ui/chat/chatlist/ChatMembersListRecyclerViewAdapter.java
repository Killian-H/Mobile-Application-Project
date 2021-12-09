package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactCardBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.ui.chat.chatroom.ChatViewModel;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;

public class ChatMembersListRecyclerViewAdapter extends RecyclerView.Adapter<ChatMembersListRecyclerViewAdapter.NewContactViewHolder> {

    //Store all of the blogs to present
    private final List<Contact> mContacts;
    private final String mJWT;
    private ChatViewModel mContactsViewModel;
    private int mChatId;


    public ChatMembersListRecyclerViewAdapter(List<Contact> items, ChatViewModel mModel, int chatId, String JWT) {
        this.mContacts = items;
        this.mJWT = JWT;
        this.mChatId = chatId;
        mContactsViewModel = mModel;
    }

    @NonNull
    @Override
    public NewContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_new_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class NewContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentNewContactCardBinding binding;
        private Contact mContact;

        public NewContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentNewContactCardBinding.bind(view);

        }



        void setContact(final Contact contact) {
            mContact = contact;

            binding.textUserDetails.setText(contact.getFirstName() + ", " + contact.getEmail());
            binding.buttonSendRequest.setText("Remove");
            binding.buttonSendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(v.getContext())
                            .setMessage("Are you sure to remove " + mContact.getFullName() + " from the chatroom?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mContactsViewModel.getMemberListByChatId(mChatId);
                                }
                            })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mContactsViewModel.deleteChatMembers(mChatId,mJWT,mContact.getEmail());
                                }
                            })
                            .show();
            }});
        }
    }

}
