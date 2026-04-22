package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.repository.CollectionScopeRepository;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class CollectionScopeService {
    public static final String SOURCE_NAME = "scope";
    private final CollectionScopeRepository repository;

    public CollectionScopeService(CollectionScopeRepository repository) {
        this.repository = repository;
    }

    public List<CollectionScope> getAll() {
        return (List<CollectionScope>) repository.findAll();
    }

    public CollectionScope getById(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return repository.findById(UUID.fromString(id)).orElseThrow(() -> new SourceNotFoundException(SOURCE_NAME));
    }

    public CollectionScope getByPriority(short priority) throws SourceNotFoundException {
        return repository.findByPriority(priority).orElseThrow(() -> new SourceNotFoundException(SOURCE_NAME));
    }
}
