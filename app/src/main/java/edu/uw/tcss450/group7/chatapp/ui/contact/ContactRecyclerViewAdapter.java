package edu.uw.tcss450.group7.chatapp.ui.contact;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;

/**
 *  RecyclerView.Adapter is used for Contact Recycler views.
 *
 */
public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    /**mContacts stores the contacts*/
    private final List<Contact> mContacts;
    /**View Model for contacts*/
    private ContactsViewModel mContactsViewModel;
    /**jwt used for api calls*/
    private String mJWT;
    /**Map to make contact cards expandable*/
    private final Map<Contact, Boolean> mExpandedFlags;

    /**Constructor*/
    public ContactRecyclerViewAdapter(List<Contact> contactList, String getmJwt, ContactsViewModel mModel) {
        this.mContacts = contactList;
        this.mExpandedFlags = mContacts.stream().collect(Collectors.toMap(Function.identity(), contact -> false));
        mContactsViewModel = mModel;
        mJWT = getmJwt;
    }


    /**Override methods*/
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
     * of rows in the Contact Recycler View.
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

        private void handleMoreOrLess(final View button){
            mExpandedFlags.put(mContact, !mExpandedFlags.get(mContact));
            displayPreview();
        }

        private void displayPreview(){
            if(mExpandedFlags.get(mContact)){
                binding.layoutDrop.setVisibility(View.VISIBLE);
                binding.buttonRemove.setVisibility(View.VISIBLE);
            }else{
                binding.layoutDrop.setVisibility(View.GONE);
                binding.buttonRemove.setVisibility(View.GONE);

            }
        }

        /**Setting contact information for each card*/
        void setContact(final Contact contact) {
            mContact = contact;
            /**Differentiating either the contact is verified and filing card details depending on that*/
            if(mContact.getVerified()){
                mView.setOnClickListener(view -> {
                    handleMoreOrLess(view);
                });
                binding.textName.setText(contact.getFullName());
                binding.textEmail.setText(contact.getEmail());
                binding.buttonAccept.setVisibility(View.GONE);
                binding.buttonDecline.setVisibility(View.GONE);
            }else{
                if(mContact.getIsIncomingRequest()){
                    binding.textName.setText("Incoming Request!");
                    binding.buttonAccept.setVisibility(View.VISIBLE);
                    binding.buttonDecline.setVisibility(View.VISIBLE);
                    binding.textEmail.setText(contact.getFullName());
                    /**setOnClickListener for accepting contact request*/
                    binding.buttonAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContactsViewModel.respondIncomingRequest(mJWT,mContact.getMemberId(),Boolean.TRUE);
                        }
                    });
                    /**setOnClickListener for declining contact request*/
                    binding.buttonDecline.setOnClickListener(view ->
                                    new MaterialAlertDialogBuilder(this.itemView.getContext())
                                            .setMessage("Are you sure you want to decline " + mContact.getFullName() + "'s friend request?")
                                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    handleMoreOrLess(view);
                                                }
                                            })
                                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mContactsViewModel.respondIncomingRequest(mJWT,mContact.getMemberId(),Boolean.FALSE);
                                                }
                                            })
                                            .show()
                    );
                }
            }

            /**setOnClickListener for remove button
             * It creates an Alert Dialog to double check with the user before deleting a contact.
             * */
            binding.buttonRemove.setOnClickListener(view ->
                    new MaterialAlertDialogBuilder(this.itemView.getContext())
                            .setMessage("Are you sure you want to remove " + mContact.getFullName() + " from your contacts?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    handleMoreOrLess(view);
                                }
                            })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mContactsViewModel.removeContact(mJWT,mContact.getMemberId());
                                }
                            })
                            .show()
            );

            /**setOnClickListener for Send Message button
             * It navigates to the New Chat creation page*/
            binding.buttonSendMessage.setOnClickListener( view ->
                Navigation.findNavController(mView).navigate(
                        ContactListFragmentDirections
                                .actionNavigationContactToNewChatListFragment(mContact.getMemberId()))
            );
            displayPreview();
        }
    }

}
