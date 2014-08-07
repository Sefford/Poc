package com.sefford.test.core.networking.requests;

import com.sefford.test.core.model.Movie;
import com.sefford.test.core.model.repos.MovieDiskRepository;
import com.sefford.test.core.networking.DataApi;
import com.sefford.test.core.responses.UpcomingError;
import com.sefford.test.core.responses.UpcomingResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import retrofit.RetrofitError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This test exemplifies a pure JUnit, Request test capability
 */
public class UpcomingRequestTest {

    @Mock
    DataApi api;
    @Mock
    MovieDiskRepository repository;
    @Mock
    UpcomingResponse response;
    @Mock
    UpcomingError error;

    UpcomingRequest request;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        request = spy(new UpcomingRequest(api, repository));
    }

    @Test
    public void testRetrieveNetworkResponse() throws Exception {
        request.retrieveNetworkResponse();
        verify(api, times(1)).getUpcoming();
    }

    @Test
    public void testPostProcess() throws Exception {
        request.postProcess(response);
        verifyNoMoreInteractions(response);
    }

    @Test
    public void testSaveToCache() throws Exception {
        final ArrayList list = mock(new ArrayList<Movie>().getClass());
        when(response.getResults()).thenReturn(list);
        request.saveToCache(response);

        verify(repository, times(1)).saveAll(list);
    }

    @Test
    public void testComposeErrorResponse() throws Exception {
        final UpcomingError object = request.composeErrorResponse(new Exception());
        assertNotNull(object);
    }

    @Test
    public void testGetRequestName() throws Exception {
        assertEquals(request.getRequestName(), UpcomingRequest.REQUEST_TAG);
    }

    @Test
    public void testComposeErrorResponseRetrofitError() throws Exception {
        final UpcomingError object = request.composeErrorResponse(mock(RetrofitError.class));
        assertNotNull(object);
    }

    @Test
    public void testRetrieveFromCache() throws Exception {
        final ArrayList list = new ArrayList<Movie>();
        when(repository.getAll()).thenReturn(list);

        final UpcomingResponse expected = request.retrieveFromCache();
        assertEquals(expected.getResults(), list);
        assertTrue(expected.isSuccess());
    }

    @Test
    public void testIsCacheValid() throws Exception {
        assertTrue(request.isCacheValid());
    }
}