package com.sefford.test.core.networking;

import com.sefford.test.core.responses.UpcomingResponse;

import retrofit.http.GET;

/**
 * Implementation for TheMovieDB
 */
public interface DataApi {
    public static final String API_KEY = "37b5485e2cf57f4417deebac585b2e8b";
    public static final String API_PARAM = "api_key";

    @GET("/movie/upcoming")
    UpcomingResponse getUpcoming();
}
