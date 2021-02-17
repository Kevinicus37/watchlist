package org.launchcode.watchlist.Models;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public class ProductionMember extends AbstractEntity {

    private String biography;

    private String name;

    @Column(name = "tmdb_id", nullable = false)
    private int tmdbId;

    @SerializedName("place_of_birth")
    private String placeOfBirth;

    // Profile picture path
    @SerializedName("profile_path")
    private String profilePath;

    private String birthday;

    private String deathday;

    public ProductionMember(){}

    public ProductionMember(String aName, int aId){
        this.name = aName;
        this.tmdbId = aId;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductionMember that = (ProductionMember) o;
        return tmdbId == that.tmdbId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, tmdbId);
    }
}
