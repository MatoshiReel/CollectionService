package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionStar;
import ru.reel.CollectionService.repository.CollectionStarRepository;
import ru.reel.CollectionService.service.exception.NotAllowException;

import java.util.Optional;

@Service
public class CollectionStarService {
    private final CollectionStarRepository repository;

    public CollectionStarService(CollectionStarRepository repository) {
        this.repository = repository;
    }

    public void save(String userId, Collection collection) throws NotAllowException {
        if(userId == null)
            throw new NullPointerException();
        if(repository.findByUserIdAndCollectionIs(userId, collection).isPresent()) {
            throw new NotAllowException("You have already starred this collection.");
        }
        repository.save(new CollectionStar(userId, collection));
    }

    public void delete(String userId, Collection collection) {
        if(userId == null)
            throw new NullPointerException();
        Optional<CollectionStar> collectionStar = repository.findByUserIdAndCollectionIs(userId, collection);
        collectionStar.ifPresent(repository::delete);
    }
}
