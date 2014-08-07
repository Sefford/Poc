package com.sefford.test.core.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sefford.test.core.model.Movie;
import com.sefford.test.core.responses.UpcomingResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom GSon deserializer for parsing the response
 */
public class UpcomingRequestDeserializer implements JsonDeserializer<UpcomingResponse> {
    @Override
    public UpcomingResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject root = json.getAsJsonObject();
        final UpcomingResponse response = new UpcomingResponse();

        final List<Movie> movies = new ArrayList<Movie>();
        for (final JsonElement element : root.get("results").getAsJsonArray()) {
            final Movie movie = context.deserialize(element, Movie.class);
            movies.add(movie);
        }
        response.setMovies(movies);
        response.setSuccess(true);
        return response;
    }
}
