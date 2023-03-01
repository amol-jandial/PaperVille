package com.telelab.paperville.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.telelab.paperville.Adapters.HomeRecyclerViewAdapter;
import com.telelab.paperville.CustomOnClickListener;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;
import com.telelab.paperville.models.Subject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements CustomOnClickListener {
    private static final String TAG = "HomeFragment";
    private View view;
    private HomeRecyclerViewAdapter adapter;
    private ArrayList<Subject> subjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    public HomeFragment(ArrayList<Subject> subjects){
        this.subjects = subjects;
        Log.d(TAG, "HomeFragment: "+this.subjects);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.d(TAG, "onStart: "+subjects);
        adapter = new HomeRecyclerViewAdapter(getActivity(), subjects, recyclerView, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onButtonClicked(View view, int position) {
        UploadPaperFragmentDialog dialog = new UploadPaperFragmentDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "uploadDialog");
    }
}
