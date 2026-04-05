package ru.reel.CollectionService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.reel.CollectionService.entity.MovieStatus;

import java.util.UUID;

@Repository
public interface MovieStatusRepository extends CrudRepository<MovieStatus, UUID> {
}
