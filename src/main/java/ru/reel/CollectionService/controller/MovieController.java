package ru.reel.CollectionService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.dto.MovieStatusDto;
import ru.reel.CollectionService.mapper.CollectionMapper;
import ru.reel.CollectionService.mapper.MovieMapper;
import ru.reel.CollectionService.mapper.MovieStatusMapper;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.MovieService;
import ru.reel.CollectionService.service.MovieStatusService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;
import ru.reel.CollectionService.service.issue.error.*;
import ru.reel.CollectionService.service.validator.MovieValidator;

import java.util.List;

@RestController
@RequestMapping(path = "/collection/movie")
public class MovieController {
    private final CollectionService collectionService;
    private final CollectionMapper collectionMapper;
    private final MovieStatusService movieStatusService;
    private final MovieStatusMapper movieStatusMapper;
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    public MovieController(CollectionService collectionService, CollectionMapper collectionMapper, MovieStatusService movieStatusService, MovieStatusMapper movieStatusMapper, MovieService movieService, MovieMapper movieMapper) {
        this.collectionService = collectionService;
        this.collectionMapper = collectionMapper;
        this.movieStatusService = movieStatusService;
        this.movieStatusMapper = movieStatusMapper;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @GetMapping(value = "/status/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieStatusDto>> getMovieStatusList() {
        List<MovieStatusDto> list = movieStatusService.getAll().stream().map(movieStatusMapper::to).toList();
        if(list.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMovieById(@PathVariable String id, @RequestHeader("X-Account-Id") String accountId) {
        MovieDto movieDto;
        try {
            movieDto = movieMapper.to(movieService.getById(id), true, collectionMapper);
            if(movieDto == null)
                return ResponseEntity.notFound().build();
            else if (!movieDto.collections.stream().findAny().orElseThrow(() -> new SourceNotFoundException("collection")).ownerId.equals(accountId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.OWNER_ACCESS).message(String.format(ErrorMessageFactory.get(ErrorReason.OWNER_ACCESS), "collection")).build());
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.ok(movieDto);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMovieByCatalogId(@RequestParam(value = "catalog_id", required = false) String catalogMovieId, @RequestHeader("X-Account-Id") String accountId) {
        if(catalogMovieId == null || catalogMovieId.isEmpty())
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("catalog_id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "catalog_id")).build());
        MovieDto movieDto;
        try {
            movieDto = movieMapper.to(movieService.getByCatalogIdAndOwnerId(catalogMovieId, accountId), true, collectionMapper);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        }
        if(movieDto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(movieDto);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> saveMovie(@RequestBody(required = false) MovieDto movieDto, @RequestParam(value = "collection_id", required = false) String collectionId, @RequestHeader("X-Account-Id") String accountId, MovieValidator validator) {
        if(movieDto == null)
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "body")).build());
        if(collectionId == null || collectionId.isEmpty())
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collectin_id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "collection_id")).build());
        List<FieldRequestError> errors = validator.validateBeforeCreating(movieDto);
        if(!errors.isEmpty())
            return ResponseEntity.badRequest().body(errors);
        try {
            if (!collectionMapper.to(collectionService.getById(collectionId)).ownerId.equals(accountId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.OWNER_ACCESS).message(String.format(ErrorMessageFactory.get(ErrorReason.OWNER_ACCESS), "collection")).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        String movieId;
        try {
            movieId = movieService.save(movieMapper.from(movieDto), collectionService.getById(collectionId));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(movieId);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> updateMovie(@PathVariable String id, @RequestBody(required = false) MovieDto movieDto, @RequestHeader("X-Account-Id") String accountId, MovieValidator validator) {
        if(movieDto == null)
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "body")).build());
        List<FieldRequestError> errors = validator.validateBeforeUpdating(movieDto);
        if(!errors.isEmpty())
            return ResponseEntity.badRequest().body(errors);
        try {
            if(!movieService.getOwnerId(movieService.getById(id)).equals(accountId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.OWNER_ACCESS).message(String.format(ErrorMessageFactory.get(ErrorReason.OWNER_ACCESS), "movie")).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(FieldRequestError.builder().field(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        String movieId;
        try {
            movieId = movieService.update(movieMapper.from(movieDto), id);
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.ok(movieId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String id, @RequestParam(value = "collection_id", required = false) String collectionId, @RequestHeader("X-Account-Id") String accountId) {
        if(collectionId == null || collectionId.isEmpty())
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "collection_id")).build());
        try {
            CollectionDto collectionDto = collectionMapper.to(collectionService.getById(collectionId));
            if (!collectionDto.ownerId.equals(accountId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.OWNER_ACCESS).message(String.format(ErrorMessageFactory.get(ErrorReason.OWNER_ACCESS), "collection")).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        try {
            collectionService.deleteMovieRelationById(collectionId, movieService.getById(id));
            if(movieService.getById(id).getCollections().isEmpty())
                movieService.deleteById(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.noContent().build();
    }
}
