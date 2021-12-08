package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentChatListBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactListBinding;
import edu.uw.tcss450.group7.chatapp.model.ContactsViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragmentDirections;
//import edu.uw.tcss450.group7.chatapp.ui.Chat.Fragment_ChatDirections;

/**
 * A simple {@link Fragment} subclass.

 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mModel;
    private UserInfoViewModel mUserModel;

    private FragmentChatListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
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

            ((RecyclerView) view).setAdapter(
                    new edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.ChatListRecyclerViewAdapter(edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.ChatRoomGenerator.getChatList(),false));
        }
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ChatListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
        setHasOptionsMenu(true);
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
                    ChatListFragmentDirections.actionNavigationChatToSettingsActivity());

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

        //On click listener to floatingButton
        binding.buttonCompose.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ChatListFragmentDirections.actionNavigationChatToNewChatListFragment()));

        mModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            if (!chatList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new ChatListRecyclerViewAdapter(chatList, false)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
        mModel.addChatListEmptyObserver(getViewLifecycleOwner(), mIsChatListEmpty -> {
            if (mIsChatListEmpty) {
                binding.layoutWait.setVisibility(View.GONE);
                List<Chat> temp = new ArrayList<>();
                temp.add(new edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat.Builder(-1, " You don't have any chats")
                        .addRecentMessage("Please create new chat!")
                        .build());
                ChatListRecyclerViewAdapter viewAdapter = new ChatListRecyclerViewAdapter(temp, true);
                binding.listRoot.setAdapter(viewAdapter);
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getFragmentManager()
//                .beginTransaction()
//                .detach(LobbyFragment.this)
//                .attach(LobbyFragment.this)
//                .commit();
//
//    }
}