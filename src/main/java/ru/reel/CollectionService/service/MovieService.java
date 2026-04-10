package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.repository.MovieRepository;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class MovieService {
    private final MovieRepository repository;
    private final MovieStatusService movieStatusService;
    private final CollectionService collectionService;

    public MovieService(MovieRepository repository, MovieStatusService movieStatusService, CollectionService collectionService) {
        this.repository = repository;
        this.movieStatusService = movieStatusService;
        this.collectionService = collectionService;
    }

    public String save(Movie movie, Collection collection) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(movie == null || movie.getStatus() == null || collection == null)
            throw new NullPointerException();
        Optional<Set<Movie>> savedFavoriteMovieUuids = repository.findIdByCatalogIdAndCollectionsOwnerId(movie.getCatalogId(), collection.getOwnerId());
        savedFavoriteMovieUuids.flatMap(movies -> movies.stream().findAny()).ifPresent(savedMovie -> movie.setId(savedMovie.getId()));
        MovieStatus movieStatus = movieStatusService.getById(movie.getStatus().getId().toString());
        movie.setStatus(movieStatus);
        repository.save(movie);
        collection.getMovies().add(movie);
        collectionService.save(collection);
        return movie.getId().toString();
    }

    public String update(Movie movie, String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(movie == null)
            throw new NullPointerException();
        Optional<Movie> savedMovie = repository.findById(UUID.fromString(id));
        if(savedMovie.isPresent()) {
            if(movie.getOwnerRating() != 0.0)
                savedMovie.get().setOwnerRating(movie.getOwnerRating());
            if(movie.getStatus() != null)
                savedMovie.get().setStatus(movieStatusService.getById(movie.getStatus().getId().toString()));
        }
        repository.save(savedMovie.orElseThrow(() -> new SourceNotFoundException(("movie"))));
        return savedMovie.get().getId().toString();
    }

    public Movie getByCatalogIdAndOwnerId(String catalogId, String ownerId) {
        return repository.findIdByCatalogIdAndCollectionsOwnerId(catalogId, ownerId).flatMap(movies -> movies.stream().findAny()).orElse(null);
    }

    public String getOwnerId(Movie movie) throws SourceNotFoundException {
        if(movie == null)
            throw new NullPointerException();
        return movie.getCollections().stream().findAny().orElseThrow(() -> new SourceNotFoundException("collection")).getOwnerId();
    }

    public Movie getById(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return repository.findById(UUID.fromString(id)).orElseThrow(() -> new SourceNotFoundException("movie"));
    }

    public void deleteById(String id) throws NullPointerException, IllegalArgumentException {
        if(id == null)
            throw new NullPointerException();
        repository.deleteById(UUID.fromString(id));
    }
}
