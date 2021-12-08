package edu.uw.tcss450.group7.chatapp.ui.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactCardBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;

public class NewContactRecyclerViewAdapter extends RecyclerView.Adapter<NewContactRecyclerViewAdapter.NewContactViewHolder> {

    //Store all of the blogs to present
    private final List<Contact> mContacts;
    private final String mJWT;
    private ContactsViewModel mContactsViewModel;


    public NewContactRecyclerViewAdapter(List<Contact> items, ContactsViewModel mModel, String isIncoming) {
        this.mContacts = items;
        this.mJWT = isIncoming;
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
//            mView.setOnClickListener(view -> {
//                Navigation.findNavController(mView).navigate(
//                        ContactListFragmentDirections
//                                .actionNavigationContactToContactFragment(contact));
//            });
            binding.buttonSendRequest.setText("Send Request");
            binding.buttonSendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContactsViewModel.sendContactRequest(mJWT, mContact.getMemberId());
                    binding.buttonSendRequest.setText("Request Sent!");
                    binding.buttonSendRequest.setClickable(false);
                }
            });
//            if(){
//
//            }
            binding.textUserDetails.setText(contact.getFirstName() + " " + contact.getLastName());
        }
    }

}
