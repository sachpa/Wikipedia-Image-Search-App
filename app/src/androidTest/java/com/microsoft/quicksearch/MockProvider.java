package com.microsoft.quicksearch;

import android.content.Context;

import com.microsoft.quicksearch.contracts.SearchContract;
import com.microsoft.quicksearch.contracts.SearchContract.Presenter;

import static org.mockito.Mockito.mock;

/**
 * Provides mocked dependencies.
 */
public class MockProvider extends Provider {

    private Utils mMockUtils;
    private Presenter mMockSearchPresenter;

    @Override
    public Utils getUtils() {
        if (mMockUtils == null) {
            mMockUtils = mock(Utils.class);
        }
        return mMockUtils;
    }

    @Override
    public Presenter getSearchPresenter(Context context, SearchContract.View view) {
        if (mMockSearchPresenter == null) {
            mMockSearchPresenter = mock(Presenter.class);
        }
        return mMockSearchPresenter;
    }
}
