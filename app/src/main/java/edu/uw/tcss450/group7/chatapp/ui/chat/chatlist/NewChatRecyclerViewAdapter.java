package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactCardBinding;
import edu.uw.tcss450.group7.chatapp.databinding.NewChatAddContactCardBinding;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;
import edu.uw.tcss450.group7.chatapp.ui.contact.NewContactRecyclerViewAdapter;

public class NewChatRecyclerViewAdapter extends RecyclerView.Adapter<NewChatRecyclerViewAdapter.NewChatViewHolder> {
    private final List<Contact> mContacts;


    public NewChatRecyclerViewAdapter(List<Contact> items) {
        this.mContacts = items;
    }

    @NonNull
    @Override
    public NewChatRecyclerViewAdapter.NewChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewChatRecyclerViewAdapter.NewChatViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.new_chat_add_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatRecyclerViewAdapter.NewChatViewHolder holder, int position) {
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
    public class NewChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public @NonNull NewChatAddContactCardBinding binding;
        private Contact mContact;

        public NewChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = NewChatAddContactCardBinding.bind(view);

        }



        void setContact(final Contact contact) {
            mContact = contact;

//            binding.buttonSearch.setOnClickListener();
//            if(){
//
//            }
            binding.textEmail.setText(contact.getEmail());
            binding.textName.setText(contact.getFirstName() + " " + contact.getLastName());

//            mView.setOnClickListener(view -> {
//
//            });
        }
    }
}
