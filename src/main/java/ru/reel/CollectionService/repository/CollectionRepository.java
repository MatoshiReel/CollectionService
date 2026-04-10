package ru.reel.CollectionService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.reel.CollectionService.entity.Collection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionRepository extends CrudRepository<Collection, UUID> {
    List<Collection> findAllByOwnerId(String ownerId);
}
