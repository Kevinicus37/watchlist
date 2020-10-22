package org.launchcode.watchlist.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CastMember extends ProductionMember {

    @ManyToMany(mappedBy = "cast")
    private List<Movie> movies = new ArrayList<>();

    public CastMember() {}

    public CastMember(String aName){
        setName(aName);
    }

    public CastMember(String aName, int aId){
        this(aName);
        setTmdbId(aId);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

}
