package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group7.chatapp.utils.PasswordValidator.checkPwdSpecialChar;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentPasswordBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group7.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.group7.chatapp.utils.PasswordValidator;

public class PasswordFragment extends Fragment {

    private static String mCode;
    /* View model for this fragment. */
    private SignInViewModel mModel;

    /* Binding to the view model for this fragment. */
    FragmentPasswordBinding mBinding;

    private boolean mReceived;


    private MutableLiveData<Boolean> mResponsePass3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mResponsePass3 = new MutableLiveData<>();
        mResponsePass3.setValue(false);
        mReceived = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPasswordBinding.inflate(inflater);
        return mBinding.getRoot();
    }

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
                        .setMessage("You need to enter email first!")
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

    public String getEmail() {
        return mBinding.usernameReset.getText().toString();
    }

    public static String getCode() {
        return mCode;
    }

}
