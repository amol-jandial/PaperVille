package com.telelab.paperville;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.telelab.paperville.models.Class;
import com.telelab.paperville.models.Subject;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";
    private DatabaseHandler databaseHandler;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        if(sharedPreferences.getInt("firstTimeUser", 0) == 0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("firstTimeUser", 1);
            editor.apply();
           HelperMethods.startNewActivity(this, RegistrationActivity.class);
        }else{
            fetchData();
        }
    }

    private void fetchData(){
       if(isUserLoggedIn()){
           //fetch data then start main activity
           databaseHandler = new DatabaseHandler(this);
           databaseHandler.getUserData();

       }else{
           userNotLoggedIn();
       }
    }

    private void userNotLoggedIn() {
        HelperMethods.startNewActivity(this, LoginActivity.class);
    }

    private boolean isUserLoggedIn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }


}