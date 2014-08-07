package com.sefford.test.ui.controllers;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

import com.sefford.brender.interfaces.RendererBuilder;
import com.sefford.kor.retrofit.strategies.CacheExecutionStrategy;
import com.sefford.kor.retrofit.strategies.StandardNetworkRequestStrategy;
import com.sefford.test.R;
import com.sefford.test.core.BusManager;
import com.sefford.test.core.Logger;
import com.sefford.test.core.model.Movie;
import com.sefford.test.core.model.repos.MovieDiskRepository;
import com.sefford.test.core.networking.DataApi;
import com.sefford.test.core.networking.provider.DataProvider;
import com.sefford.test.core.responses.UpcomingError;
import com.sefford.test.core.responses.UpcomingResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * These tests illustrate how easy is to test a controller
 */
@RunWith(RobolectricTestRunner.class)
public class DataActivityControllerTest {

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private MovieDiskRepository repository;
    @Mock
    private Logger logger;
    @Mock
    private DataApi api;
    @Mock
    private RendererBuilder builder;
    @Mock
    private BusManager manager;
    @Mock
    private DataProvider provider;
    @Mock
    private UpcomingResponse response;

    private DataActivityController controller;

    private View view;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        controller = new DataActivityController(provider, manager, builder, api, logger, repository, connectivityManager);
        controller.onCreate();
        view = LayoutInflater.from(Robolectric.application).inflate(R.layout.activity_data, null, false);
    }

    @Test
    public void testOnCreateWithNoConnectivity() throws Exception {
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);

        verify(provider, times(1)).executeOperation(any(new CacheExecutionStrategy<UpcomingResponse, UpcomingError>(null, null, null).getClass()));
    }

    @Test
    public void testOnCreateWithNoConnection() throws Exception {
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnected()).thenReturn(false);

        verify(provider, times(1)).executeOperation(any(new CacheExecutionStrategy<UpcomingResponse, UpcomingError>(null, null, null).getClass()));
    }


    @Test
    public void testOnCreate() throws Exception {
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnected()).thenReturn(true);

        verify(provider, times(1)).executeOperation(any(new StandardNetworkRequestStrategy<UpcomingResponse, UpcomingError>(null, null, null).getClass()));
    }

    @Test
    public void testOnResume() throws Exception {
        controller.onResume(Robolectric.application, view);

        assertNotNull(controller.context);
        assertNotNull(controller.lvData);
        assertNotNull(controller.pbLoading);
        verify(manager, times(1)).register(controller);
    }

    @Test
    public void testOnPause() throws Exception {
        controller.onPause();

        verify(manager, times(1)).unregister(controller);
    }

    @Test
    public void testOnDestroy() throws Exception {
        controller.onDestroy();

        assertNull(controller.context);
    }

    @Test
    public void testOnEventMainThread() throws Exception {
        when(response.getResults()).thenReturn(new ArrayList<Movie>());

        controller.onResume(Robolectric.application, view);
        controller.onEventMainThread(response);

        assertNotNull(controller.adapter);
        assertEquals(controller.lvData.getAdapter(), controller.adapter);
    }
}