package com.sefford.test.ui.renderers;

import android.view.LayoutInflater;
import android.view.View;

import com.sefford.test.R;
import com.sefford.test.core.model.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MovieRendererTest {

    public static final String EXPECTED_POSTER_URL = "http://www.google.com";
    public static final String EXPECTED_MOVIE_TITLE = "Fight Club";
    private static final double EXPECTED_SCORE = 5.5;

    @Mock
    Movie movie;
    private MovieRenderer renderer;
    private View view;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(movie.getPosterPath()).thenReturn(EXPECTED_POSTER_URL);
        when(movie.getTitle()).thenReturn(EXPECTED_MOVIE_TITLE);
        when(movie.getAverage()).thenReturn(EXPECTED_SCORE);
        renderer = new MovieRenderer();
        view = LayoutInflater.from(Robolectric.application).inflate(R.layout.listitem_movie, null, false);
    }

    @Test
    public void testMapViews() throws Exception {
        renderer.mapViews(view);
        assertNotNull(renderer.background);
        assertNotNull(renderer.info);
        assertNotNull(renderer.name);
        assertNotNull(renderer.score);
    }

    @Test
    public void testRender() throws Exception {
        // This test is not totally correct because Picasso is not injected and reaches outside our
        // SUT
        renderer.mapViews(view);
        renderer.render(Robolectric.application, movie, 0, false, false);
        assertEquals(renderer.score.getText().toString(), Double.toString(EXPECTED_SCORE));
        assertEquals(renderer.name.getText().toString(), EXPECTED_MOVIE_TITLE);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(renderer.getId(), R.layout.listitem_movie);
    }
}