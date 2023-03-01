package com.telelab.paperville;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.telelab.paperville.fragments.EmailFragment;
import com.telelab.paperville.fragments.FragmentHelper;
import com.telelab.paperville.fragments.InformationFragment;
import com.telelab.paperville.fragments.PasswordFragment;
import com.telelab.paperville.fragments.PhoneVerificationFragment;
import com.telelab.paperville.models.Class;
import com.telelab.paperville.models.Student;
import com.telelab.paperville.models.Subject;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity implements EmailFragment.OnEmailVerifiedListener,
        PasswordFragment.OnPasswordVerifiedListener,
        InformationFragment.OnInfoVerifiedListener,
        PhoneVerificationFragment.OnPhoneVerificationListener {
    private static final String TAG = "RegistrationActivity";
    private Student student;
    private Class mClass;
    private View rootView;
    private FragmentContainerView fragmentContainerView;
    private Authentication authentication;
    private FragmentHelper fragmentHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init(){
        rootView = findViewById(R.id.root_view_registration);
        student = new Student();
        mClass = new Class();
        fragmentContainerView = findViewById(R.id.frame_layout_registration);
        authentication = new Authentication(student, this);
        fragmentHelper = new FragmentHelper();
        fragmentHelper.setFragment(new EmailFragment(this, rootView, this), "emailFragment", "0", getSupportFragmentManager());

    }


    @Override
    public void onEmailVerified(String email) {
        student.setEmail(email);
        fragmentHelper.setFragment(new PasswordFragment(this, rootView, this), "passwordFragment", "0", getSupportFragmentManager());
        updateProgression(R.id.text_email, R.id.text_password, R.id.first_circle, R.id.second_circle, R.id.first_line);
    }


    @Override
    public void onPasswordVerifiedListener(String password) {
        student.setPassword(password);
        fragmentHelper.setFragment(new InformationFragment(this, rootView, this), "infoFragment", "0", getSupportFragmentManager());
        updateProgression(R.id.text_password, R.id.text_info, R.id.second_circle, R.id.third_circle, R.id.second_line);
    }

    @Override
    public void OnInfoVerifiedListener(String phone, String username, String branch, String semester, String section) {
        student.setUsername(username);
        mClass.setBranch(branch);
        mClass.setSemester(semester);
        mClass.setSection(section);
        mClass.setStudents(new ArrayList<Student>());
        mClass.makeSubjects();
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userBranch", branch+semester);
        editor.apply();
        fragmentHelper.setFragment(new PhoneVerificationFragment(this, this, rootView, phone), "phoneFragment", "0",
                getSupportFragmentManager());

    }

    @Override
    public void onPhoneVerificationListener(String phone) {
        student.setPhone(phone);
        Log.d(TAG, "onPhoneVerificationListener: "+student.toString());
        authentication.upload(student, mClass, this);
    }

    private void updateProgression(int firstText, int secondText, int firstCircle, int nextCircle,
                                   int lineBetween) {
        HelperMethods.nextText(firstText, secondText, this, rootView);
        HelperMethods.nextTimeline(firstCircle, nextCircle, lineBetween, this, rootView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Authentication.RESULT_CODE_SIGNIN){
            authentication.finishGoogleSignIn(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        if(authentication.isVerificationInProcess()){
            authentication.verifyPhone();
        }
    }
}