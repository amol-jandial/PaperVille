package com.telelab.admin_app;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VerifyRecyclerViewAdapter extends RecyclerView.Adapter<VerifyRecyclerViewAdapter.VerifyRecyclerViewHolder>{

    private static final String TAG = "VerifyRecyclerViewAdapt";
    private ArrayList<Paper> papers;

    public VerifyRecyclerViewAdapter(ArrayList<Paper> papers){
        this.papers = papers;
    }


    @NonNull
    @Override
    public VerifyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verify_layout, parent, false);
        return new VerifyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerifyRecyclerViewHolder holder, int position) {
        for(Paper paper : papers){
            holder.pdfUri.setText(paper.getUri());
        }
    }

    @Override
    public int getItemCount() {
        return (papers != null && !papers.isEmpty())? papers.size() : 0;
    }

    public static class VerifyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pdfUri;
        Button verifyButton, rejectButton;

        public VerifyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfUri = itemView.findViewById(R.id.text_view_pdf_uri);
            verifyButton = itemView.findViewById(R.id.button_verify);
            rejectButton = itemView.findViewById(R.id.button_reject);
            pdfUri.setOnClickListener(this);
            verifyButton.setOnClickListener(this);
            rejectButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.text_view_pdf_uri:
                    openPdf();
                    break;

                case R.id.button_verify:
                    verifyPdf();
                    break;

                case R.id.button_reject:
                    rejectPdf();
                    break;
            }
        }

        private void rejectPdf() {
        }

        private void verifyPdf() {
        }

        private void openPdf() {
        }
    }
}
