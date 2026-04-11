package ru.reel.CollectionService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.reel.CollectionService.dto.CollectionStarDto;
import ru.reel.CollectionService.entity.CollectionStar;
import ru.reel.CollectionService.mapper.CollectionStarMapper;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.CollectionStarService;
import ru.reel.CollectionService.service.exception.NotAllowException;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;
import ru.reel.CollectionService.service.issue.error.ErrorMessageFactory;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.ParamRequestError;
import ru.reel.CollectionService.service.issue.error.RequestError;

import java.util.List;

@RestController
@RequestMapping(path = "/collection/star")
public class CollectionStarController {
    private final CollectionService collectionService;
    private final CollectionStarMapper collectionStarMapper;
    private final CollectionStarService collectionStarService;

    public CollectionStarController(CollectionService collectionService, CollectionStarMapper collectionStarMapper, CollectionStarService collectionStarService) {
        this.collectionService = collectionService;
        this.collectionStarMapper = collectionStarMapper;
        this.collectionStarService = collectionStarService;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getCollectionStarsList(@RequestParam("collection_id") String collectionId) {
        if(collectionId == null || collectionId.isEmpty())
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collectin_id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "collection_id")).build());
        List<CollectionStarDto> collectionStarsDto;
        try {
            collectionStarsDto = collectionService.getById(collectionId).getUserStars().stream().map(collectionStarMapper::to).toList();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.ok(collectionStarsDto);
    }

    @PostMapping("")
    public ResponseEntity<Object> starredCollection(@RequestParam("collection_id") String collectionId, @RequestHeader("X-Account-Id") String accountId) {
        if(collectionId == null || collectionId.isEmpty())
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collectin_id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "collection_id")).build());
        try {
            collectionStarService.save(accountId, collectionService.getById(collectionId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        } catch (NotAllowException e) {
            return ResponseEntity.badRequest().body(RequestError.builder().errorReason(ErrorReason.NOT_ALLOW).message(e.getMessage()).build());
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Object> unstarCollection(@RequestParam("collection_id") String collectionId, @RequestHeader("X-Account-Id") String accountId) {
        if(collectionId == null || collectionId.isEmpty())
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collectin_id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "collection_id")).build());
        try {
            collectionStarService.delete(accountId, collectionService.getById(collectionId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param("collection_id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } catch (SourceNotFoundException e) {
            return ResponseEntity.badRequest().body(ParamRequestError.builder().param(e.getSource()).errorReason(ErrorReason.NOT_FOUND).message(String.format(ErrorMessageFactory.get(ErrorReason.NOT_FOUND), e.getSource())).build());
        }
        return ResponseEntity.noContent().build();
    }
}
