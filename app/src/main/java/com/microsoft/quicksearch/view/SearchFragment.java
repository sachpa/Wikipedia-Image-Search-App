package com.microsoft.quicksearch.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.microsoft.outlookassignment.R;
import com.microsoft.quicksearch.Provider;
import com.microsoft.quicksearch.adapter.ResultAdapter;
import com.microsoft.quicksearch.contracts.SearchContract;
import com.microsoft.quicksearch.model.ResultData;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View, TextWatcher {

    private static final String TAG = "SearchFragment";

    private ResultAdapter mResultAdapter;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private TextView mMessageToUser;
    private SearchContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Get presenter here so that data can be persisted across re-creation of activity but not fragment.
        mPresenter = Provider.get().getSearchPresenter(getContext().getApplicationContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler_view);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMessageToUser = (TextView) view.findViewById(R.id.msg_to_user);
        mResultAdapter = new ResultAdapter(getContext(), new ArrayList<ResultData>());
        mRecyclerView.setAdapter(mResultAdapter);

        // Important : Adjust width of gridLayout only when fragment view is fully drawn.
        // It handles following real estate use cases :
        // 1. Fragment's real estate is fixed by Activity
        // 2. Fragment's real estate gets changed during runtime when device is rotated
        // 3. Fragment's real estate gets changed due to Multi-Window resizing (Android N and above).
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridLayoutManager.setSpanCount(getSpanCount(view));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AutoCompleteTextView)getView().findViewById(R.id.autoCompleteTextView))
                .addTextChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AutoCompleteTextView)getView().findViewById(R.id.autoCompleteTextView))
                .removeTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPresenter.search(s.toString());
    }

    private int getSpanCount(@NonNull View view) {
        final float density = getResources().getDisplayMetrics().density;
        final float dpWidth = view.getWidth() / density;
        return Math.round(dpWidth / 100); // total width divided by search result image width.
    }

    @Override
    public void showResults(List<ResultData> results) {
        Log.i(TAG, "showResults: " + results);
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mResultAdapter.setItems(results);
        mMessageToUser.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(@StringRes int stringId) {
        progressBar.setVisibility(View.GONE);
        mResultAdapter.setItems(null);
        mRecyclerView.setVisibility(View.GONE);
        mMessageToUser.setVisibility(View.VISIBLE);
        mMessageToUser.setText(getString(stringId));
    }

    @Override
    public void onSearchStart() {
        progressBar.setVisibility(View.VISIBLE);
        mResultAdapter.setItems(null);
        mRecyclerView.setVisibility(View.GONE);
        mMessageToUser.setVisibility(View.GONE);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.restoreState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.stop();
    }

    // Do nothing methods which must be implemented.
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}