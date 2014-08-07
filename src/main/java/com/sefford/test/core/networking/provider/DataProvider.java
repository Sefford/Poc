package com.sefford.test.core.networking.provider;


import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.sefford.kor.providers.interfaces.Provider;

import java.util.List;

/**
 * Basic Implementation of a Provider to execute requests
 */
public class DataProvider implements Provider<Job> {
    protected final JobManager queue;

    public DataProvider(JobManager queue) {
        this.queue = queue;
    }


    @Override
    public void executeOperation(Job dataRequest) {
        queue.addJob(dataRequest);
    }

    @Override
    public void executeOperations(List<Job> dataRequests) {
        for (Job request : dataRequests){
            executeOperation(request);
        }
    }

    @Override
    public void clear() {

    }
}
