package com.telelab.paperville;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputEditText editTextEmail, editTextPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private AppCompatButton loginButton, googleButton;
    private TextView textViewRegister;
    private boolean verified = true;
    private Authentication authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setListeners();
    }


    private void init(){
        editTextEmail = findViewById(R.id.edittext_email_login);
        editTextPassword = findViewById(R.id.edittext_password_login);
        textViewRegister = findViewById(R.id.text_view_register);
        loginButton = findViewById(R.id.button_login);
        inputLayoutEmail = findViewById(R.id.text_input_layout_email_login);
        inputLayoutPassword = findViewById(R.id.text_input_layout_password_login);
        googleButton = findViewById(R.id.button_google_login);
        authentication = new Authentication(this);
    }


    private void setListeners() {
        loginButton.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        googleButton.setOnClickListener(this);
    }

    private void checkCredentials(){
        if(editTextEmail.getText().toString().equals("")){
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Fill in Email");
            return;
        }
        else if(editTextPassword.getText().toString().equals("")){
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutPassword.setError("Fill in Password");
            return;
        }
            inputLayoutEmail.setErrorEnabled(false);
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            Log.d(TAG, "checkCredentials: email : "+email+" pass :"+password);
            authentication.verify(email, password);



    }


    private void goToRegisterActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void googleLogIn() {
        authentication.startGoogleSignIn(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_login:
                checkCredentials();
                break;

            case R.id.text_view_register:
                goToRegisterActivity();
                break;

            case R.id.button_google_login:
                googleLogIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Authentication.RESULT_CODE_SIGNIN){
            authentication.finishGoogleSignIn(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }
}