package com.telelab.paperville.fragments;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.telelab.paperville.AnimationHelper;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;

public class FragmentHelper{
    private static final String TAG = "FragmentHelper";


    public void setFragment(Fragment fragment, String name, String tag, FragmentManager fragmentManager){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right);
        transaction.replace(R.id.frame_layout_registration, fragment, tag);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public static void popFragment(FragmentManager fragmentManager, String name, int flag){
        fragmentManager.popBackStack(name, flag);
    }

    public void setHiddenFragment(Fragment fragment, String name, FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container_main, fragment, name)
                .hide(fragment)
                .commit();
    }

    public void setCurrentFragment(Fragment fragment, String name, FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container_main, fragment, name)
                .setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .commit();
    }

    public Fragment changeFragment(Fragment newFrag, Fragment oldFrag, FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .hide(oldFrag)
                .show(newFrag)
                .commit();
        return newFrag;
    }
}
