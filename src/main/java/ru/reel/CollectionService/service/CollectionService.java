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
    private final CollectionRepository repository;
    private final CollectionScopeService collectionScopeService;

    public CollectionService(CollectionRepository repository, CollectionScopeService collectionScopeService) {
        this.repository = repository;
        this.collectionScopeService = collectionScopeService;
    }

    public String save(Collection collection) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(collection == null || collection.getScope() == null)
            throw new NullPointerException();
        CollectionScope scope;
        if(collection.getScope().getId() != null) scope = collectionScopeService.getById(collection.getScope().getId().toString());
        else scope = collectionScopeService.getByPriority(collection.getScope().getPriority());
        collection.setScope(scope);
        repository.save(collection);
        return collection.getId().toString();
    }

    public String update(Collection collection, String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(collection == null)
            throw new NullPointerException();
        Optional<Collection> savedCollection = repository.findById(UUID.fromString(id));
        if(savedCollection.isPresent()) {
            if(collection.getName() != null)
                savedCollection.get().setName(collection.getName());
            if(collection.getOrder() != 0.0)
                savedCollection.get().setOrder(collection.getOrder());
            if(collection.getScope() != null) {
                if(collection.getScope().getId() != null) savedCollection.get().setScope(collectionScopeService.getById(collection.getScope().getId().toString()));
                else savedCollection.get().setScope(collectionScopeService.getByPriority(collection.getScope().getPriority()));
            }
        }
        repository.save(savedCollection.orElseThrow(() -> new SourceNotFoundException("collection")));
        return savedCollection.get().getId().toString();
    }

    public Collection getById(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return repository.findById(UUID.fromString(id)).orElseThrow(() -> new SourceNotFoundException("collection"));
    }

    public List<Collection> getByOwnerId(String ownerId) throws NullPointerException, IllegalArgumentException {
        if(ownerId == null)
            throw  new NullPointerException();
        return repository.findAllByOwnerId(UUID.fromString(ownerId));
    }

    public void deleteById(String id) throws NullPointerException, IllegalArgumentException {
        if(id == null)
            throw new NullPointerException();
        repository.deleteById(UUID.fromString(id));
    }

    public void deleteMovieRelationById(String id, Movie movie) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        Collection collection = this.getById(id);
        collection.getMovies().remove(movie);
        movie.getCollections().remove(collection);
        repository.save(collection);
    }
}
