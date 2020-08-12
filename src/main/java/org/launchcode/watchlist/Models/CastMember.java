package org.launchcode.watchlist.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CastMember extends ProductionMember {

//    private String name;

//    private int tmdbId;

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

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

//    public int getTmdbId() {
//        return tmdbId;
//    }
//
//    public void setTmdbId(int tmdbId) {
//        this.tmdbId = tmdbId;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        CastMember that = (CastMember) o;
//        return getTmdbId() == that.getTmdbId() &&
//                Objects.equals(getName(), that.getName());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), getName(), getTmdbId());
//    }
}
