package org.launchcode.watchlist.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Director extends ProductionMember {

    @ManyToMany(mappedBy = "directors")
    private List<Movie> movies = new ArrayList<>();

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

}
