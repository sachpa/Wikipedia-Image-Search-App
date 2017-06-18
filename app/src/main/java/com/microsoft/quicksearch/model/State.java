package com.microsoft.quicksearch.model;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Stores state of search fragment for restoration
 */
public class State {

    @Retention(SOURCE)
    @IntDef({NO_CONTENT, SHOWING_MESSAGE, SEARCHING, SHOWING_RESULTS})
    @interface DataState {}
    public static final int NO_CONTENT = -1;
    public static final int SHOWING_MESSAGE = 0;
    public static final int SEARCHING = 1;
    public static final int SHOWING_RESULTS = 2;

    private @DataState int mState = NO_CONTENT;
    private @StringRes int mMessageId;
    private String mSearchString;
    private List<ResultData> mResultDataList;

    public void setState(@DataState int state) {
        mState = state;
    }

    public void setMessage(@StringRes int messageId) {
        mMessageId = messageId;
    }

    public void setSearchString(String searchString) {
        mSearchString = searchString;
    }

    public void setResults(List<ResultData> resultDataList) {
        mResultDataList = resultDataList;
    }

    public int getState() {
        return mState;
    }

    public int getMessageId() {
        return mMessageId;
    }

    public String getSearchString() {
        return mSearchString;
    }

    public List<ResultData> getResultDataList() {
        return mResultDataList;
    }
}