package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionStar;
import ru.reel.CollectionService.repository.CollectionStarRepository;
import ru.reel.CollectionService.service.exception.NotAllowException;

import java.util.Optional;
import java.util.UUID;

@Service
public class CollectionStarService {
    private final CollectionStarRepository repository;

    public CollectionStarService(CollectionStarRepository repository) {
        this.repository = repository;
    }

    public void save(String userId, Collection collection) throws NotAllowException, IllegalArgumentException {
        if(userId == null)
            throw new NullPointerException();
        if(repository.findByUserIdAndCollectionIs(UUID.fromString(userId), collection).isPresent()) {
            throw new NotAllowException("You have already starred this collection.");
        }
        repository.save(new CollectionStar(UUID.fromString(userId), collection));
    }

    public void delete(String userId, Collection collection) throws IllegalArgumentException {
        if(userId == null)
            throw new NullPointerException();
        Optional<CollectionStar> collectionStar = repository.findByUserIdAndCollectionIs(UUID.fromString(userId), collection);
        collectionStar.ifPresent(repository::delete);
    }
}
