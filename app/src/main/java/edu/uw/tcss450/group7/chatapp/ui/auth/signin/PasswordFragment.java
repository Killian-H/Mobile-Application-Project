package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdSpecialChar;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentPasswordBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group7.chatapp.utils.PasswordValidator;

public class PasswordFragment extends Fragment {

    /* View model for this fragment. */
    private SignInViewModel mModel;

    /* Binding to the view model for this fragment. */
    FragmentPasswordBinding mBinding;

    private String mCode = "";
    // Validation for proper email formatting.
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPasswordBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.buttonReset.setOnClickListener(view1 -> {
            if (mBinding.usernameReset.getText() != null) {
                mModel.connectForgetPass(mBinding.usernameReset.getText().toString());
            }
        });
            mModel.addResponsePassObserver(getViewLifecycleOwner(), mResponsePass -> {
                if (!mBinding.usernameReset.getText().toString().isEmpty()) {
                    if (!mResponsePass) {
                        new MaterialAlertDialogBuilder(this.getView().getContext())
                                .setMessage("The email does not exist.")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mBinding.usernameReset.getText().clear();
                                    }
                                }).show();
                    }
                        final EditText input = new EditText(this.getView().getContext());
                        new MaterialAlertDialogBuilder(this.getView().getContext())
                                .setMessage("An email with a verification code has been sent to " +
                                        mBinding.usernameReset.getText().toString() +
                                        ". Please enter the code below.")
                                .setView(input)
                                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mCode = input.getText().toString();
                                        // TODO if else statement checking if code is correct before navigation.
                                        Navigation.findNavController(getView()).navigate
                                                (edu.uw.tcss450.group7.chatapp.ui.auth.signin.
                                                        PasswordFragmentDirections.actionPasswordFragmentToPasswordResetFragment());
                                    }
                                }).show();
                }
            });
    }

//    private void attemptReset(final View button) {
//        validateEmail();
//    }

}
