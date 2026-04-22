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
    public static final String SOURCE_NAME = "movie";
    private final MovieRepository repository;
    private final MovieStatusService movieStatusService;
    private final CollectionService collectionService;

    public MovieService(MovieRepository repository, MovieStatusService movieStatusService, CollectionService collectionService) {
        this.repository = repository;
        this.movieStatusService = movieStatusService;
        this.collectionService = collectionService;
    }

    public String save(Movie movie, String collectionId) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(movie == null || movie.getStatus() == null)
            throw new NullPointerException();
        Collection collection = collectionService.getById(collectionId);
        Optional<Set<Movie>> savedMovieUuids = repository.findIdByCatalogIdAndCollectionsOwnerId(movie.getCatalogId(), collection.getOwnerId());
        savedMovieUuids.flatMap(movies -> movies.stream().findAny()).ifPresent(savedMovie -> movie.setId(savedMovie.getId()));
        MovieStatus movieStatus;
        if(movie.getStatus().getId() != null) movieStatus = movieStatusService.getById(movie.getStatus().getId().toString());
        else movieStatus = movieStatusService.getByOrder(movie.getStatus().getOrder());
        movie.setStatus(movieStatus);
        repository.save(movie);
        collection.getMovies().add(movie);
        collectionService.save(collection);
        return movie.getId().toString();
    }

    public String update(Movie movie) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(movie == null)
            throw new NullPointerException();
        Movie savedMovie = this.getById(movie.getId().toString());
        if(movie.getOwnerRating() != 0.0) {
            savedMovie.setOwnerRating(movie.getOwnerRating());
        }
        if(movie.getStatus() != null) {
            if(movie.getStatus().getId() != null) {
                savedMovie.setStatus(movieStatusService.getById(movie.getStatus().getId().toString()));
            } else {
                savedMovie.setStatus(movieStatusService.getByOrder(movie.getStatus().getOrder()));
            }
        }
        repository.save(savedMovie);
        return savedMovie.getId().toString();
    }

    public Movie getByCatalogIdAndOwnerId(String catalogId, String ownerId) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(catalogId == null || ownerId == null)
            throw new NullPointerException();
        return repository.findIdByCatalogIdAndCollectionsOwnerId(UUID.fromString(catalogId), UUID.fromString(ownerId)).flatMap(movies -> movies.stream().findAny()).orElseThrow(() -> new SourceNotFoundException(SOURCE_NAME));
    }

    public String getOwnerId(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return this.getById(id).getCollections().stream().findAny().orElseThrow(() -> new SourceNotFoundException(CollectionService.SOURCE_NAME)).getOwnerId().toString();
    }

    public Movie getById(String id) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        if(id == null)
            throw new NullPointerException();
        return repository.findById(UUID.fromString(id)).orElseThrow(() -> new SourceNotFoundException(SOURCE_NAME));
    }

    public void deleteById(String id) throws NullPointerException, IllegalArgumentException {
        if(id == null)
            throw new NullPointerException();
        repository.deleteById(UUID.fromString(id));
    }

    public void deleteCollectionRelationById(String movieId, String collectionId) throws NullPointerException, IllegalArgumentException, SourceNotFoundException {
        Movie movie = this.getById(movieId);
        Collection collection = collectionService.getById(collectionId);
        collection.getMovies().remove(movie);
        movie.getCollections().remove(collection);
        collectionService.save(collection);
        if(movie.getCollections().isEmpty())
            this.deleteById(movie.getId().toString());
    }
}
