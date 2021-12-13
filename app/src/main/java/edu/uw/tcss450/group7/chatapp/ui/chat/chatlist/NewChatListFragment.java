package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewChatBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.model.NewChatViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragment} factory method to
 * create an instance of this fragment.
 */
public class NewChatListFragment extends Fragment {
    private ContactsViewModel mContactsModel;
    private NewChatViewModel mNewChatModel;
    private ChatListViewModel mChatModel;
    private UserInfoViewModel mUserModel;

    public NewChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        int contactToAdd = this.getArguments().getInt("contactIdToAdd");

        mUserModel = provider.get(UserInfoViewModel.class);
        mContactsModel = provider.get(ContactsViewModel.class);
        mNewChatModel = provider.get(NewChatViewModel.class);
        if (contactToAdd > -1) {
            mNewChatModel.addMemberID(contactToAdd);
        }
        mChatModel = provider.get(ChatListViewModel.class);
        mContactsModel.connectGet(mUserModel.getmJwt());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentNewChatBinding binding = FragmentNewChatBinding.bind(getView());

        binding.linearProgress.hide();

        mContactsModel.addVerifiedListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.listAddContacts.setAdapter(
                        new NewChatRecyclerViewAdapter(contactList, mNewChatModel)
                );
            }
        });

        binding.buttonCreateChat.setOnClickListener( button -> {
            if(binding.textInputChatName.getText() != null ) {
                binding.linearProgress.show();
                if (!(mNewChatModel == null)) {
                    mNewChatModel.connectCreateChatAndAddUsers(mUserModel.getmJwt(), binding.textInputChatName.getText().toString());
                }
                mChatModel.connectGet(mUserModel.getmJwt());
                Navigation.findNavController(getView()).navigateUp();


            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mNewChatModel.clearMemberID();
    }


}