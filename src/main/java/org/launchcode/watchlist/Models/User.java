package org.launchcode.watchlist.Models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractEntity {

    @NotNull
    private String username;

    @NotNull
    private String passwordHash;

    @OneToMany(mappedBy="user")
    private List<Movie> watchlist = new ArrayList<>();

    private String profilePicturePath;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = encoder.encode(password);
    }

    public boolean isMatchingPassword(String password){
        return encoder.matches(password, passwordHash);
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Movie> getMovies() {
        return watchlist;
    }

    public void setMovies(List<Movie> movies) {
        this.watchlist = movies;
    }

    public List<Movie> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<Movie> watchlist) {
        this.watchlist = watchlist;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = encoder.encode(password);
    }
}
