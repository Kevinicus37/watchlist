package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    @Query("select id from User u where u.username = :username")
    public int findIdByUsername(@Param("username") String username);
    List<User> findByUsernameContaining(@Param("username") String username, Pageable pageable);
    // @Query("SELECT COUNT(id) FROM watchlist.user u WHERE u.username LIKE ")
    long countByUsernameContaining(@Param("username") String username);
    List<User> findAll(Pageable pageable);
}
