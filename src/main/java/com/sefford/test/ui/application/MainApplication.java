package com.sefford.test.ui.application;

import android.app.Application;

import com.sefford.test.core.modules.ProductionModule;

import dagger.ObjectGraph;

/**
 * Main production application for the test
 */
public class MainApplication extends Application{


    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Dagger's Dependency injection object Graph
        objectGraph = initializeGraph();
        objectGraph.inject(this);
    }

    protected ObjectGraph initializeGraph() {
        return ObjectGraph.create(new ProductionModule(this));
    }

    public <T> void inject(T instance) {
        objectGraph.inject(instance);
    }
}
