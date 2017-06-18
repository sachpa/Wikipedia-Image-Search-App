package com.microsoft.quicksearch;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.microsoft.quicksearch.contracts.SearchContract;
import com.microsoft.quicksearch.presenter.SearchPresenter;

/**
 * Provides dependencies. External dependencies should be added here to make junit simplified.
 */
public class Provider {

    private static Provider sProvider = new Provider();

    @VisibleForTesting
    public static void set(Provider provider) {
        sProvider = provider;
    }

    public static Provider get() {
        return sProvider;
    }

    public Utils getUtils() {
        return new Utils();
    }

    public SearchContract.Presenter getSearchPresenter(Context context, SearchContract.View view) {
        return new SearchPresenter(context, view);
    }
}
