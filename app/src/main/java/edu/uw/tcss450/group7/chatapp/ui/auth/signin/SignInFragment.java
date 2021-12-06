/*
 * TCSS 450
 *
 * Sign in fragment.
 */
package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdSpecialChar;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group7.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.group7.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group7.chatapp.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author Charles Bryan
 * @author Group 7
 * Commented by: Killian Hickey
 */
public class SignInFragment extends Fragment {

    /* Binding to the view model for this fragment. */
    private FragmentSignInBinding mBinding;

    /* View model for this fragment. */
    private SignInViewModel mSignInModel;

    /* Pushy token view model. */
    private PushyTokenViewModel mPushyTokenViewModel;

    /* VIew model for the info about the user. */
    private UserInfoViewModel mUserViewModel;

    /* Checks to see if the email provided is valid. */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /* Checks to see if the password provided is valid. */
    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    /**
     * Required default public constructor.
     */
    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes the fragment and the pushy token view model.
     *
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
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
        mBinding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    /**
     * Called immediately after onCreateView() once it is known the view has been
     * created without problems. Gives subclasses time to initialize. Handles what
     * happens when the yser enters a valid email and password combination, then
     * clicks the sign-in button, which navigates to the MainActivity.
     *
     *
     * @param view The view which has been created.
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonToRegister.setOnClickListener(button ->
            Navigation.findNavController(getView()).navigate(
                    edu.uw.tcss450.group7.chatapp.ui.auth.signin.SignInFragmentDirections.actionLoginFragmentToRegisterFragment()
            ));

        mBinding.buttonSignIn.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        edu.uw.tcss450.group7.chatapp.ui.auth.signin.SignInFragmentArgs args = edu.uw.tcss450.group7.chatapp.ui.auth.signin.SignInFragmentArgs.fromBundle(getArguments());
        mBinding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
//        mBinding.editEmail.setText("zhong4475@gmail.com");
//        mBinding.editPassword.setText("test123@");
        mBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                mBinding.buttonSignIn.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    /**
     * Gets called once the fragment has been created. Checks to see if the
     * JWT associated with the user is still valid. If it is, it will call
     * navigateToSucess().
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, token);
                return;
            }
        }
    }

    /**
     * Calls the validateEmail() method when the user presses the sign-in button.
     *
     * @param button The sign-in button.
     */
    private void attemptSignIn(final View button) {
        validateEmail();
    }

    /**
     * Checks whether the email provided is valid.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mBinding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> mBinding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Checks whether the password is valid.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> mBinding.editPassword.setError("Please enter a valid Password."));
    }

    /**
     * Asynchronous call to the server with the email and password
     * provided by the user to verify whether the email and password
     * are linked to a user in the database.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                mBinding.editEmail.getText().toString(),
                mBinding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().

    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     *
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        if (mBinding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }
        Navigation.findNavController(getView())
                .navigate(edu.uw.tcss450.group7.chatapp.ui.auth.signin.SignInFragmentDirections
                        .actionLoginFragmentToMainActivity(email, jwt));
        //Remove THIS activity from the Task list. Pops off the backstack
        getActivity().finish();
    }
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getmJwt());
    }


    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                mBinding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        mBinding.editEmail.getText().toString(),
                        mUserViewModel.getmJwt()
                );
            }
        }
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
                    mBinding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    mBinding.editEmail.getText().toString(),
                                    response.getString("token")
                            )).get(UserInfoViewModel.class);
                    Log.d("sign", mUserViewModel.toString());
                    Log.d("sign", mUserViewModel.getmJwt());

                    sendPushyToken();

                    navigateToSuccess(
                            mBinding.editEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
