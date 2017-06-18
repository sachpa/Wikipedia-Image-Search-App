package com.microsoft.quicksearch.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.outlookassignment.R;
import com.microsoft.quicksearch.Provider;
import com.microsoft.quicksearch.contracts.SearchContract;
import com.microsoft.quicksearch.model.ResultData;
import com.microsoft.quicksearch.model.State;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Performs network query for search string. Notifies view with appropriate message/result.
 */
public class SearchPresenter
        implements SearchContract.Presenter, Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = "SearchPresenter";
    private static final String REQUEST_TAG = "wikipedia_search";
    private static final String SEARCH_URL = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=500&pilimit=50&generator=prefixsearch&gpssearch=";

    private final Context mContext;
    private RequestQueue mQueue;
    private final SearchContract.View mView;
    private final State mViewState;

    public SearchPresenter(@NonNull Context context, @NonNull SearchContract.View view) {
        mContext = context;
        mView = view;
        mViewState = new State();
    }

    @Override
    public void search(String searchString) {
        Log.d(TAG, "search for " + searchString);
        initializeRequestQueue();
        mQueue.cancelAll(REQUEST_TAG);
        if (searchString.length() < 3) {
            if (searchString.isEmpty()) {
                showMessage(R.string.message_start_search_empty);
            } else {
                showMessage(R.string.message_start_search_at_least_three);
            }
            return;
        }
        if (!Provider.get().getUtils().isNetworkAvailable(mContext)) {
            showMessage(R.string.error_no_network);
            return;
        }
        onSearchStart(searchString);
        final String searchUrl = SEARCH_URL + searchString;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl, this, this);
        addRequest(stringRequest);
    }

    private void initializeRequestQueue() {
        if (mQueue == null) {
            Log.d(TAG, "Initializing request queue");
            mQueue = Volley.newRequestQueue(mContext);
        }
    }

    @VisibleForTesting
    protected void addRequest(StringRequest stringRequest) {
        stringRequest.setTag(REQUEST_TAG);
        mQueue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (Provider.get().getUtils().isNetworkAvailable(mContext)) {
            showMessage(R.string.error_something_went_wrong);
        } else {
            showMessage(R.string.error_no_network);
        }
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, response);
        final List<ResultData> list;
        try {
            list = parseResponse(response);
        } catch (JSONException ex) {
            Log.e(TAG, "Unable to parse response " + ex);
            showMessage(R.string.error_something_went_wrong);
            return;
        }

        if (list.isEmpty()) {
            showMessage(R.string.message_nothing_to_show);
        } else {
            showResults(list);
        }
    }

    private List<ResultData> parseResponse(String response) throws JSONException {
        final List<ResultData> list = new ArrayList<>();
        final JSONObject root = new JSONObject(response);

        if (root.has("query") && root.getJSONObject("query").has("pages")) {
            final JSONObject pages = root.getJSONObject("query").getJSONObject("pages");
            final Iterator<String> iterator = pages.keys();
            while(iterator.hasNext()) {
                final String pageId = iterator.next();
                final String title = pages.getJSONObject(pageId).getString("title");
                if (pages.getJSONObject(pageId).has("thumbnail")) {
                    final JSONObject imageThumbnail = pages.getJSONObject(pageId).getJSONObject("thumbnail");
                    final String url1 = imageThumbnail.getString("source");
                    final ResultData data = new ResultData();
                    data.setTitle(title);
                    data.setSource(url1);
                    list.add(data);
                }
            }
        }
        return list;
    }

    private void showMessage(@StringRes int messageId) {
        mView.showMessage(messageId);
        mViewState.setState(State.SHOWING_MESSAGE);
        mViewState.setMessage(messageId);
    }

    private void onSearchStart(String searchString) {
        mView.onSearchStart();
        mViewState.setState(State.SEARCHING);
        mViewState.setSearchString(searchString);
    }

    private void showResults(List<ResultData> resultDataList) {
        mView.showResults(resultDataList);
        mViewState.setState(State.SHOWING_RESULTS);
        mViewState.setResults(resultDataList);
    }

    @Override
    public void restoreState() {
        switch (mViewState.getState()) {
            case State.SHOWING_MESSAGE:
                showMessage(mViewState.getMessageId());
                break;
            case State.SEARCHING:
                search(mViewState.getSearchString());
                break;
            case State.SHOWING_RESULTS:
                showResults(mViewState.getResultDataList());
                break;
        }
    }

    @VisibleForTesting
    State getViewState() {
        return mViewState;
    }

    @Override
    public void stop() {
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
            mQueue.stop();
            mQueue = null;
        }
    }
}
