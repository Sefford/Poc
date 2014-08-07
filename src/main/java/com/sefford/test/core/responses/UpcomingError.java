package com.sefford.test.core.responses;

import com.sefford.kor.errors.ErrorInterface;

/**
 * Fast implementation of an Error Response to return to the UI
 */
public class UpcomingError implements ErrorInterface {
    @Override
    public int getStatusCode() {
        return 0;
    }

    @Override
    public String getUserError() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
