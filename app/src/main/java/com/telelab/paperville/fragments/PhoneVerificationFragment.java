package com.telelab.paperville.fragments;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.telelab.paperville.Authentication;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;

public class PhoneVerificationFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener,
        View.OnKeyListener {

    private static final String TAG = "PhoneVerificationFragme";
    private View view;
    private OnPhoneVerificationListener mListener;
    private View rootView;
    private Context context;
    private AppCompatButton backButton, finalButton;
    private EditText editTextOne, editTextTwo, editTextThree, editTextFour, editTextFive, editTextSix;
    private String code, phone, codeSent;
    private Authentication authentication;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private TextView requestText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phone_verification_fragment, container, false);
        init();
        listeners();
        if(savedInstanceState != null){
            authentication.setVerificationInProcess(savedInstanceState.getBoolean("phoneTimeout"));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("phoneTimeout", authentication.isVerificationInProcess());
    }

    @Override
    public void onStart() {
        super.onStart();
//        authentication.phoneAuth(phone, getActivity(), mCallback);
//        if(authentication.isVerificationInProcess()){
//            authentication.verifyPhone();
//        }
    }

    public interface OnPhoneVerificationListener{
        void onPhoneVerificationListener(String phone);
    }

    public PhoneVerificationFragment(OnPhoneVerificationListener listener, Context context, View rootView, String phone){
        this.rootView = rootView;
        this.phone = phone;
        this.context = context;
        mListener = listener;
    }

    private void init(){
        backButton = view.findViewById(R.id.button_back_final);
        finalButton = rootView.findViewById(R.id.button_registration);
        editTextOne = view.findViewById(R.id.edittext_phone_1);
        editTextTwo = view.findViewById(R.id.edittext_phone_2);
        editTextThree = view.findViewById(R.id.edittext_phone_3);
        editTextFour = view.findViewById(R.id.edittext_phone_4);
        editTextFive = view.findViewById(R.id.edittext_phone_5);
        editTextSix = view.findViewById(R.id.edittext_phone_6);
        authentication = new Authentication(getActivity());
        requestText = view.findViewById(R.id.text_request_again);
    }

    private void listeners(){
        backButton.setOnClickListener(this);
        finalButton.setOnClickListener(this);
        editTextOne.setOnFocusChangeListener(this);
        editTextTwo.setOnFocusChangeListener(this);
        editTextThree.setOnFocusChangeListener(this);
        editTextFour.setOnFocusChangeListener(this);
        editTextFive.setOnFocusChangeListener(this);
        editTextSix.setOnFocusChangeListener(this);
        editTextOne.setOnKeyListener(this);
        editTextTwo.setOnKeyListener(this);
        editTextThree.setOnKeyListener(this);
        editTextFour.setOnKeyListener(this);
        editTextFive.setOnKeyListener(this);
        editTextSix.setOnKeyListener(this);
        HelperMethods.onKeyboardDownListener(rootView, editTextFour);
        HelperMethods.onKeyboardDownListener(rootView, editTextThree);
        HelperMethods.onKeyboardDownListener(rootView, editTextTwo);
        HelperMethods.onKeyboardDownListener(rootView, editTextOne);
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e(TAG, "onVerificationFailed: "+e.getMessage() );
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(getActivity(), "Invalid Number", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                codeSent = s;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                requestText.setTextColor(getResources().getColor(R.color.highlight));
                requestText.setClickable(true);
                requestText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        authentication.phoneAuth(phone, getActivity(), mCallback);
                    }
                });
            }
        };


    }

    private void popFragment(){
        FragmentHelper.popFragment(getParentFragmentManager(), "infoFragment", 0);
    }

    private void verify(){
//        if(authentication.isVerificationInProcess()){
//            if(code.equals(codeSent)){
//                mListener.onPhoneVerificationListener(phone);
//            }
//        }
        mListener.onPhoneVerificationListener(phone);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(!editTextOne.getText().toString().equals("")){
            editTextTwo.requestFocus();
        }
        if(!editTextTwo.getText().toString().equals("")){
            editTextThree.requestFocus();
        }
        if(!editTextThree.getText().toString().equals("")){
            editTextFour.requestFocus();
        }
        if(!editTextFour.getText().toString().equals("")){
            editTextFive.requestFocus();
        }
        if(!editTextFive.getText().toString().equals("")){
            editTextSix.requestFocus();
        }
        if(!editTextSix.getText().toString().equals("")){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextFour.getWindowToken(), 0);
            code = editTextOne.getText().toString() + editTextTwo.getText().toString()
                    + editTextThree.getText().toString() + editTextFour.getText().toString();
            HelperMethods.onKeyboardDownListener(rootView, editTextSix);
            HelperMethods.enableButton(null, finalButton, getActivity());
            verify();
        }
        return false;
    }



    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            view.setBackground(HelperMethods.getDrawable(R.drawable.edittext_custom_active, getActivity()));
        }else{
            view.setBackground(HelperMethods.getDrawable(R.drawable.edittext_custom, getActivity()));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_back_final:
                popFragment();
                break;

            case R.id.button_registration:
                verify();
        }
    }
}
