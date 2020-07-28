package org.launchcode.watchlist.Models.dto;

public class MovieByDirectorDTO extends MovieDbListDTO {

    private int tmdbId;


    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }
}
