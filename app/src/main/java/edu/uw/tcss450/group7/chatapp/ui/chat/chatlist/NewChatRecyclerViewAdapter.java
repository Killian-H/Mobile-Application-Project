package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactCardBinding;
import edu.uw.tcss450.group7.chatapp.databinding.NewChatAddContactCardBinding;
import edu.uw.tcss450.group7.chatapp.model.NewChatViewModel;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;
import edu.uw.tcss450.group7.chatapp.ui.contact.NewContactRecyclerViewAdapter;

public class NewChatRecyclerViewAdapter extends RecyclerView.Adapter<NewChatRecyclerViewAdapter.NewChatViewHolder> {
    private final List<Contact> mContacts;

    private final NewChatViewModel mNewChatViewModel;


    public NewChatRecyclerViewAdapter(List<Contact> items, NewChatViewModel newChatViewModel) {
        this.mContacts = items;
        this.mNewChatViewModel = newChatViewModel;
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

            binding.textEmail.setText(contact.getEmail());
            binding.textName.setText(contact.getFirstName() + " " + contact.getLastName());
            if(mNewChatViewModel.getAddMemList().contains((Integer) mContact.getMemberId())) {
                binding.checkBoxAddToChat.setChecked(true);
            }
            binding.checkBoxAddToChat.setOnClickListener( checkBox -> {
                boolean checked = binding.checkBoxAddToChat.isChecked();
                if(checked) {
                    mNewChatViewModel.addMemberID(mContact.getMemberId());
                } else {
                    mNewChatViewModel.removeMemberID(mContact.getMemberId());
                }
            });


        }


    }
}
