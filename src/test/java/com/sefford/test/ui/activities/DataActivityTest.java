package com.sefford.test.ui.activities;

import com.sefford.test.ui.controllers.DataActivityController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * These tests is a PoC on how thanks to extracting the logic to a controller, the activity is
 * easily testable
 */
@RunWith(RobolectricTestRunner.class)
public class DataActivityTest {

    private ActivityController<DataActivity> activityController;
    private DataActivityController controller;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(DataActivity.class).create();
        controller = activityController.get().controller;
    }

    @Test
    public void testOnCreate() throws Exception {
        verify(controller, times(1)).onCreate();
    }

    @Test
    public void testOnResume() throws Exception {
        activityController.resume();
        verify(controller, times(1)).onResume(activityController.get(), activityController.get().findViewById(android.R.id.content));
    }

    @Test
    public void testOnPause() throws Exception {
        activityController.pause();
        verify(controller, times(1)).onPause();
    }

    @Test
    public void testOnDestroy() throws Exception {
        activityController.destroy();
        verify(controller, times(1)).onDestroy();
    }
}