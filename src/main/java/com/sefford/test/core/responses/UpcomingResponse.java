package com.sefford.test.core.responses;

import com.sefford.kor.responses.ResponseInterface;
import com.sefford.test.core.model.Movie;

import java.util.List;

/**
 * Basic response specialization for the UpcomingRequest
 */
public class UpcomingResponse implements ResponseInterface {

    private boolean success;
    private boolean isFromNetwork;
    private List<Movie> movies;

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean isFromNetwork() {
        return isFromNetwork;
    }


    public List<Movie> getResults() {
        return movies;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
