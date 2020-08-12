package org.launchcode.watchlist.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Director extends ProductionMember{

    @ManyToMany(mappedBy = "directors")
    private List<Movie> movies = new ArrayList<>();

//    private String name;

//    private int tmdbId;

    public Director(){}

    public Director(String aName){
        setName(aName);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

//    public int getTmdbId() {
//        return tmdbId;
//    }
//
//    public void setTmdbId(int tmdbId) {
//        this.tmdbId = tmdbId;
//    }
}
