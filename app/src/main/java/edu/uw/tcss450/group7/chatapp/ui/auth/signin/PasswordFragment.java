/*
 * TCSS 450
 * Initial screen for resetting the password.
 */
package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.uw.tcss450.group7.chatapp.databinding.FragmentPasswordBinding;

/**
 * Fragment which handles the user requesting a new password and inputting
 * the code sent to tehir email.
 *
 * @author: Group 7
 */
public class PasswordFragment extends Fragment {

    /* Code input by the user. */
    private static String mCode;

    /* View model for this fragment. */
    private SignInViewModel mModel;

    /* Binding to the view model for this fragment. */
    FragmentPasswordBinding mBinding;

    /* Data checking whether the code provided by the user matches the email. */
    private MutableLiveData<Boolean> mResponsePass3;

    /**
     * Initializes the fragment as well as the live data.
     *
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mResponsePass3 = new MutableLiveData<>();
        mResponsePass3.setValue(false);
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
        mBinding = FragmentPasswordBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    /**
     * Called immediately after onCreateView() once it is known the view has been
     * created without problems. Gives subclasses time to initialize. Assigns
     * onClickListeners to the button on the page. Also creates alert dialogs when
     * the user enters an email. If they hit submit without an email they will be
     * told to enter one. If they enter an invalid email they will be prompted to
     * enter a valid one.
     *
     * @param view The view which has been created.
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.buttonBack.setOnClickListener(view1 -> {
            Navigation.findNavController(getView()).navigate
                    (edu.uw.tcss450.group7.chatapp.ui.auth.signin.
                            PasswordFragmentDirections.actionPasswordFragmentToSignInFragment());
        });
        mBinding.btnHaveCode.setOnClickListener(view1 -> {
            if(mBinding.usernameReset.getText().length()!=0) {
                goodEmail();
            }else{
                new MaterialAlertDialogBuilder(this.getView().getContext())
                        .setMessage("You need to enter  an email first!")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBinding.usernameReset.getText().clear();
                            }
                        }).show();
            }
        });
        mBinding.buttonReset.setOnClickListener(view1 -> {
            if (mBinding.usernameReset.getText() != null) {
                mModel.connectForgetPass(mBinding.usernameReset.getText().toString());
            }
        });
        mModel.addResponsePassObserver(getViewLifecycleOwner(), mResponsePass -> {
            if (!mBinding.usernameReset.getText().toString().isEmpty()) {
                if (!mModel.getResponsePass()) {
                    new MaterialAlertDialogBuilder(this.getView().getContext())
                            .setMessage("The email does not exist.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mBinding.usernameReset.getText().clear();
                                }
                            }).show();
                } else {
                   goodEmail();
                }

            }
        });
    }

    /**
     * When the user enters a valid email, they will be sent an email with
     * a code they have to enter into the alert dialog. If incorrect, they will be
     * prompted to enter it again. When entered the code is compared to the code
     * from the server. If equal they will navigate to the reset fragment.
     */
    public void goodEmail() {
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
                        mModel.connectCheckCode(mBinding.usernameReset.getText().toString(), mCode);
                        mModel.addResponsePassObserver3(getViewLifecycleOwner(), mResponsePass3 -> {
                            System.out.println("mCode: " + mResponsePass3);
                            if (mResponsePass3) {
                                Navigation.findNavController(getView()).navigate
                                        (edu.uw.tcss450.group7.chatapp.ui.auth.signin.
                                                PasswordFragmentDirections.actionPasswordFragmentToPasswordResetFragment(mBinding.usernameReset.getText().toString(),mCode));
                            } else {
                                incorrectCode();
                            }
                        });

                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }})
                .show();
    }

    /**
     * Displays when the user enters an incorrect code.
     */
    public void incorrectCode() {
        new MaterialAlertDialogBuilder(this.getView().getContext())
                .setMessage("The code is incorrect, please try again.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * Gets the email the user entered.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return mBinding.usernameReset.getText().toString();
    }

    /**
     * Gets the code the user entered.
     *
     * @return The user's code.
     */
    public static String getCode() {
        return mCode;
    }

}
