package com.example.collection.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class DBUtil {
    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
