package com.telelab.paperville;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class SearchActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Intent.ACTION_SEARCH.equals(getIntent().getAction())){
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            performSearch(query);
        }
    }

    private void performSearch(String query){
            finish();
    }

}
