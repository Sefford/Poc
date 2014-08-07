package com.sefford.test.core.networking;


import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Custom Error handling to override Retrofit's default one. This will allow Kor requests to
 * bypass the error and notify it through the methods.
 */
public class CustomErrorHandler implements ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError cause) {
        return cause;
    }
}
