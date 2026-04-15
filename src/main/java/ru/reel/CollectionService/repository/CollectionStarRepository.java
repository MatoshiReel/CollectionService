package ru.reel.CollectionService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionStar;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionStarRepository extends CrudRepository<CollectionStar, UUID> {
    Optional<CollectionStar> findByUserIdAndCollectionIs(UUID userId, Collection collection);
}
