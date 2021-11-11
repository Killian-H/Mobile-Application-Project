/*
 * TCSS 450
 * Fragment for Registration.
 */
package edu.uw.tcss450.group7.chatapp.ui.auth.register;

import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.*;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdUpperCase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group7.chatapp.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.group7.chatapp.utils.PasswordValidator;


/**
 * A simple {@link Fragment} subclass.
 * @version: 11/04/2021
 *
 * @author Charles Bryan
 * @author Troy Zon
 * @author Zheng Zhong
 * Commented by: Killian Hickey
 */
public class RegisterFragment extends Fragment {

    // Binding used to create fields for registration.
    private FragmentRegisterBinding binding;

    // The view model for registration.
    private RegisterViewModel mRegisterModel;

    // Validation used for checking a proper password.
    private PasswordValidator mNameValidator = checkPwdLength(1);

    // Validation for proper email formatting.
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Lambda expression used to check for specific traits in a password.
     * The password must be at least seven characters, have one special character,
     * no white space, at least one digit, and at least one uppercase letter.
     */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Empty default public constructor.
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes the fragment.
     *
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    /**
     * Creates and returns the view hierarchy belonging to this fragment.
     *
     * @param inflater Instantiates the xml file for the layout.
     * @param container Container used to contain other views.
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     * @return Returns the view hierarchy belonging to this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * Called immediately after onCreateView() once it is known the view has been
     * created without problems. Gives subclasses time to initialize.
     *
     * @param view The view which has been created.
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Attempts to register the user for the app by calling validation methods
     * to make sure the user has provided all the required information for
     * registration.
     * @param button Button used to initiate the registration process.
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * Validates whether the user has provided a valid first name. If they have not,
     * a message prompt will appear telling the user to enter a first name. Then calls
     * the validateLast() method.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * Checks whether the user has provided a valid last name. If not, a message
     * prompt will appear telling the user to enter a last name. Then calls the
     * validateEmail() method.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * Checks whether the user has provided a valid email. If not, a message
     * prompt will appear telling the user to enter one that is valid. Then calls
     * the validatePasswordsMatch() method.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Checks whether the user entered the same password in both password fields.
     * If not, a message prompt will appear telling the user the passwords must match.
     * Then calls the validatePassword() method.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * Checks to see whether the password is valid. If not a message prompt appears
     * telling the user to enter a valid password. Then calls verifyAuthWithServer().
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password."));
    }

    /**
     * Asynchronous call. verifying the registration with the auth endpoint of the server.
     */
    private void verifyAuthWithServer() {

        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Once the user registers with valid info the fragment will navigate
     * to the login fragment, filling in the email and password field with
     * the email and password the user provided.
     */
    private void navigateToLogin() {
        edu.uw.tcss450.group7.chatapp.ui.auth.register.RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
                edu.uw.tcss450.group7.chatapp.ui.auth.register.RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());



        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
