package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdSpecialChar;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentPasswordBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group7.chatapp.utils.PasswordValidator;

public class PasswordFragment extends Fragment {

    /* View model for this fragment. */
    private SignInViewModel mModel;

    /* Binding to the view model for this fragment. */
    FragmentPasswordBinding mBinding;

    // Validation for proper email formatting.
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mBinding.usernameReset.getText().toString();
            }
        });
    }

//    private void attemptReset(final View button) {
//        validateEmail();
//    }

}
