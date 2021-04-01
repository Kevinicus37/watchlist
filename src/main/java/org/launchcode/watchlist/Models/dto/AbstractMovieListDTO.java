package org.launchcode.watchlist.Models.dto;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMovieListDTO extends AbstractListDTO {

    private String username;

    private long movieCount;

    private boolean isUserList;

    private List<String> genreNames = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(long movieCount) {
        this.movieCount = movieCount;
    }

    public boolean isUserList() {
        return isUserList;
    }

    public void setUserList(boolean userList) {
        isUserList = userList;
    }

    public List<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(List<String> genreNames) {
        this.genreNames = genreNames;
    }
}
