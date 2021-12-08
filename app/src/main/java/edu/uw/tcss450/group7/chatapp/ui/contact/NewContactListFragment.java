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
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ContactsViewModel.class);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);
        if (view instanceof RecyclerView) {
            //Try out a grid layout to achieve rows AND columns. Adjust the widths of the
            //cards on display
//            ((RecyclerView) view).setLayoutManager(new GridLayoutManager(getContext(), 2));

            //Try out horizontal scrolling. Adjust the widths of the card so that it is
            //obvious that there are more cards in either direction. i.e. don't have the cards
            //span the entire witch of the screen. Also, when considering horizontal scroll
            //on recycler view, ensure that there is other content to fill the screen.
//            ((LinearLayoutManager)((RecyclerView) view).getLayoutManager())
//                    .setOrientation(LinearLayoutManager.HORIZONTAL);

//            ((RecyclerView) view).setAdapter(
//                    new ContactRecyclerViewAdapter(ContactGenerator.getContactList()));
        }
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

        if (id == R.id.action_settings) {
            Navigation.findNavController(getView()).navigate(
                    NewContactListFragmentDirections.actionNewContactFragmentToSettingsActivity());

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentNewContactBinding binding = FragmentNewContactBinding.bind(getView());
        binding.linearProgress.hide();
        binding.textNotFound.setVisibility(View.GONE);

//        Listeners for contact Searching

        binding.TextInputEmail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    binding.linearProgress.show();
                    mModel.connectGetSearch(mUserModel.getmJwt(),binding.TextInputEmail.getText().toString());
                    return true;
                }
                return false;
            }
        });
        binding.buttonSearch.setOnClickListener(
                (searchList) -> {
                        binding.linearProgress.show();
                        mModel.connectGetSearch(mUserModel.getmJwt(),binding.TextInputEmail.getText().toString());
                }
        );

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