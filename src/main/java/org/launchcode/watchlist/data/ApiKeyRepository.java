package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.ApiKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKey, Integer> {
    ApiKey findByName(String name);
}
