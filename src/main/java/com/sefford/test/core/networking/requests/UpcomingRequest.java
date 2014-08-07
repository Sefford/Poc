package com.sefford.test.core.networking.requests;

import com.sefford.kor.requests.interfaces.CacheRequest;
import com.sefford.kor.retrofit.interfaces.RetrofitRequest;
import com.sefford.test.core.model.Movie;
import com.sefford.test.core.model.repos.MovieDiskRepository;
import com.sefford.test.core.networking.DataApi;
import com.sefford.test.core.responses.UpcomingError;
import com.sefford.test.core.responses.UpcomingResponse;

import java.util.ArrayList;

import retrofit.RetrofitError;

/**
 * Upcoming Request will query for the upcoming movies from TheMovieDB
 */
public class UpcomingRequest implements RetrofitRequest<UpcomingResponse, UpcomingError>, CacheRequest<UpcomingResponse, UpcomingError> {

    public static final String REQUEST_TAG = "UpcomingRequest";
    private final DataApi api;
    private final MovieDiskRepository repository;

    public UpcomingRequest(DataApi api, MovieDiskRepository repository) {
        this.api = api;
        this.repository = repository;
    }


    @Override
    public UpcomingResponse retrieveNetworkResponse() {
        return api.getUpcoming();
    }

    @Override
    public UpcomingResponse postProcess(UpcomingResponse upcomingResponse) {
        return upcomingResponse;
    }

    @Override
    public void saveToCache(UpcomingResponse upcomingResponse) {
        repository.saveAll(upcomingResponse.getResults());
    }

    @Override
    public UpcomingError composeErrorResponse(Exception e) {
        return new UpcomingError();
    }

    @Override
    public String getRequestName() {
        return REQUEST_TAG;
    }

    @Override
    public UpcomingError composeErrorResponse(RetrofitError error) {
        return new UpcomingError();
    }


    @Override
    public UpcomingResponse retrieveFromCache() {
        final UpcomingResponse response = new UpcomingResponse();
        response.setSuccess(true);
        response.setMovies(new ArrayList<Movie>(repository.getAll()));
        return response;
    }

    @Override
    public boolean isCacheValid() {
        return true;
    }
}
