package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    @Query("select id from User u where u.username = :username")
    public int findIdByUsername(@Param("username") String username);
}
