package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdUpperCase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group7.chatapp.databinding.FragmentPasswordBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentPasswordResetBinding;
import edu.uw.tcss450.group7.chatapp.utils.PasswordValidator;

public class PasswordResetFragment extends Fragment {

    /* View model for this fragment. */
    private SignInViewModel mModel;

    /* Binding to the view model for this fragment. */
    private FragmentPasswordResetBinding mBinding;

    private FragmentPasswordBinding mPassBinding;

    /**
     * Lambda expression used to check for specific traits in a password.
     * The password must be at least seven characters, have one special character,
     * no white space, at least one digit, and at least one uppercase letter.
     */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(mBinding.passTwo.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

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
        mBinding = FragmentPasswordResetBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PasswordResetFragmentArgs args = PasswordResetFragmentArgs.fromBundle(getArguments());
        mBinding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePasswordsMatch();
                mModel.connectResetPass(args.getCode(), args.getEmail(), mBinding.passOne.getText().toString());

            }
        });
    }

    private void navigateToLogin() {
        edu.uw.tcss450.group7.chatapp.ui.auth.signin.PasswordResetFragmentDirections.ActionPasswordResetFragmentToSignInFragment directions =
                edu.uw.tcss450.group7.chatapp.ui.auth.signin.PasswordResetFragmentDirections.actionPasswordResetFragmentToSignInFragment();

        directions.setPassword(mBinding.passOne.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);
    }

    /**
     * Checks whether the user entered the same password in both password fields.
     * If not, a message prompt will appear telling the user the passwords must match.
     * Then calls the validatePassword() method.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(mBinding.passTwo.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(mBinding.passOne.getText().toString().trim()),
                this::validatePassword,
                result -> mBinding.passOne.setError("Passwords must match."));
    }

    /**
     * Checks to see whether the password is valid. If not a message prompt appears
     * telling the user to enter a valid password. Then calls verifyAuthWithServer().
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.passOne.getText().toString()),
                this::navigateToLogin,
                result -> mBinding.passOne.setError("Please enter a valid Password."));
    }
}
