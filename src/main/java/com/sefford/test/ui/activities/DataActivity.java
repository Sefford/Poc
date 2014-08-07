package com.sefford.test.ui.activities;

import android.app.Activity;
import android.os.Bundle;

import com.sefford.test.R;
import com.sefford.test.ui.application.MainApplication;
import com.sefford.test.ui.controllers.DataActivityController;

import javax.inject.Inject;

/**
 * Activity that handles a list of movies
 */
public class DataActivity extends Activity {

    @Inject
    protected DataActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ((MainApplication) getApplication()).inject(this);
        controller.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.onResume(this, findViewById(android.R.id.content));
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }
}
