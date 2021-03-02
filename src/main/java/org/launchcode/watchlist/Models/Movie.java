package org.launchcode.watchlist.Models;

import com.google.gson.annotations.SerializedName;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie extends AbstractEntity{

    @NotNull
    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    private String releaseYear;

    private String physicalReleaseDate;

    private String digitalReleaseDate;

    private String sortByDate;

    private String overview;

    private String trailerUrl;

    @SerializedName("poster_path")
    private String posterPath;

    private int runtime;

    private String comment;

    @SerializedName("id")
    private int tmdbId;

    @ManyToOne
    private User user;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private List<Director> directors = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private List<CastMember> cast = new ArrayList<>();

    private String dateAdded;

    public Movie(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.dateAdded= currentDateTime.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        setReleaseYear();
        String[] sorted = this.releaseDate.split("-");
        setSortByDate(sorted[2]+ "-" + sorted[0] + "-" + sorted[1]);
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    private void setReleaseYear() {
        this.releaseYear =  "(" + getReleaseDate().split("-")[2] + ")";
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<Director> getCrew(){
        return directors;
    }

    public List<CastMember> getCast() {
        return cast;
    }

    public void setCast(List<CastMember> cast) {
        this.cast = cast;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getPhysicalReleaseDate() {
        return physicalReleaseDate;
    }

    public void setPhysicalReleaseDate(String physicalReleaseDate) {
        this.physicalReleaseDate = physicalReleaseDate;
    }

    public String getDigitalReleaseDate() {
        return digitalReleaseDate;
    }

    public void setDigitalReleaseDate(String digitalReleaseDate) {
        this.digitalReleaseDate = digitalReleaseDate;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    private void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSortByDate() {
        return sortByDate;
    }

    public void setSortByDate(String sortByDate) {
        this.sortByDate = sortByDate;
    }
}
