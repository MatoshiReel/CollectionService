package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.repository.MovieStatusRepository;

import java.util.List;

@Service
public class MovieStatusService {
    private final MovieStatusRepository repository;

    public MovieStatusService(MovieStatusRepository repository) {
        this.repository = repository;
    }

    public List<MovieStatus> getAll() {
        return (List<MovieStatus>) repository.findAll();
    }
}
