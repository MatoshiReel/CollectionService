package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.repository.MovieStatusRepository;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class MovieStatusService {
    private final MovieStatusRepository repository;

    public MovieStatusService(MovieStatusRepository repository) {
        this.repository = repository;
    }

    public List<MovieStatus> getAll() {
        return (List<MovieStatus>) repository.findAll();
    }

    public MovieStatus getById(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return repository.findById(UUID.fromString(id)).orElseThrow(() -> new SourceNotFoundException("status"));
    }
}
