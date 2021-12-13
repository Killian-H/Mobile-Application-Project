package edu.uw.tcss450.group7.chatapp.ui.contact;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewContactListFragment} factory method to
 * create an instance of this fragment.
 */
public class NewContactListFragment extends Fragment {
    private ContactsViewModel mModel;
    private UserInfoViewModel mUserModel;

    public NewContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Getting viewModel objects*/
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ContactsViewModel.class);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_contact, container, false);
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
                    NewContactListFragmentDirections.actionNewContactFragmentToSettingsActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //binding to set things in the xml
        FragmentNewContactBinding binding = FragmentNewContactBinding.bind(getView());
        binding.linearProgress.hide();
        binding.textNotFound.setVisibility(View.GONE);

        //Listeners for contact Searching
        binding.TextInputEmail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    binding.linearProgress.show();
                    mModel.connectGetSearch(mUserModel.getmJwt(),binding.TextInputEmail.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //On click listener to Search Button
        binding.buttonSearch.setOnClickListener(
                (searchList) -> {
                        binding.linearProgress.show();
                        mModel.connectGetSearch(mUserModel.getmJwt(),binding.TextInputEmail.getText().toString());
                }
        );

        //when the SearchList in ContactsViewModel is updated, sets the adapter for the recyclerview.
        mModel.addSearchListObserver(getViewLifecycleOwner(), (contactList) -> {
            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new NewContactRecyclerViewAdapter(contactList, mModel, mUserModel.getmJwt())
                );
                binding.textNotFound.setVisibility(View.GONE);
            }else{
                binding.textNotFound.setVisibility(View.VISIBLE);
            }
            binding.linearProgress.hide();
        });

    }

}