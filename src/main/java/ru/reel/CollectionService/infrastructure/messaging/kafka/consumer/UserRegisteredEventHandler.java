package ru.reel.CollectionService.infrastructure.messaging.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.service.CollectionScopeService;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

@Component
public class UserRegisteredEventHandler {
    private final CollectionService collectionService;
    private final CollectionScopeService collectionScopeService;

    public UserRegisteredEventHandler(CollectionService collectionService, CollectionScopeService collectionScopeService) {
        this.collectionService = collectionService;
        this.collectionScopeService = collectionScopeService;
    }

    @KafkaListener(topics = "user-registered")
    public void handle(String userId) throws SourceNotFoundException {
        Collection collection = new Collection();
        collection.setName("Избранное");
        collection.setOwnerId(userId);
        collection.setOrder((short) 1);
        collection.setScope(collectionScopeService.getByPriority((short) 1));
        collectionService.save(collection);
    }
}
