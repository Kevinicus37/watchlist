package org.launchcode.watchlist.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CastMember extends AbstractEntity {

    private String name;

    private int castId;

    @ManyToMany(mappedBy = "cast")
    private List<Movie> movies = new ArrayList<>();

    public CastMember() {}

    public CastMember(String aName){
        this.name = aName;
    }

    public CastMember(String aName, int aId){
        this(aName);
        this.castId = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CastMember that = (CastMember) o;
        return castId == that.castId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, castId);
    }
}
