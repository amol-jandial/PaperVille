package com.telelab.paperville.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.card.MaterialCardView;
import com.telelab.paperville.CustomOnClickListener;
import com.telelab.paperville.R;
import com.telelab.paperville.models.Subject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRecyclerViewHolder>
    {

    private static final String TAG = "HomeRecyclerViewAdapter";
    private ArrayList<Subject> subjects = new ArrayList<Subject>();
    private Context context;
    private int previouslyExpanded = -1;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private final CustomOnClickListener mListener;

    public HomeRecyclerViewAdapter(Context context, ArrayList<Subject> subjects, RecyclerView recyclerView,
                                   CustomOnClickListener listener){
        this.context = context;
        this.subjects = subjects;
        this.recyclerView = recyclerView;
        mListener = listener;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_card, parent, false);
        return new HomeRecyclerViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean visibility = holder.expandedCardView.getVisibility() == View.VISIBLE;
                if(!visibility){
                    holder.itemView.setActivated(true);
                    holder.expandedCardView.setVisibility(View.VISIBLE);
                    if(previouslyExpanded != -1 && previouslyExpanded != holder.getAdapterPosition()){
                        recyclerView.findViewHolderForLayoutPosition(previouslyExpanded)
                                .itemView.setActivated(false);
                        recyclerView.findViewHolderForLayoutPosition(previouslyExpanded)
                                .itemView.findViewById(R.id.card_view_expanded_home)
                                .setVisibility(View.GONE);
                    }
                    previouslyExpanded = holder.getAdapterPosition();
                }else{
                    holder.itemView.setActivated(false);
                    holder.expandedCardView.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(recyclerView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (subjects != null && subjects.size() != 0 ) ? subjects.size() : 0;
    }





    public static class HomeRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "HomeRecyclerViewHolder";
        private WeakReference<CustomOnClickListener> listenerRef;
        MaterialCardView subjectCardView, expandedCardView;
        AppCompatButton buttonExpand, buttonBookmark, buttonAdd, buttonView, buttonDownload;
        AppCompatAutoCompleteTextView dropdownExam, dropdownPaper;

        public HomeRecyclerViewHolder(@NonNull View itemView, CustomOnClickListener listener) {
            super(itemView);
            subjectCardView = itemView.findViewById(R.id.card_view_home);
            expandedCardView = itemView.findViewById(R.id.card_view_expanded_home);
            buttonAdd = itemView.findViewById(R.id.button_add);
            buttonBookmark = itemView.findViewById(R.id.button_bookmark);
            buttonExpand = itemView.findViewById(R.id.button_expand);
            buttonView = itemView.findViewById(R.id.button_view);
            buttonDownload = itemView.findViewById(R.id.button_download);
            dropdownExam = itemView.findViewById(R.id.dropdown_select_exam);
            dropdownPaper = itemView.findViewById(R.id.dropdown_select_paper);
            listenerRef = new WeakReference<>(listener);
            buttonAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button_add:
                    getPaperPhotos(view);
            }
        }

        private void getPaperPhotos(View view){
            listenerRef.get().onButtonClicked(view, getAdapterPosition());

        }
    }
}
