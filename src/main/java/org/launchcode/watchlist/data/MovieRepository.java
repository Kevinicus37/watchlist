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

    public static final String MOVIE_GENRE_JOIN = "INNER JOIN movie_genres mg ON m.id = mg.movies_id " +
            "INNER JOIN genre g ON g.id = mg.genres_id ";
    public static final String MOVIE_CAST_JOIN = "Inner join movie_cast mc on m.id = mc.movies_id " +
            "inner join cast_member cm on cm.id = mc.cast_id ";
    public static final String SELECT_MOVIE = "SELECT DISTINCT m.* FROM watchlist.movie m ";
    public static final String SELECT_MOVIE_COUNT = "SELECT count(DISTINCT m.id) FROM watchlist.movie m ";
    public static final String ORDER_BY = "ORDER BY ?#{#pageable} ";
    public static final String WHERE_USER_TITLE_GENRE = "WHERE m.user_id = ?1 AND g.name in (?3) AND m.title like %?2% ";
    public static final String WHERE_USER_CAST_GENRE = "where m.user_id = ?1 and g.name in (?3) and cm.name like %?2%";

    List<Movie> findByTitle(String title);
    Page<Movie> findByTitle(String title, Pageable pageable);
    Page<Movie> findByUserId(int id, Pageable pageable);
    Page<Movie> findByUserIdAndSortByDateAfter(Integer id, String sortByDate, Pageable pageable);
    Page<Movie> findByUserIdAndTitleContaining(int id, String search, Pageable pageable);
    Movie findByTmdbId(int tmdbId);
    Movie findByTitleAndUserId(String title, int userId);
    Movie findByTmdbIdAndUserId(int tmdbId, int userId);




    @Query(value = SELECT_MOVIE + MOVIE_CAST_JOIN + MOVIE_GENRE_JOIN + WHERE_USER_CAST_GENRE + ORDER_BY,
            countQuery = SELECT_MOVIE_COUNT + MOVIE_CAST_JOIN + MOVIE_GENRE_JOIN + WHERE_USER_CAST_GENRE,
            nativeQuery = true)
    Page<Movie> findByUserIdAndCastMemberNameFilteredByGenre(@Param("userId") int userId, @Param("name") String name,
                                                             @Param("genres") List<String> genres, Pageable pageable);

    @Query(value = SELECT_MOVIE + MOVIE_GENRE_JOIN + WHERE_USER_TITLE_GENRE + ORDER_BY,
            countQuery = SELECT_MOVIE_COUNT + MOVIE_GENRE_JOIN + WHERE_USER_TITLE_GENRE,
            nativeQuery = true)
    Page<Movie> findByUserIdAndTitleContainingFilteredByGenre(@Param("userId") int userId, @Param("name") String name,
                                                             @Param("genres") List<String> genres, Pageable pageable);


}
