package com.microsoft.quicksearch.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultDataTest {

    @Test
    public void test_ResultData() {
        // ---------- Given -----------
        final ResultData resultData = new ResultData();
        final String title = "title";
        final String source = "url";

        // ---------- When -----------
        resultData.setTitle(title);
        resultData.setSource(source);

        // ---------- Then -----------
        assertEquals(title, resultData.getTitle());
        assertEquals(source, resultData.getSource());
    }
}