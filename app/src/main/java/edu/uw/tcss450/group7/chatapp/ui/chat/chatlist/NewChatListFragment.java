package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;
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

import edu.uw.tcss450.group7.chatapp.databinding.FragmentChatListBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactListBinding;
import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewChatBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragment} factory method to
 * create an instance of this fragment.
 */
public class NewChatListFragment extends Fragment {
    private ContactsViewModel mModel;
    private UserInfoViewModel mUserModel;

    public NewChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ContactsViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_chat, container, false);
        if (view instanceof RecyclerView) {

            ((RecyclerView) view).setAdapter(
                    new edu.uw.tcss450.group7.chatapp.ui.contact.ContactRecyclerViewAdapter(edu.uw.tcss450.group7.chatapp.ui.contact.ContactGenerator.getContactList()));
        }
        return inflater.inflate(R.layout.fragment_new_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FragmentNewChatBinding binding = FragmentNewChatBinding.bind(getView());


        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new edu.uw.tcss450.group7.chatapp.ui.contact.ContactRecyclerViewAdapter(contactList)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }

}