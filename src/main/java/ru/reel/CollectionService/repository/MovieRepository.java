package ru.reel.CollectionService.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.reel.CollectionService.entity.Movie;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID> {
    @Query("SELECT fm FROM Movie fm JOIN fm.collections c WHERE fm.catalogId = :catalogId and c.ownerId = :collectionOwnerId")
    Optional<Set<Movie>> findIdByCatalogIdAndCollectionsOwnerId(UUID catalogId, UUID collectionOwnerId);
}
