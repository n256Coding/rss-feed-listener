package com.n256.rssfeedlistener.persistance.repository;

import com.n256.rssfeedlistener.persistance.entity.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository which will act as the Data Access layer.
 * Provides various methods to fetch and manipulate data in the database.
 */
@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Integer> {

    List<FeedEntity> findByIdentifierIn(List<String> newIdList);

    void deleteByIdNotIn(List<Integer> idList);
}
