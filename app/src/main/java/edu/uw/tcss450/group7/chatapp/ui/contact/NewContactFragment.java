package edu.uw.tcss450.group7.chatapp.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentNewContactBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewContactFragment} factory method to
 * create an instance of this fragment.
 */
public class NewContactFragment extends Fragment {

    public NewContactFragment() {
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
        return inflater.inflate(R.layout.fragment_new_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentNewContactBinding binding = FragmentNewContactBinding.bind(getView());

        binding.textPubdate.setText("Create new Contact");
        binding.textTitle.setText("Create new Contact Title");

        binding.textPreview.setText("We will create a new Contact Here");


        //Note we are using an Intent here to start the default system web browser
        binding.buttonUrl.setOnClickListener(button ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.com"))));

    }

}