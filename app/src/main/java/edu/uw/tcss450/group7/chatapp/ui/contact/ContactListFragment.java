package edu.uw.tcss450.group7.chatapp.ui.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactListBinding;
import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {
    private ContactsViewModel mModel;
    private UserInfoViewModel mUserModel;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Getting viewModel objects*/
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ContactsViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();
        //If settings is clicked navigate to SettingsActivity.
        if (id == R.id.action_settings) {
            Navigation.findNavController(getView()).navigate(
                    ContactListFragmentDirections.actionNavigationContactToSettingsActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //binding to set things in the xml
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        //On click listener to floatingButton
        binding.buttonCompose.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactListFragmentDirections
                                .actionNavigationContactToNewContactFragment()));

        //when the contactList in ContactsViewModel is updated, sets the adapter for the recyclerview.
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new edu.uw.tcss450.group7.chatapp.ui.contact.ContactRecyclerViewAdapter(contactList,mUserModel.getmJwt(),mModel)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }

}