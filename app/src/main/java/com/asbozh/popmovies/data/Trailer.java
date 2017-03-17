package com.asbozh.popmovies.data;

public class Trailer {

    private String trailerName;
    private String trailerKey;

    public Trailer(String trailerName, String trailerKey) {
        this.trailerName = trailerName;
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

}
