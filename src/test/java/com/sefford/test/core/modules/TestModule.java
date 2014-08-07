package com.sefford.test.core.modules;

import android.content.Context;
import android.net.ConnectivityManager;

import com.sefford.brender.interfaces.RendererBuilder;
import com.sefford.test.core.BusManager;
import com.sefford.test.core.Logger;
import com.sefford.test.core.model.repos.MovieDiskRepository;
import com.sefford.test.core.networking.DataApi;
import com.sefford.test.core.networking.RetrofitGsonConverter;
import com.sefford.test.core.networking.provider.DataProvider;
import com.sefford.test.ui.activities.DataActivity;
import com.sefford.test.ui.application.TestMainApplication;
import com.sefford.test.ui.controllers.DataActivityController;

import org.robolectric.Robolectric;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

import static org.mockito.Mockito.mock;

/**
 * Dagger Dependency Test Injection Module
 */
@Module(
        // The Module is incomplete, as we require the Application context for the DataProvider
        complete = false,
        library = true,
        injects = {
                TestMainApplication.class,
                DataActivity.class
        }
)
public class TestModule {

    @Provides
    @Singleton
    Context provideContext() {
        return Robolectric.application;
    }

    @Provides
    @Singleton
    public EventBus provideBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public BusManager provideBusManager() {
        return mock(BusManager.class);
    }

    @Provides
    @Singleton
    public DataApi provideDataApi() {
        return mock(DataApi.class);
    }

    @Provides
    @Singleton
    public DataProvider provideDataProvider() {
        return mock(DataProvider.class);
    }

    @Provides
    public RendererBuilder provideRendererBuilder() {
        return mock(RendererBuilder.class);
    }

    @Provides
    public Logger provideLogger() {
        return mock(Logger.class);
    }

    @Provides
    public RetrofitGsonConverter provideGsonConverter() {
        return mock(RetrofitGsonConverter.class);
    }

    @Provides
    @Singleton
    public MovieDiskRepository provideDiskRepository() {
        return mock(MovieDiskRepository.class);
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager() {
        return mock(ConnectivityManager.class);
    }

    @Provides
    public DataActivityController provideDataActivityController() {
        return mock(DataActivityController.class);
    }

}