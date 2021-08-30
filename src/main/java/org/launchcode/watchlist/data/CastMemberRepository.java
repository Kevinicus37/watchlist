package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.CastMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastMemberRepository extends CrudRepository<CastMember, Integer> {
    CastMember findByName(String name);
    CastMember findById(int id);

      List<CastMember> findByTmdbId(int tmdbId);
}
