package com.sefford.test.core.modules;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.GsonBuilder;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.sefford.brender.builder.Builder;
import com.sefford.brender.interfaces.Postable;
import com.sefford.brender.interfaces.Renderer;
import com.sefford.brender.interfaces.RendererBuilder;
import com.sefford.brender.interfaces.RendererFactory;
import com.sefford.test.core.BusManager;
import com.sefford.test.core.Logger;
import com.sefford.test.core.deserializers.UpcomingRequestDeserializer;
import com.sefford.test.core.model.repos.MovieDiskRepository;
import com.sefford.test.core.networking.CustomErrorHandler;
import com.sefford.test.core.networking.DataApi;
import com.sefford.test.core.networking.RetrofitGsonConverter;
import com.sefford.test.core.networking.provider.DataProvider;
import com.sefford.test.core.responses.UpcomingResponse;
import com.sefford.test.ui.activities.DataActivity;
import com.sefford.test.ui.application.MainApplication;
import com.sefford.test.ui.controllers.DataActivityController;
import com.sefford.test.ui.renderers.MovieRenderer;

import java.io.File;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Dagger Dependency Injection Module
 */
@Module(
        // The Module is incomplete, as we require the Application context for the DataProvider
        complete = false,
        library = true,
        injects = {
                MainApplication.class,
                DataActivity.class
        }
)
public class ProductionModule {

    private static final String API_ENDPOINT = "https://api.themoviedb.org/3/";
    /**
     * Application context
     */
    protected Context context;

    /**
     * Creates new Module with the Application Context
     *
     * @param context Application Context
     */
    public ProductionModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public EventBus provideBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public BusManager provideBusManager(EventBus bus) {
        return new BusManager(bus);
    }

    @Provides
    @Singleton
    public DataApi provideDataApi(RetrofitGsonConverter converter) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam(DataApi.API_PARAM, DataApi.API_KEY);
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .setClient(new OkClient())
                .setExecutors(Executors.newCachedThreadPool(), Executors.newCachedThreadPool())
                .setErrorHandler(new CustomErrorHandler())
                .setLogLevel(RestAdapter.LogLevel.HEADERS)
                .setConverter(converter)
                .setRequestInterceptor(requestInterceptor)
                .build();
        return restAdapter.create(DataApi.class);
    }

    @Provides
    @Singleton
    public DataProvider provideDataProvider() {
        return new DataProvider(new JobManager(context, new Configuration.Builder(context)
                .id("DataProvider")
                .minConsumerCount(3)
                .maxConsumerCount(4)
                .loadFactor(3)
                .build()));
    }

    @Provides
    public RendererBuilder provideRendererBuilder() {
        return new Builder(new RendererFactory() {
            @Override
            public Renderer getRenderer(int i, Postable postable, Object o) {
                return new MovieRenderer();
            }
        });
    }

    @Provides
    public Logger provideLogger() {
        return new Logger();
    }

    @Provides
    public RetrofitGsonConverter provideGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UpcomingResponse.class, new UpcomingRequestDeserializer());
        return new RetrofitGsonConverter(builder.create());
    }

    @Provides
    @Singleton
    public MovieDiskRepository provideDiskRepository(Context context) {
        return new MovieDiskRepository(null, new File(context.getCacheDir() + "/movies"));
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    public DataActivityController provideDataActivityController(DataProvider provider, BusManager manager,
                                                                RendererBuilder builder, DataApi api,
                                                                Logger logger, MovieDiskRepository repository,
                                                                ConnectivityManager connectivityManager) {
        // With DI this method becomes sort of Factory method. Extracting DataActivityController to an
        // interface could allow us to runtime - inject an instance of a DataActivityInterface
        // to handle different configurations and formats
        return new DataActivityController(provider, manager, builder, api, logger, repository, connectivityManager);
    }

}