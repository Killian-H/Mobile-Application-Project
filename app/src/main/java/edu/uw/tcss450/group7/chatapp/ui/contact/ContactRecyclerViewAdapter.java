package edu.uw.tcss450.group7.chatapp.ui.contact;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactCardBinding;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    //Store all of the blogs to present
    private final List<Contact> mContacts;


    public ContactRecyclerViewAdapter(List<Contact> items) {

        this.mContacts = items;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
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
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactCardBinding binding;
        private Contact mContact;

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);

        }



        void setContact(final Contact contact) {
            mContact = contact;
            mView.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(
                        ContactListFragmentDirections
                                .actionNavigationContactToContactFragment(contact));
            });

            binding.textName.setText(contact.getFirstName() + " " + contact.getLastName());
            binding.textEmail.setText(contact.getEmail());
        }
    }

}
