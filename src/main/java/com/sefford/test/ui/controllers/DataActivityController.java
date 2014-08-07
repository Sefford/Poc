package com.sefford.test.ui.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.ListView;

import com.sefford.brender.adapters.RendererAdapter;
import com.sefford.brender.interfaces.Renderable;
import com.sefford.brender.interfaces.RendererBuilder;
import com.sefford.kor.retrofit.strategies.CacheExecutionStrategy;
import com.sefford.kor.retrofit.strategies.StandardNetworkRequestStrategy;
import com.sefford.test.R;
import com.sefford.test.core.BusManager;
import com.sefford.test.core.Logger;
import com.sefford.test.core.model.repos.MovieDiskRepository;
import com.sefford.test.core.networking.DataApi;
import com.sefford.test.core.networking.provider.DataProvider;
import com.sefford.test.core.networking.requests.UpcomingRequest;
import com.sefford.test.core.responses.UpcomingError;
import com.sefford.test.core.responses.UpcomingResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Activity controller extracts all the business view logic from the Activity (or fragment) itself
 * so the code becomes injectable, testable and lightweights the Activity, which becomes testable too.
 */
public class DataActivityController {

    protected final DataProvider provider;
    protected final BusManager manager;
    protected final RendererBuilder builder;
    protected final DataApi api;
    protected final Logger logger;
    protected final MovieDiskRepository repository;
    protected final ConnectivityManager connectivityManager;

    protected final List<Renderable> data = new ArrayList<Renderable>();

    protected ListView lvData;
    protected RendererAdapter adapter;
    protected View pbLoading;
    protected Context context;

    public DataActivityController(DataProvider provider, BusManager manager, RendererBuilder builder, DataApi api, Logger logger, MovieDiskRepository repository, ConnectivityManager connectivityManager) {
        this.provider = provider;
        this.manager = manager;
        this.builder = builder;
        this.api = api;
        this.logger = logger;
        this.repository = repository;
        this.connectivityManager = connectivityManager;
    }


    public void onCreate() {
        // Refactoring this a little, could make the request injectable too, but we'll leave it for now
        UpcomingRequest upcomingRequest = new UpcomingRequest(api, repository);
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            provider.executeOperation(new StandardNetworkRequestStrategy<UpcomingResponse, UpcomingError>(manager, logger, upcomingRequest));
        } else {
            provider.executeOperation(new CacheExecutionStrategy<UpcomingResponse, UpcomingError>(manager, logger, upcomingRequest));
        }
    }

    public void onResume(Context context, View view) {
        this.context = context;
        manager.register(this);
        lvData = (ListView) view.findViewById(R.id.lv_data);
        pbLoading = view.findViewById(R.id.pb_loading);
        lvData.setEmptyView(pbLoading);
    }

    public void onPause() {
        manager.unregister(this);
    }

    public void onDestroy() {
        context = null;
    }

    public void onEventMainThread(UpcomingResponse response) {
        data.addAll(response.getResults());
        adapter = new RendererAdapter(context, data, builder, null, null);
        lvData.setAdapter(adapter);
    }
}
