package com.sefford.test.core.networking;

import com.google.gson.Gson;

import retrofit.converter.GsonConverter;

/**
 * GsonConverter to provide custom deserializations for Retrofit
 */
public class RetrofitGsonConverter extends GsonConverter {
    public RetrofitGsonConverter(Gson gson) {
        super(gson);
    }

    public RetrofitGsonConverter(Gson gson, String encoding) {
        super(gson, encoding);
    }
}
