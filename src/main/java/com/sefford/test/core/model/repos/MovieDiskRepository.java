package com.sefford.test.core.model.repos;

import com.sefford.kor.repositories.interfaces.Repository;
import com.sefford.test.core.model.Movie;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Specialization from DiskRepository for Movie disk caching
 */
public class MovieDiskRepository extends DiskRepository<Long, Movie> {
    /**
     * Creates a new instance of the Disk Repository
     *
     * @param nextLevel  Next level if available for this repository
     * @param descriptor File descriptor where to save the information
     */
    public MovieDiskRepository(Repository<Long, Movie> nextLevel, File descriptor) {
        super(nextLevel, descriptor);
    }

    @Override
    public Collection<Movie> getAll() {
        final List<Movie> movieList = new ArrayList<Movie>(super.getAll());
        for (String entry : cache.getDirectory().list()) {
            Movie element = get(Long.valueOf(entry.substring(0, entry.indexOf("."))));
            if (element != null) {
                movieList.add(element);
            }
        }
        return movieList;
    }
}
