/*
 * TCSS 450
 * Fragment for the chat lists.
 */
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
 * Class creating a new fragment for the chat list.
 *
 * @author Charles Bryan
 * @author Group 7
 * Commented by: Killian Hickey
 */
public class ChatListFragment extends Fragment {

    /* View model for the list of chat rooms. */
    private ChatListViewModel mModel;

    /* View model for the current users info. */
    private UserInfoViewModel mUserModel;

    /* Binding to the view model for this fragment. */
    private FragmentChatListBinding binding;

    /**
     * Initializes the fragment and the view models needed for
     * this fragment.
     *
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ChatListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
        setHasOptionsMenu(true);
    }

    /**
     * Creates and returns the view hierarchy belonging to this fragment
     * and sets the recycler view to be a vertical list.
     *
     * @param inflater Instantiates the xml file for this layout.
     * @param container Container used to contain other views.
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     *
     * @return Returns the view hierarchy belonging to this fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).setAdapter(
                    new edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.ChatListRecyclerViewAdapter(edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.ChatRoomGenerator.getChatList(),false));
        }
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    /**
     * Instantiates the xml file for the drop down menu.
     * @param menu The menu being inflated.
     * @param inflater Instantiates the toolbar xml file.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Sets the behavior of the Settings item in the toolbar. Navigates
     * to Settings Activity when selected.
     *
     * @param item The Settings item in the menu.
     *
     * @return The behavior for the item.
     */
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

    /**
     * Called immediately after onCreateView() once it is known the view has been
     * created without problems. Gives subclasses time to initialize. Handles displaying
     * the chat list. If there are no chats no chats will be displayed.
     *
     *
     * @param view The view which has been created.
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
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

    @Override
    public void onResume() {
        super.onResume();
        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());
        mModel.connectGet(mUserModel.getmJwt());
    }
}