package com.microsoft.quicksearch.model;

import android.support.annotation.StringRes;

import com.microsoft.outlookassignment.R;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StateTest {

    @Test
    public void test_StateData() {
        // ---------- Given -----------
        final State state = new State();
        final @State.DataState int dataState = State.NO_CONTENT;
        final @StringRes int msgId = R.string.error_no_network;
        final String searchSearch = "test";
        final List<ResultData> results = new ArrayList<>();

        // ---------- When -----------
        state.setState(dataState);
        state.setMessage(msgId);
        state.setSearchString(searchSearch);
        state.setResults(results);

        // ---------- Then -----------
        assertEquals(dataState, state.getState());
        assertEquals(msgId, state.getMessageId());
        assertEquals(searchSearch, state.getSearchString());
        assertEquals(results, state.getResultDataList());
    }
}
