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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.telelab.paperville.AnimationHelper;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;

import java.util.regex.Pattern;

public class InformationFragment extends Fragment implements
        View.OnClickListener,
        TextWatcher,
        TextView.OnEditorActionListener,
        RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "informationFragment";
    private View view;
    private View rootView;
    private Context context;
    private OnInfoVerifiedListener mListener;
    private AppCompatButton buttonBack, nextButton, continueButton;
    private TextInputLayout inputLayout, inputLayoutUsername;
    private TextInputEditText editTextPhone, editTextUsername;
    private boolean verified = false;
    private Spinner spinnerBranch, spinnerSemester;
    private String branch, semester, section, username, phone;
    private RadioGroup radioGroup;

    public InformationFragment(Context context, View rootView, OnInfoVerifiedListener listener) {
        this.context = context;
        this.rootView = rootView;
        mListener = listener;
    }

    public interface OnInfoVerifiedListener {
        void OnInfoVerifiedListener(String phone, String username, String branch, String semester, String section);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.information_fragment, container, false);
        init();
        setListeners();
        return view;
    }

    private void init() {
        buttonBack = view.findViewById(R.id.button_info_back);
        inputLayout = view.findViewById(R.id.text_input_layout_info);
        editTextPhone = view.findViewById(R.id.editTextPhone_info);
        nextButton = view.findViewById(R.id.button_info_next);
        continueButton = rootView.findViewById(R.id.button_registration);
        inputLayoutUsername = view.findViewById(R.id.text_input_layout_info_username);
        editTextUsername = view.findViewById(R.id.edittext_username_info);
        radioGroup = view.findViewById(R.id.radioGroup);
        spinnerBranch = view.findViewById(R.id.spinner_branch);
        spinnerSemester = view.findViewById(R.id.spinner_semester);
        final ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(getActivity(),
                androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.branch_array));
        final ArrayAdapter<String> semesterAdapter = new ArrayAdapter<String>(getActivity(),
                androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.semester_array));
        spinnerBranch.setAdapter(branchAdapter);
        spinnerSemester.setAdapter(semesterAdapter);
    }

    private void setListeners() {
        buttonBack.setOnClickListener(this);
        editTextPhone.setOnEditorActionListener(this);
        editTextPhone.addTextChangedListener(this);
        editTextUsername.setOnEditorActionListener(this);
        editTextUsername.addTextChangedListener(this);
        nextButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        spinnerBranch.setOnItemSelectedListener(this);
        spinnerSemester.setOnItemSelectedListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        HelperMethods.onKeyboardDownListener(rootView, inputLayout);
        HelperMethods.onKeyboardDownListener(rootView, inputLayoutUsername);
    }

    private void popFragment() {
        FragmentHelper.popFragment(getParentFragmentManager(), "passwordFragment", 0);
        HelperMethods.prevText(R.id.text_password, R.id.text_info, context, rootView);
        HelperMethods.prevTimeline(R.id.second_circle, R.id.third_circle, R.id.second_line, context, rootView);
    }

    private void verifyPhone(String text) {

        if ((Pattern.compile("[0-9]").matcher(text).find()) && (text.length() >= 10 && text.length() <= 12)) {
            verified = Patterns.PHONE.matcher(text).matches();
            phone = text;
        } else {
            verified = false;
        }

        verified = Pattern.compile("[a-z]").matcher(text).find() ||
                Pattern.compile("[A-Z]").matcher(text).find();


        changeButtonState();

    }

    private void changeButtonState(){
        
        if(branch == null || semester == null || section == null || editTextPhone.getText() == null || editTextUsername.getText() == null){
            Log.d(TAG, "changeButtonState: "+branch);
            Log.d(TAG, "changeButtonState: "+semester);
            Log.d(TAG, "changeButtonState: "+section);
            Log.d(TAG, "changeButtonState: "+editTextPhone.getText());
            Log.d(TAG, "changeButtonState: "+editTextUsername.getText());

            verified = false;
        }
        if (verified) {
            HelperMethods.enableButton(nextButton, continueButton, getActivity());
        } else {
            HelperMethods.disableButton(nextButton, continueButton,"Fill Information To Continue", getActivity());

        }
    }

    private void changeToMainActivity() {
        if (verified) {
            continueButton.setText("Final Step");
            continueButton.setClickable(false);
            continueButton.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_registration, getActivity()));
            mListener.OnInfoVerifiedListener(editTextPhone.getText().toString(),
                    editTextUsername.getText().toString(), branch, semester, section);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        String selectedItem = adapterView.getItemAtPosition(i).toString();
        switch(spinner.getId()){
            case R.id.spinner_branch:
                if(i != 0){
                    branch = selectedItem;
                }
                break;

            case R.id.spinner_semester:
                if(i != 0){
                    semester = selectedItem;
                }
                break;
        }
        changeButtonState();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        verified = false;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(i == R.id.radio_button_1){
            section = "1";
        }else if(i == R.id.radio_button_2){
            section = "2";
        }
        verified = true;
        Log.d(TAG, "onCheckedChanged: "+section);
        changeButtonState();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_info_back:
                popFragment();
                break;

            case R.id.button_info_next:
                changeToMainActivity();
                break;

            case R.id.button_registration:
                changeToMainActivity();
                break;
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



        if (editable.length() != 0) {
            verifyPhone(editable.toString());
        } else {
            verified = false;
            HelperMethods.disableButton(nextButton, continueButton, "Fill Information To Continue", getActivity());
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }


}
