package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionStar;
import ru.reel.CollectionService.repository.CollectionStarRepository;
import ru.reel.CollectionService.service.exception.NotAllowException;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
public class CollectionStarService {
    private final static String NOT_ALLOW_EXCEPTION_MESSAGE = "You have already starred this collection.";
    private final CollectionStarRepository repository;
    private final CollectionService collectionService;

    public CollectionStarService(CollectionStarRepository repository, CollectionService collectionService) {
        this.repository = repository;
        this.collectionService = collectionService;
    }

    public void save(String userId, String collectionId) throws NullPointerException, NotAllowException, IllegalArgumentException, SourceNotFoundException {
        if(userId == null || collectionId == null)
            throw new NullPointerException();
        Collection collection = collectionService.getById(collectionId);
        if(repository.findByUserIdAndCollectionIs(UUID.fromString(userId), collection).isPresent()) {
            throw new NotAllowException(NOT_ALLOW_EXCEPTION_MESSAGE);
        }
        repository.save(new CollectionStar(UUID.fromString(userId), collection));
    }

    public void delete(String userId, String collectionId) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(userId == null || collectionId == null)
            throw new NullPointerException();
        Collection collection = collectionService.getById(collectionId);
        Optional<CollectionStar> collectionStar = repository.findByUserIdAndCollectionIs(UUID.fromString(userId), collection);
        collectionStar.ifPresent(repository::delete);
    }
}