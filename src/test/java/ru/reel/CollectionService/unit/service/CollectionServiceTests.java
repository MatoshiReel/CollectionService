package ru.reel.CollectionService.unit.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.repository.CollectionRepository;
import ru.reel.CollectionService.repository.CollectionScopeRepository;
import ru.reel.CollectionService.repository.MovieRepository;
import ru.reel.CollectionService.repository.MovieStatusRepository;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection service")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class CollectionServiceTests {
    @Autowired
    private CollectionService service;
    private static MovieStatus status;
    private static CollectionScope scope1;
    private static CollectionScope scope2;
    private static Collection collection;

    @BeforeAll
    public static void init(@Autowired MovieStatusRepository statusRepository, @Autowired MovieRepository movieRepository, @Autowired CollectionScopeRepository scopeRepository) {
        status = new MovieStatus();
        status.setName("status_name");
        status.setOrder((short) 1);
        statusRepository.save(status);

        scope1 = new CollectionScope();
        scope1.setTitle("Private");
        scope1.setPriority((short)1);
        scopeRepository.save(scope1);

        scope2 = new CollectionScope();
        scope2.setTitle("Public");
        scope2.setPriority((short)2);
        scopeRepository.save(scope2);

        collection = new Collection();
        collection.setName("collection_name");
        collection.setOrder((short) 1);
        collection.setOwnerId(UUID.randomUUID());
        collection.setScope(scope1);
    }

    @Nested
    @Order(1)
    @DisplayName("save")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SaveTests {
        @Test
        @Order(1)
        @DisplayName("save null collection")
        public void saveNullCollectionTest() {
            assertThrows(NullPointerException.class, () -> service.save(null));
        }

        @Test
        @Order(2)
        @DisplayName("save collection with null scope")
        public void saveCollectionWithNullScopeTest() {
            Collection collection1 = new Collection();
            collection1.setName(collection.getName());
            collection1.setOrder(collection.getOrder());
            collection1.setScope(null);
            assertThrows(NullPointerException.class, () -> service.save(collection1));
        }

        @Test
        @Order(3)
        @DisplayName("save collection")
        public void saveCollectionTest() throws SourceNotFoundException {
            assertThat(service.save(collection), Matchers.notNullValue());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("get by id")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class GetByIdTests {
        @Test
        @Order(1)
        @DisplayName("get by null id")
        public void getByNullId() {
            assertThrows(NullPointerException.class, () -> service.getById(null));
        }

        @Test
        @Order(2)
        @DisplayName("get by wrong id")
        public void getByWrongId() {
            assertThrows(IllegalArgumentException.class, () -> service.getById("wrong_id"));
        }

        @Test
        @Order(3)
        @DisplayName("get by not existing id")
        public void getByNotExistingId() {
            assertThrows(SourceNotFoundException.class, () -> service.getById(UUID.randomUUID().toString()));
            try {
                service.getById(UUID.randomUUID().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }

        @Test
        @Order(4)
        @DisplayName("get by id")
        public void getById() throws SourceNotFoundException {
            assertThat(service.getById(collection.getId().toString()), Matchers.notNullValue());
        }
    }

    @Nested
    @Order(3)
    @DisplayName("get by owner id")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class GetByOwnerIdTests {
        @Test
        @Order(1)
        @DisplayName("get by null owner id")
        public void getByNullOwnerId() {
            assertThrows(NullPointerException.class, () -> service.getByOwnerId(null));
        }

        @Test
        @Order(2)
        @DisplayName("get by wrong owner id")
        public void getByWrongOwnerId() {
            assertThrows(IllegalArgumentException.class, () -> service.getByOwnerId("wrong_id"));
        }

        @Test
        @Order(3)
        @DisplayName("get by not existing owner id")
        public void getByNotExistingOwnerId() {
            assertThrows(SourceNotFoundException.class, () -> service.getByOwnerId(UUID.randomUUID().toString()));
            try {
                service.getByOwnerId(UUID.randomUUID().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }

        @Test
        @Order(4)
        @DisplayName("get by owner id")
        public void getByOwnerId() throws SourceNotFoundException {
            assertThat(service.getByOwnerId(collection.getOwnerId().toString()), Matchers.not(Matchers.empty()));
        }
    }

    @Nested
    @Order(4)
    @DisplayName("update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class UpdateTests {
        @Test
        @Order(1)
        @DisplayName("update null collection")
        public void updateNullCollectionTest() {
            assertThrows(NullPointerException.class, () -> service.update(null));
        }

        @Test
        @Order(2)
        @DisplayName("update not existing collection")
        public void updateNotExistingCollectionTest() {
            Collection notExistingCollection = new Collection();
            notExistingCollection.setId(UUID.randomUUID());
            notExistingCollection.setName(collection.getName());
            assertThrows(SourceNotFoundException.class, () -> service.update(notExistingCollection));
            try {
                service.update(notExistingCollection);
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }

        @Test
        @Order(3)
        @DisplayName("update empty collection")
        public void updateEmptyCollectionTest() {
            assertThrows(NullPointerException.class, () -> service.update(new Collection()));
        }

        @Test
        @Order(4)
        @DisplayName("update collection name")
        public void updateCollectionNameTest() throws SourceNotFoundException {
            String newName = "new_name";
            collection.setName(newName);
            Collection updatedCollection = service.getById(service.update(collection));
            assertThat(collection.getName(), Matchers.equalTo(updatedCollection.getName()));
        }

        @Test
        @Order(5)
        @DisplayName("update collection order")
        public void updateCollectionOrderTest() throws SourceNotFoundException {
            short newOrder = 2;
            collection.setOrder(newOrder);
            Collection updatedCollection = service.getById(service.update(collection));
            assertThat(collection.getOrder(), Matchers.equalTo(updatedCollection.getOrder()));
        }

        @Test
        @Order(6)
        @DisplayName("update collection scope by id")
        public void updateCollectionScopeByIdTest() throws SourceNotFoundException {
            CollectionScope newScope = new CollectionScope();
            newScope.setId(scope2.getId());
            collection.setScope(scope2);
            Collection updatedCollection1 = service.getById(service.update(collection));
            assertThat(collection.getScope().getId(), Matchers.equalTo(updatedCollection1.getScope().getId()));
        }

        @Test
        @Order(6)
        @DisplayName("update collection scope by priority")
        public void updateCollectionScopeByPriorityTest() throws SourceNotFoundException {
            CollectionScope newScope = new CollectionScope();
            newScope.setPriority(scope2.getPriority());
            collection.setScope(scope2);
            Collection updatedCollection1 = service.getById(service.update(collection));
            assertThat(collection.getScope().getId(), Matchers.equalTo(updatedCollection1.getScope().getId()));
        }
    }

    @Nested
    @Order(5)
    @DisplayName("delete by id")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class DeleteTests {
        @Test
        @Order(1)
        @DisplayName("delete by null id")
        public void deleteByNullIdTest() {
           assertThrows(NullPointerException.class, () ->  service.deleteById(null));
        }

        @Test
        @Order(2)
        @DisplayName("delete by wrong id")
        public void deleteByWrongIdTest() {
            assertThrows(IllegalArgumentException.class, () ->  service.deleteById(""));
            assertThrows(IllegalArgumentException.class, () ->  service.deleteById("wrong_id"));
        }

        @Test
        @Order(3)
        @DisplayName("delete by not existing id")
        public void deleteByNotExistingIdTest() {
            service.deleteById(UUID.randomUUID().toString());
        }

        @Test
        @Order(4)
        @DisplayName("delete by id")
        public void deleteByIdTest() {
            service.deleteById(collection.getId().toString());
        }
    }

    @AfterAll
    public static void destroy(@Autowired MovieStatusRepository statusRepository, @Autowired MovieRepository movieRepository, @Autowired CollectionScopeRepository scopeRepository, @Autowired CollectionRepository collectionRepository) {
        collectionRepository.delete(collection);
        scopeRepository.delete(scope1);
        scopeRepository.delete(scope2);
        statusRepository.delete(status);
    }
}
