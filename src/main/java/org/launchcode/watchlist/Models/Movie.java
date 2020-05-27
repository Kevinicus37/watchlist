package org.launchcode.watchlist.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Movie extends AbstractEntity{

    @NotNull
    private String title;

    private String releaseDate;

    private String releaseYear;

    private String formattedReleaseDate;



    private String physicalReleaseDate;

    private String digitalReleaseDate;

    private String overview;

    private String trailerUrl;

    private String posterPath;

    private int runtime;

    private String comment;

    private int tmdbId;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany
    private List<Director> directors = new ArrayList<>();

    @ManyToMany
    private List<CastMember> cast = new ArrayList<>();

    public Movie(){}

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
        setFormattedReleaseDate();
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    private void setReleaseYear() {
        this.releaseYear =  "(" + getReleaseDate().split("-")[0] + ")";
    }

    public String getFormattedReleaseDate() {
        return formattedReleaseDate;
    }

    public void setFormattedReleaseDate() {
        int index = this.releaseDate.indexOf('T');
        this.formattedReleaseDate = "";

        if (index >= 0){
            this.formattedReleaseDate = this.releaseDate.substring(0,index);
        }
        else {
            this.formattedReleaseDate = this.releaseDate;
        }

        if (!this.formattedReleaseDate.isEmpty()){
            String[] dateParts = this.formattedReleaseDate.split("-");
            dateParts[0] = dateParts[0].substring(2);
            this.formattedReleaseDate = dateParts[1] + '-' + dateParts[2] + '-' + dateParts[0];
        }

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

}
