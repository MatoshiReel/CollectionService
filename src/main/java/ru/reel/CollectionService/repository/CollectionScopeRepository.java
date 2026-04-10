package ru.reel.CollectionService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.reel.CollectionService.entity.CollectionScope;

import java.util.UUID;

@Repository
public interface CollectionScopeRepository extends CrudRepository<CollectionScope, UUID> {
}
