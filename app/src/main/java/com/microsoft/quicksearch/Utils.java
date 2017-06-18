package com.microsoft.quicksearch;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Class with utility methods.
 *
 * Note : Utility methods are specifically not made static for testing purpose.
 * Using libraries like PowerMock has its own disadvantage.
 *
 * @see Provider
 */
public class Utils {

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
