package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
    List<Movie> findByTitle(String title);
    Page<Movie> findByTitle(String title, Pageable pageable);
    Page<Movie> findByUserId(int id, Pageable pageable);
    Page<Movie> findByUserIdAndSortByDateAfter(Integer id, String sortByDate, Pageable pageable);
    Page<Movie> findByUserIdAndTitleContaining(int id, String search, Pageable pageable);

    Movie findByTmdbId(int tmdbId);
    Movie findByTitleAndUserId(String title, int userId);
    Movie findByTmdbIdAndUserId(int tmdbId, int userId);

    @Query(value = "SELECT * FROM watchlist.movie m"
            + " INNER JOIN movie_cast mc ON m.id = mc.movies_id INNER JOIN cast_member cm ON cm.id = mc.cast_id"
            + " WHERE m.user_id = ?1 and cm.name like ?2 ;",
            nativeQuery = true)
    List<Movie> findByUserIdAndCastMemberName(@Param("userId") int userId, @Param("name") String name);


}
