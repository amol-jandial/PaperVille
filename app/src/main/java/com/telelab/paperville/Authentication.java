package com.telelab.paperville;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.telelab.paperville.models.Class;
import com.telelab.paperville.models.Student;
import com.telelab.paperville.models.Subject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Authentication{
    private static final String TAG = "Authentication";
    static final int RESULT_CODE_SIGNIN = 999;
    Student student;
    final Context context;
    private boolean verificationInProcess = false;
    private PhoneAuthOptions options;

    public Authentication(Student student, Context context){
        this.student = student;
        this.context = context;
    }

    public Authentication(Context context){
        this.context = context;
    }

    public void setVerificationInProcess(boolean verificationInProcess) {
        this.verificationInProcess = verificationInProcess;
    }

    public boolean isVerificationInProcess() {
        return verificationInProcess;
    }

    public void upload(Student student, Class mClass, Activity activity){
        ProgressDialog progressDialog = ProgressDialog.show(context, "Registering", "Please wait...",true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        boolean found = false;
        auth.createUserWithEmailAndPassword(student.getEmail(), student.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                            databaseHandler.initialize(student, mClass);
                            if(user != null){
                                databaseHandler.getUserData();
                                progressDialog.dismiss();
                            }else{
                                Log.d(TAG, "onComplete: couldn't log but registered");
                            }
                        }else{
                            Log.e(TAG, "onComplete: error registering "+task.getException() );
                        }
                    }
                });

    }

    public void verify(String email, String password){
        ProgressDialog progressDialog = ProgressDialog.show(context, "Login","Please wait...",true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    if(auth != null){
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        progressDialog.dismiss();
                    }else{
                        Log.e(TAG, "onComplete: login done but user getting failed");
                    }
                }else{
                    Log.e(TAG, "onComplete: login fail"+task.getException() );
                }
            }
        });

    }

    public static boolean checkEmail(String email, TextInputLayout inputLayout){

        return true;
    }

    public static boolean checkPassword(String password, TextInputLayout inputLayout){
        return true;
    }

    public static void getData(){

    }

    public void startGoogleSignIn(Activity activity){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(context, gso);
        signInClient.signOut();
        Intent signInIntent = signInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RESULT_CODE_SIGNIN);
    }

    public void finishGoogleSignIn(Task<GoogleSignInAccount> task){
        ProgressDialog progressDialog = ProgressDialog.show(context, "Google","Please wait...",true);
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                                progressDialog.dismiss();
                            }else{
                                Log.e(TAG, "onComplete: got credentials but failed"+task.getException() );
                            }
                        }
                    });
        }catch(ApiException e){
            Log.e(TAG, "finishGoogleSignIn: google sign in error"+e.getMessage());
        }
    }

    public static boolean basicEmailAuth(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean passwordAuth(String password){
        return Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                password.length() >= 8 && password.length() <=16;
    }

    public void phoneAuth(String phone, Activity activity,
                                 PhoneAuthProvider.OnVerificationStateChangedCallbacks callback){

        options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber("+91"+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callback)
                .build();

        verifyPhone();
        verificationInProcess = true;

    }

    public void verifyPhone(){
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}
