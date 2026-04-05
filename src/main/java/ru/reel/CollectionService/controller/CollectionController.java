package ru.reel.CollectionService.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.reel.CollectionService.dto.MovieStatusDto;
import ru.reel.CollectionService.dto.PermissionDto;
import ru.reel.CollectionService.mapper.MovieStatusMapper;
import ru.reel.CollectionService.mapper.PermissionMapper;
import ru.reel.CollectionService.service.MovieStatusService;
import ru.reel.CollectionService.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController {
    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;
    private final MovieStatusService movieStatusService;
    private final MovieStatusMapper movieStatusMapper;


    public CollectionController(PermissionService permissionService, PermissionMapper permissionMapper, MovieStatusService movieStatusService, MovieStatusMapper movieStatusMapper) {
        this.permissionService = permissionService;
        this.permissionMapper = permissionMapper;
        this.movieStatusService = movieStatusService;
        this.movieStatusMapper = movieStatusMapper;
    }

    @PostMapping(value = "/")
    public ResponseEntity<Object> createCollection(@RequestBody(required = false) String collectionData, @RequestHeader("X-Account-Id") String accountId) {
        return null;
    }

    @GetMapping(value = "/permission/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PermissionDto>> getCollectionPermissionList() {
        return ResponseEntity.ok(permissionService.getAll().stream().map(permissionMapper::to).toList());
    }

    @GetMapping(value = "/movie/status/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieStatusDto>> getMovieStatusList() {
        return ResponseEntity.ok(movieStatusService.getAll().stream().map(movieStatusMapper::to).toList());
    }
}
