package com.telelab.paperville.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.telelab.paperville.AnimationHelper;
import com.telelab.paperville.Authentication;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;

import java.util.regex.Pattern;

public class PasswordFragment extends Fragment implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    private static final String TAG = "PasswordFragment";
    private View view;
    private AppCompatButton backButton;
    private Context context;
    private View rootView;
    private TextInputLayout inputLayout;
    private TextInputEditText editText;
    private AppCompatButton button, continueButton;
    private boolean verified;
    private OnPasswordVerifiedListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.password_fragment, container, false);
        init();
        setListeners();
        return view;
    }

    public interface OnPasswordVerifiedListener{
        void onPasswordVerifiedListener(String password);
    }

    public PasswordFragment(Context context, View rootView, OnPasswordVerifiedListener listener){
        this.context = context;
        this.rootView = rootView;
        mListener = listener;
    }


    private void setListeners() {
        backButton.setOnClickListener(this);
        editText.addTextChangedListener(this);
        editText.setOnEditorActionListener(this);
        button.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        HelperMethods.onKeyboardDownListener(rootView, inputLayout);
    }

    private void init() {
        backButton = view.findViewById(R.id.button_password_back);
        inputLayout = view.findViewById(R.id.text_input_layout_password);
        editText = view.findViewById(R.id.edittext_password);
        button = view.findViewById(R.id.button_password_next);
        continueButton = rootView.findViewById(R.id.button_registration);

    }


    private void changeToInfoFragment(){
        if(verified){
            continueButton.setText("Fill Information To Continue");
            continueButton.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_registration, getActivity()));
            mListener.onPasswordVerifiedListener(editText.getText().toString());
        }
    }

    private void popFragment() {
        FragmentHelper.popFragment(getParentFragmentManager(), "emailFragment", 0);
        HelperMethods.prevText(R.id.text_email, R.id.text_password, context, rootView);
        HelperMethods.prevTimeline(R.id.first_circle, R.id.second_circle, R.id.first_line, context, rootView);

    }

    private void verifyPassword(String password){

        verified = Authentication.passwordAuth(password);


        if(verified){
            HelperMethods.enableButton(button, continueButton, getActivity());
        }else{
            HelperMethods.disableButton(button, continueButton, "Enter Password To Continue", getActivity());
        }
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
            verifyPassword(editable.toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_password_back:
                Log.d(TAG, "onClick: back button");
                popFragment();
                break;

            case R.id.button_password_next:
                Log.d(TAG, "onClick: next button");
                changeToInfoFragment();
                break;

            case R.id.button_registration:
                Log.d(TAG, "onClick: regis button");
                changeToInfoFragment();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            Log.d(TAG, "onEditorAction: editor button");
            changeToInfoFragment();
        }
        return false;
    }
}
