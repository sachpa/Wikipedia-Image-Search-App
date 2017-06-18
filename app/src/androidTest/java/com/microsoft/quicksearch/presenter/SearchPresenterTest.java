package com.microsoft.quicksearch.presenter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.microsoft.outlookassignment.R;
import com.microsoft.quicksearch.MockProvider;
import com.microsoft.quicksearch.Provider;
import com.microsoft.quicksearch.Utils;
import com.microsoft.quicksearch.contracts.SearchContract;
import com.microsoft.quicksearch.model.State;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class SearchPresenterTest {

    static class TestPresenter extends SearchPresenter {

        StringRequest mRequest;
        TestPresenter(Context context, SearchContract.View view) {
            super(context, view);
        }

        @Override
        protected void addRequest(StringRequest stringRequest) {
            mRequest = stringRequest;
        }

        public StringRequest getRequest() {
            return mRequest;
        }
    }

    private Context mContext;
    private TestPresenter mTestPresenter;
    private SearchContract.View mTestView;
    private MockProvider mMockProvider;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getContext();
        mTestView = mock(SearchContract.View.class);
        mTestPresenter = new TestPresenter(mContext, mTestView);
        mMockProvider = new MockProvider();
        Provider.set(mMockProvider);
    }

    @After
    public void tearDown() {
        mTestPresenter.stop();
        // Verify mockito usage.
        // If not done after every test, next test case involving mockito will fail even though it does not have any issue.
        // It will be difficult to debug it.
        Mockito.validateMockitoUsage();
    }

    @Test
    public void test_emptySearch() {
        // ---------- Given -----------
        final String searchString = "";

        // ---------- When -----------
        mTestPresenter.search(searchString);

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.message_start_search_empty);
        assertEquals(mTestPresenter.getViewState().getState(), State.SHOWING_MESSAGE);
    }

    @Test
    public void test_oneLetterSearch() {
        // ---------- Given -----------
        final String searchString = "a";

        // ---------- When -----------
        mTestPresenter.search(searchString);

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.message_start_search_at_least_three);
    }

    @Test
    public void test_noNetwork() {
        // ---------- Given -----------
        final String searchString = "abc";
        final Utils utils = mMockProvider.getUtils();
        doReturn(false).when(utils).isNetworkAvailable(any(Context.class));

        // ---------- When -----------
        mTestPresenter.search(searchString);

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.error_no_network);
    }

    @Test
    public void test_searchStart() {
        // ---------- Given -----------
        final String searchString = "abc";
        final Utils utils = mMockProvider.getUtils();
        doReturn(true).when(utils).isNetworkAvailable(any(Context.class));

        // ---------- When -----------
        mTestPresenter.search(searchString);

        // ---------- Then -----------
        assertNotNull(mTestPresenter.getRequest());
        verify(mTestView).onSearchStart();
    }

    @Test
    public void test_searchResultError_WithNetwork() {
        // ---------- Given -----------
        final Utils utils = mMockProvider.getUtils();
        doReturn(true).when(utils).isNetworkAvailable(any(Context.class));

        // ---------- When -----------
        mTestPresenter.onErrorResponse(Mockito.mock(VolleyError.class));

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.error_something_went_wrong);
    }

    @Test
    public void test_searchResultError_WithoutNetwork() {
        // ---------- Given -----------
        final Utils utils = mMockProvider.getUtils();
        doReturn(false).when(utils).isNetworkAvailable(any(Context.class));

        // ---------- When -----------
        mTestPresenter.onErrorResponse(Mockito.mock(VolleyError.class));

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.error_no_network);
    }

    @Test
    public void test_searchResultError_JsonFormat() {
        // ---------- Given -----------
        final Utils utils = mMockProvider.getUtils();
        doReturn(false).when(utils).isNetworkAvailable(any(Context.class));
        final String invalidJson = "{";

        // ---------- When -----------
        mTestPresenter.onResponse(invalidJson);

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.error_something_went_wrong);
    }

    @Test
    public void test_searchResult_NoImages() {
        // ---------- Given -----------
        final Utils utils = mMockProvider.getUtils();
        doReturn(false).when(utils).isNetworkAvailable(any(Context.class));
        final String invalidJson = "{}";

        // ---------- When -----------
        mTestPresenter.onResponse(invalidJson);

        // ---------- Then -----------
        verify(mTestView).showMessage(R.string.message_nothing_to_show);
    }

    @Test
    public void test_searchResult_WithImages() {
        // ---------- Given -----------
        final Utils utils = mMockProvider.getUtils();
        doReturn(false).when(utils).isNetworkAvailable(any(Context.class));
        final String validJson = "{'query' : { 'pages' : { '123' : { 'title' : 'hello', 'thumbnail' : { 'source' : 'image.url' } } } }}";

        // ---------- When -----------
        mTestPresenter.onResponse(validJson);

        // ---------- Then -----------
        verify(mTestView).showResults(any(List.class));
    }

}
