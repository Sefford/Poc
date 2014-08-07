package com.sefford.test.core.model;

import com.google.gson.annotations.SerializedName;
import com.sefford.brender.interfaces.Renderable;
import com.sefford.kor.repositories.interfaces.RepoElement;
import com.sefford.test.R;

import java.io.Serializable;

/**
 * Created by sefford on 7/08/14.
 */
public class Movie implements Renderable, RepoElement<Long>, Serializable {

    private long id;
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    private double popularity;
    @SerializedName("vote_average")
    private double average;
    @SerializedName("release_date")
    private String release;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/original" + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    @Override
    public int getRenderableId() {
        return R.layout.listitem_movie;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
