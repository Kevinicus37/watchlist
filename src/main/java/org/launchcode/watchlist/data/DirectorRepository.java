package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Integer> {
    Director findByName(String name);
}
