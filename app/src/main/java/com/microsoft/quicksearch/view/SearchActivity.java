package com.microsoft.quicksearch.view;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.microsoft.outlookassignment.R;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    @VisibleForTesting
    static final String TAG_FRAGMENT = "search_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null) {
            Log.d(TAG, "onCreate: fragment needs to be created");
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.search_fragment_holder, new SearchFragment(), TAG_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}
