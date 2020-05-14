package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer> {
    Genre findByName(String name);
}
