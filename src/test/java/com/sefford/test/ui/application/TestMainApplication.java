package com.sefford.test.ui.application;

import com.sefford.test.core.modules.TestModule;

import dagger.ObjectGraph;

public class TestMainApplication extends MainApplication {

    @Override
    protected ObjectGraph initializeGraph() {
        return ObjectGraph.create(new TestModule());
    }
}