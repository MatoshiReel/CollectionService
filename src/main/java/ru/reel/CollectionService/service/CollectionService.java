package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.repository.CollectionRepository;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollectionService {
    public static final String SOURCE_NAME = "collection";
    private final CollectionRepository repository;
    private final CollectionScopeService collectionScopeService;

    public CollectionService(CollectionRepository repository, CollectionScopeService collectionScopeService) {
        this.repository = repository;
        this.collectionScopeService = collectionScopeService;
    }

    public String save(Collection collection) throws NullPointerException, SourceNotFoundException {
        if(collection == null)
            throw new NullPointerException();
        CollectionScope scope;
        if(collection.getScope().getId() != null) scope = collectionScopeService.getById(collection.getScope().getId().toString());
        else scope = collectionScopeService.getByPriority(collection.getScope().getPriority());
        collection.setScope(scope);
        repository.save(collection);
        return collection.getId().toString();
    }

    public String update(Collection collection) throws NullPointerException, SourceNotFoundException {
        if(collection == null)
            throw new NullPointerException();
        Collection savedCollection = this.getById(collection.getId().toString());
        if(collection.getName() != null)
            savedCollection.setName(collection.getName());
        if(collection.getOrder() != 0.0)
            savedCollection.setOrder(collection.getOrder());
        if(collection.getScope() != null) {
            if(collection.getScope().getId() != null) savedCollection.setScope(collectionScopeService.getById(collection.getScope().getId().toString()));
            else savedCollection.setScope(collectionScopeService.getByPriority(collection.getScope().getPriority()));
        }
        repository.save(savedCollection);
        return savedCollection.getId().toString();
    }

    public Collection getById(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return repository.findById(UUID.fromString(id)).orElseThrow(() -> new SourceNotFoundException(SOURCE_NAME));
    }

    public List<Collection> getByOwnerId(String ownerId) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(ownerId == null)
            throw  new NullPointerException();
        List<Collection> ownerCollections = repository.findAllByOwnerId(UUID.fromString(ownerId));
        if(ownerCollections.isEmpty())
            throw new SourceNotFoundException(SOURCE_NAME);
        return ownerCollections;
    }

    public void deleteById(String id) throws NullPointerException, IllegalArgumentException {
        if(id == null)
            throw new NullPointerException();
        repository.deleteById(UUID.fromString(id));
    }
}
