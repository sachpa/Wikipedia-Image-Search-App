package com.microsoft.quicksearch.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ProgressBar;

import com.microsoft.outlookassignment.R;
import com.microsoft.quicksearch.MockProvider;
import com.microsoft.quicksearch.Provider;
import com.microsoft.quicksearch.contracts.SearchContract;
import com.microsoft.quicksearch.contracts.SearchContract.Presenter;
import com.microsoft.quicksearch.model.ResultData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.microsoft.quicksearch.view.SearchActivity.TAG_FRAGMENT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    private Context mContext;
    private MockProvider mMockProvider;
    private SearchContract.View mMockedView;

    @Rule
    public ActivityTestRule<SearchActivity> mRule = new ActivityTestRule<>(SearchActivity.class,true,false);

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getContext();
        mMockedView = Mockito.mock(SearchContract.View.class);
        mMockProvider = new MockProvider();
        Provider.set(mMockProvider);
    }

    @After
    public void tearDown() {
        // Verify mockito usage.
        // If not done after every test, next test case involving mockito will fail even though it does not have any issue.
        // It will be difficult to debug issue in that case.
        Mockito.validateMockitoUsage();
    }

    @Test
    public void test_searchString() {
        // ---------- Given -----------
        final Presenter searchPresenter = mMockProvider.getSearchPresenter(mContext, mMockedView);
        doNothing().when(searchPresenter).search(any(String.class));
        final String searchString = "abc";

        // ---------- When ------------
        mRule.launchActivity(null);
        onView(withId(R.id.autoCompleteTextView)).perform(ViewActions.typeText(searchString));

        // ---------- Then ------------
        verify(searchPresenter, atLeastOnce()).search(eq(searchString));
    }

    @Test
    public void test_showMessageCallback() {
        // ---------- Given -----------
        final Presenter searchPresenter = mMockProvider.getSearchPresenter(mContext, mMockedView);
        doNothing().when(searchPresenter).search(any(String.class));

        // ---------- When ------------
        mRule.launchActivity(null);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
                new Runnable() {
                    @Override
                    public void run() {
                        getSearchFragment().showMessage(R.string.message_nothing_to_show);
                    }
                }
        );

        // ---------- Then ------------
        onView(withId(R.id.progress_bar))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.result_recycler_view))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.msg_to_user))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void test_searchStartCallback() {
        // ---------- Given -----------
        final Presenter searchPresenter = mMockProvider.getSearchPresenter(mContext, mMockedView);
        doNothing().when(searchPresenter).search(any(String.class));

        // ---------- When ------------
        mRule.launchActivity(null);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
                new Runnable() {
                    @Override
                    public void run() {
                        getSearchFragment().onSearchStart();
                        // Remove animation of progress bar when indeterminate. Otherwise, Espresso keeps on waiting for device idle state.
                        ((ProgressBar) mRule.getActivity().findViewById(R.id.progress_bar)).setIndeterminate(false);
                    }
                }
        );

        // ---------- Then ------------
        onView(withId(R.id.progress_bar))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.result_recycler_view))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.msg_to_user))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void test_showResults() {
        // ---------- Given -----------
        final Presenter searchPresenter = mMockProvider.getSearchPresenter(mContext, mMockedView);
        doNothing().when(searchPresenter).search(any(String.class));

        // ---------- When ------------
        mRule.launchActivity(null);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(
                new Runnable() {
                    @Override
                    public void run() {
                        getSearchFragment().showResults(new ArrayList<ResultData>());
                    }
                }
        );

        // ---------- Then ------------
        onView(withId(R.id.progress_bar))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.result_recycler_view))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.msg_to_user))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    private SearchFragment getSearchFragment() {
        return (SearchFragment) mRule.getActivity()
                .getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
