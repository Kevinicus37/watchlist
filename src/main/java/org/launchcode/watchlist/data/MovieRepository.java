package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.Movie;
import org.launchcode.watchlist.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findByTitle(String title);
    Movie findByTmdbId(int tmdbId);
    Movie findByTitleAndUserId(String title, int userId);
}
