package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentChatDetailsBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.model.NewChatViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group7.chatapp.ui.chat.chatroom.ChatViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatDetailsFragment} factory method to
 * create an instance of this fragment.
 */
public class ChatDetailsFragment extends Fragment {
    private ChatViewModel mModel;
    private ContactsViewModel mContactModel;
    private UserInfoViewModel mUserModel;

    public ChatDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactModel = provider.get(ContactsViewModel.class);
        mModel = provider.get(ChatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChatDetailsFragmentArgs args = ChatDetailsFragmentArgs.fromBundle(getArguments());


        FragmentChatDetailsBinding binding = FragmentChatDetailsBinding.bind(getView());

        binding.textTitle.setText("Member's List:");

        mModel.getChatMembers(args.getChat().getChatId(), mUserModel.getmJwt());
        mModel.addMemberListObserver(args.getChat().getChatId(), getViewLifecycleOwner(), (memberList) -> {
                    binding.listRoot.setAdapter(new ChatMembersListRecyclerViewAdapter(memberList, mModel, args.getChat().getChatId(), mUserModel.getmJwt()));
                });
        mContactModel.connectGet(mUserModel.getmJwt());
        mContactModel.addVerifiedListObserver(getViewLifecycleOwner(), (contactList) -> {
            binding.addToChatList.setAdapter(new ChatAddMemberListRecyclerViewAdapter(contactList, mModel, args.getChat().getChatId(), mUserModel.getmJwt()));
        });

    }

}