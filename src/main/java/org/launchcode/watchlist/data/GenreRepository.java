package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer> {
    Genre findByName(String name);

    @Query(value= "SELECT g.name From watchlist.genre g", nativeQuery = true)
    List<String> findAllNames();
}
