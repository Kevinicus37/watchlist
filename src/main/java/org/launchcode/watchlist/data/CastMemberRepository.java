package org.launchcode.watchlist.data;

import org.launchcode.watchlist.Models.CastMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastMemberRepository extends CrudRepository<CastMember, Integer> {
    public CastMember findByName(String name);
    public CastMember findById(int id);
}
