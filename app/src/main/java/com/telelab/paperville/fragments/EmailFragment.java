package com.telelab.paperville.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.telelab.paperville.Authentication;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;
import com.telelab.paperville.RegistrationActivity;

public class EmailFragment extends Fragment implements TextWatcher, View.OnClickListener,
        TextView.OnEditorActionListener {
    private static final String TAG = "EmailFragment";
    private View view;
    private final View rootView;
    private TextInputLayout inputLayout;
    private TextInputEditText editText;
    private AppCompatButton button, continueButton, googleButton;
    private boolean verified;
    private final OnEmailVerifiedListener mListener;
    private final Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.email_fragment, container, false);
        init();
        setListeners();

        return view;
    }

    public interface OnEmailVerifiedListener{
        void onEmailVerified(String email);
    }


    public EmailFragment(OnEmailVerifiedListener listener, View rootView, Context context){
        mListener = listener;
        this.rootView = rootView;
        this.context = context;
    }


    private void init() {
        editText = view.findViewById(R.id.edittext_email);
        button = view.findViewById(R.id.button_email_next);
        inputLayout = view.findViewById(R.id.text_input_layout_email);
        continueButton = rootView.findViewById(R.id.button_registration);
        googleButton = view.findViewById(R.id.button_google);
        verified = false;
    }

    private void setListeners() {
        editText.addTextChangedListener(this);
        button.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
        continueButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        HelperMethods.onKeyboardDownListener(rootView, inputLayout);
    }


    private void verifyEmail(String email){

        verified = Authentication.basicEmailAuth(email);


        if(verified){
            HelperMethods.enableButton(button, continueButton, getActivity());

        }else{
            HelperMethods.disableButton(button, continueButton, "Enter Email To Continue", getActivity());

        }
    }



    private void emailVerified(){
        if(verified){
            continueButton.setText("Enter Password to continue");
            continueButton.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_registration, getActivity()));
            continueButton.setClickable(false);
            mListener.onEmailVerified(editText.getText().toString());
        }
    }

    private void googleLogIn() {
        Authentication authentication = new Authentication(context);
        authentication.startGoogleSignIn(getActivity());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length() != 0){
            verifyEmail(editable.toString());
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_email_next:
                Log.d(TAG, "onClick: email next");
                emailVerified();
                break;

            case R.id.button_registration:
                Log.d(TAG, "onClick: email regis");
                emailVerified();
                break;

            case R.id.button_google:
                googleLogIn();
                break;
        }
    }




    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            emailVerified();
        }
        return false;
    }


}
