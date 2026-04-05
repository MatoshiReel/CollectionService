package ru.reel.CollectionService.service;

import org.springframework.stereotype.Service;
import ru.reel.CollectionService.entity.Permission;
import ru.reel.CollectionService.repository.PermissionRepository;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository repository;

    public PermissionService(PermissionRepository repository) {
        this.repository = repository;
    }

    public List<Permission> getAll() {
        return (List<Permission>) repository.findAll();
    }
}
