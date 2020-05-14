package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
    Movie findByTitle(String title);
    Movie findByTmdbId(int tmdbId);
    Movie findByTitleAndUserId(String title, int userId);
}
