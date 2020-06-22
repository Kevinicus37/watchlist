package org.launchcode.watchlist.Models.dto;

import info.movito.themoviedbapi.model.MovieDb;

import java.util.List;

public class HomePageDTO {

    private List<MovieDb> upcoming;

    private List<MovieDb> nowPlaying;

    private boolean isUserList;

    public List<MovieDb> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<MovieDb> upcoming) {
        this.upcoming = upcoming;
    }

    public List<MovieDb> getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(List<MovieDb> nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public boolean isUserList() {
        return isUserList;
    }

    public void setUserList(boolean userList) {
        isUserList = userList;
    }
}
