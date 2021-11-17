package edu.uw.tcss450.group7.chatapp.ui.contact;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactBinding;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContactFragmentArgs args = ContactFragmentArgs.fromBundle(getArguments());

        FragmentContactBinding binding = FragmentContactBinding.bind(getView());

        binding.textPubdate.setText(args.getContact().getFullName());
        binding.textTitle.setText(args.getContact().getFullName());

        binding.textPreview.setText(args.getContact().getEmail());

        //Note we are using an Intent here to start the default system web browser
//        binding.buttonUrl.setOnClickListener(button ->
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse(args.getContact().getUrl())))
//        );

    }

}