package com.microsoft.quicksearch.contracts;

import android.support.annotation.StringRes;

import com.microsoft.quicksearch.model.ResultData;

import java.util.List;

/**
 * Contract between view and presenter for search
 */
public interface SearchContract {

    interface View {
        void showResults(List<ResultData> results);
        void onSearchStart();
        void showMessage(@StringRes int stringid);
    }

    interface Presenter {
        void search(String searchString);
        void restoreState();
        void stop();
    }
}
