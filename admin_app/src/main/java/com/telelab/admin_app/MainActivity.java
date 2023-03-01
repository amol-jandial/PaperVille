package com.telelab.admin_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getPapers();
    }

    private void getPapers() {
        GetPaperLinks task = new GetPaperLinks(this, this);
        task.execute();
    }

    private void init(){
        recyclerView = findViewById(R.id.recycler_view);
    }

    private static class GetPaperLinks extends AsyncTask<Void, Void, ArrayList<Paper>>{
        ProgressDialog progressDialog;
        Context context;
        private WeakReference<MainActivity> weakReference;

        public GetPaperLinks(Context context, MainActivity activity){
            this.context = context;
            weakReference = new WeakReference<>(activity);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Getting papers", "please wait...", true);
        }

        @Override
        protected ArrayList<Paper> doInBackground(Void... voids) {
            ArrayList<Paper> papers = new ArrayList<>();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("Classes").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                    documentSnapshot.getReference().collection("Subjects").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(QueryDocumentSnapshot paper : task.getResult()){
                                                            Paper testPaper = paper.toObject(Paper.class);
                                                            Log.d(TAG, "onComplete: "+testPaper.getUri());
                                                            papers.add(paper.toObject(Paper.class));
                                                        }
                                                    }else{
                                                        Log.e(TAG, "onComplete: "+task.getException());
                                                    }
                                                }
                                            });
                                }
                            }else{
                                Log.e(TAG, "onComplete: "+task.getException());
                            }
                        }
                    });
            return papers;
        }


        @Override
        protected void onPostExecute(ArrayList<Paper> papers) {
            progressDialog.dismiss();
            VerifyRecyclerViewAdapter adapter = new VerifyRecyclerViewAdapter(papers);
            MainActivity mainActivity = weakReference.get();
            if(mainActivity == null || mainActivity.isFinishing()) return;
            Log.d(TAG, "onPostExecute: "+papers);
            mainActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mainActivity.recyclerView.setAdapter(adapter);

        }
    }
}