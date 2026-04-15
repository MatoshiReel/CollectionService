package ru.reel.CollectionService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.reel.CollectionService.dto.*;
import ru.reel.CollectionService.dto.criteria.CollectionCriteria;
import ru.reel.CollectionService.mapper.CollectionMapper;
import ru.reel.CollectionService.mapper.CollectionScopeMapper;
import ru.reel.CollectionService.mapper.MovieMapper;
import ru.reel.CollectionService.service.CollectionScopeService;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;
import ru.reel.CollectionService.service.issue.error.*;
import ru.reel.CollectionService.service.sort.CollectionSorter;
import ru.reel.CollectionService.service.validator.CollectionValidator;

import java.util.List;

@RestController
@RequestMapping(path = "/collection")
public class CollectionController {
    private final CollectionService collectionService;
    private final CollectionMapper collectionMapper;
    private final CollectionScopeService collectionScopeService;
    private final CollectionScopeMapper collectionScopeMapper;
    private final MovieMapper movieMapper;


    public CollectionController(CollectionService collectionService, CollectionMapper collectionMapper, CollectionScopeService collectionScopeService, CollectionScopeMapper collectionScopeMapper, MovieMapper movieMapper) {
        this.collectionService = collectionService;
        this.collectionMapper = collectionMapper;
        this.collectionScopeService = collectionScopeService;
        this.collectionScopeMapper = collectionScopeMapper;
        this.movieMapper = movieMapper;
    }

    @GetMapping(value = "/scope/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CollectionScopeDto>> getCollectionScopeList() {
        List<CollectionScopeDto> list = collectionScopeService.getAll().stream().map(collectionScopeMapper::to).toList();
        if(list.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CollectionDto>> getCollectionList(@RequestBody(required = false) CollectionCriteria criteria, @RequestHeader("X-Account-Id") String accountId) {
        List<CollectionDto> sortedCollectionsDto;
        if(criteria != null) {
            sortedCollectionsDto = CollectionSorter
                    .sort(collectionService.getByOwnerId(accountId))
                    .by()
                    .field(criteria.sort)
                    .order(criteria.order)
                    .get().stream().map(collectionMapper::to).toList();
            if(criteria.page != null) {
                sortedCollectionsDto = sortedCollectionsDto.stream().skip(criteria.page.index * criteria.page.items).limit(criteria.page.items).toList();
            }
        } else {
            sortedCollectionsDto = collectionService.getByOwnerId(accountId).stream().map(collectionMapper::to).toList();
        }
        if(sortedCollectionsDto.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sortedCollectionsDto);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCollection(@PathVariable String id, @RequestHeader("X-Account-Id") String accountId) {
        CollectionDto collectionDto;
        try {
            collectionDto = collectionMapper.to(collectionService.getById(id), true, movieMapper);
            if (!collectionDto.ownerId.equals(accountId) && collectionDto.scope.title.equalsIgnoreCase("Private"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.SCOPE).message(String.format(ErrorMessageFactory.get(ErrorReason.SCOPE), "collection", collectionDto.scope.title)).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.ok(collectionDto);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> createCollection(@RequestBody(required = false) CollectionDto collectionDto, @RequestHeader("X-Account-Id") String accountId, CollectionValidator validator) {
        if(collectionDto == null)
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "body")).build());
        List<FieldRequestError> errors = validator.validateBeforeCreating(collectionDto);
        if(!errors.isEmpty())
            return ResponseEntity.badRequest().body(errors);
        collectionDto.ownerId = accountId;
        collectionDto.name = collectionDto.name.trim();
        String collectionId;
        try {
            collectionId = collectionService.save(collectionMapper.from(collectionDto));
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(FieldRequestError.builder().field(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionId);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> updateCollection(@PathVariable String id, @RequestBody(required = false) CollectionDto collectionDto, @RequestHeader("X-Account-Id") String accountId, CollectionValidator validator) {
        if(collectionDto == null)
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "body")).build());
        try {
            if(!collectionMapper.to(collectionService.getById(id)).ownerId.equals(accountId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.OWNER_ACCESS).message(String.format(ErrorMessageFactory.get(ErrorReason.OWNER_ACCESS), "collection")).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(FieldRequestError.builder().field(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        List<FieldRequestError> errors = validator.validateBeforeUpdating(collectionDto);
        if(!errors.isEmpty())
            return ResponseEntity.badRequest().body(errors);
        collectionDto.ownerId = accountId;
        if(collectionDto.name != null)
            collectionDto.name = collectionDto.name.trim();
        String collectionId;
        try {
            collectionId = collectionService.update(collectionMapper.from(collectionDto), id);
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(FieldRequestError.builder().field(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.ok(collectionId);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestError> deleteCollection(@PathVariable String id, @RequestHeader("X-Account-Id") String accountId) {
        try {
            CollectionDto collectionDto = collectionMapper.to(collectionService.getById(id));
            if(!collectionDto.ownerId.equals(accountId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RequestError.builder().errorReason(ErrorReason.OWNER_ACCESS).message(String.format(ErrorMessageFactory.get(ErrorReason.OWNER_ACCESS), "collection")).build());
            collectionService.deleteById(collectionDto.id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.noContent().build();
    }
}
