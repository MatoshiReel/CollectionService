package ru.reel.CollectionService.unit.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.repository.CollectionRepository;
import ru.reel.CollectionService.repository.CollectionScopeRepository;
import ru.reel.CollectionService.repository.MovieRepository;
import ru.reel.CollectionService.repository.MovieStatusRepository;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.MovieService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("movie service")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class MovieServiceTests {
    @Autowired
    private MovieService service;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private MovieRepository movieRepository;
    private static CollectionScope scope;
    private static Collection collection1;
    private static Collection collection2;
    private static Collection collection3;
    private static Movie movie;
    private static Movie unlinkedMovie;
    private static MovieStatus status1;
    private static MovieStatus status2;

    @BeforeAll
    public static void init(@Autowired CollectionScopeRepository scopeRepository, @Autowired CollectionRepository collectionRepository, @Autowired MovieStatusRepository statusRepository, @Autowired MovieRepository movieRepository) {
        scope = new CollectionScope();
        scope.setTitle("Public");
        scope.setPriority((short)2);
        scopeRepository.save(scope);

        UUID ownerId = UUID.randomUUID();

        collection1 = new Collection();
        collection1.setName("collection_name");
        collection1.setOrder((short) 1);
        collection1.setOwnerId(ownerId);
        collection1.setScope(scope);
        collectionRepository.save(collection1);

        collection2 = new Collection();
        collection2.setName("collection2_name");
        collection2.setOrder((short) 2);
        collection2.setOwnerId(ownerId);
        collection2.setScope(scope);
        collectionRepository.save(collection2);

        collection3 = new Collection();
        collection3.setName("collection3_name");
        collection3.setOrder((short) 3);
        collection3.setOwnerId(ownerId);
        collection3.setScope(scope);
        collectionRepository.save(collection3);

        status1 = new MovieStatus();
        status1.setOrder((short)1);
        status1.setName("Movie");
        statusRepository.save(status1);

        status2 = new MovieStatus();
        status2.setOrder((short)2);
        status2.setName("Series");
        statusRepository.save(status2);

        movie = new Movie();
        movie.setOwnerRating(1.1);
        movie.setCatalogId(UUID.randomUUID());
        movie.setStatus(status1);
        movie.getCollections().add(collection1);

        unlinkedMovie = new Movie();
        unlinkedMovie.setOwnerRating(1.1);
        unlinkedMovie.setCatalogId(UUID.randomUUID());
        unlinkedMovie.setStatus(status1);
        movieRepository.save(unlinkedMovie);
    }

    @Nested
    @Order(1)
    @DisplayName("save")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SaveTests {
        @Test
        @Order(1)
        @DisplayName("save null")
        public void saveNullTest() {
            assertThrows(NullPointerException.class, () -> service.save(null, UUID.randomUUID().toString()));
        }

        @Test
        @Order(2)
        @DisplayName("save movie with null status")
        public void saveWithNullScopeTest() {
            Movie movie1 = new Movie();
            movie1.setStatus(null);
            assertThrows(NullPointerException.class, () -> service.save(movie1, UUID.randomUUID().toString()));
        }

        @Test
        @Order(3)
        @DisplayName("save valid movie")
        public void saveTest() throws SourceNotFoundException {
            assertThat(service.save(movie, collection1.getId().toString()), Matchers.notNullValue());
            Optional<Collection> savedCollection = collectionRepository.findById(collection1.getId());
            assertThat(savedCollection.isPresent(), Matchers.equalTo(true));
            assertThat(savedCollection.get().getMovies().contains(movie), Matchers.equalTo(true));
            Optional<Movie> savedMovie = movieRepository.findById(movie.getId());
            assertThat(savedMovie.isPresent(), Matchers.equalTo(true));
            assertThat(savedMovie.get().getCollections().contains(collection1), Matchers.equalTo(true));
        }

        @Test
        @Order(4)
        @DisplayName("save valid movie in the same collection")
        public void saveInTheSameCollectionTest() throws SourceNotFoundException {
            assertThat(service.save(movie, collection1.getId().toString()), Matchers.notNullValue());
            Optional<Collection> savedCollection = collectionRepository.findById(collection1.getId());
            assertThat(savedCollection.isPresent(), Matchers.equalTo(true));
            assertThat(savedCollection.get().getMovies().size(), Matchers.equalTo(1));
            Optional<Movie> savedMovie = movieRepository.findById(movie.getId());
            assertThat(savedMovie.isPresent(), Matchers.equalTo(true));
            assertThat(savedMovie.get().getCollections().size(), Matchers.equalTo(1));
        }

        @Test
        @Order(5)
        @DisplayName("save same movie in another collection")
        public void saveSameMovieInAnotherCollectionTest() throws SourceNotFoundException {
            assertThat(service.save(movie, collection2.getId().toString()), Matchers.notNullValue());
            Optional<Collection> savedCollection2 = collectionRepository.findById(collection2.getId());
            assertThat(savedCollection2.isPresent(), Matchers.equalTo(true));
            assertThat(savedCollection2.get().getMovies().size(), Matchers.equalTo(1));
            Optional<Movie> savedMovie = movieRepository.findById(movie.getId());
            assertThat(savedMovie.isPresent(), Matchers.equalTo(true));
            assertThat(savedMovie.get().getCollections().size(), Matchers.equalTo(2));
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
        @DisplayName("get not existing movie")
        public void getByNotExistingIdTest() {
            assertThrows(SourceNotFoundException.class, () -> service.getById(UUID.randomUUID().toString()));
            try {
                service.getById(UUID.randomUUID().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(MovieService.SOURCE_NAME));
            }
        }

        @Test
        @Order(4)
        @DisplayName("get movie")
        public void getByIdTest() throws SourceNotFoundException {
            Movie savedMovie = service.getById(movie.getId().toString());
            assertThat(savedMovie, Matchers.notNullValue());
            assertThat(savedMovie.getId(), Matchers.equalTo(movie.getId()));
        }
    }

    @Nested
    @Order(3)
    @DisplayName("get by catalogId")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class GetByCatalogIdTests {
        @Test
        @Order(1)
        @DisplayName("get by null catalogId")
        public void getByNullCatalogId() {
            assertThrows(NullPointerException.class, () -> service.getByCatalogIdAndOwnerId(null, UUID.randomUUID().toString()));
        }

        @Test
        @Order(2)
        @DisplayName("get by null ownerId")
        public void getByNullOwnerId() {
            assertThrows(NullPointerException.class, () -> service.getByCatalogIdAndOwnerId(UUID.randomUUID().toString(), null));
        }

        @Test
        @Order(3)
        @DisplayName("get by wrong catalogId")
        public void getByWrongCatalogId() {
            assertThrows(IllegalArgumentException.class, () -> service.getByCatalogIdAndOwnerId("wrong_id", UUID.randomUUID().toString()));
        }

        @Test
        @Order(4)
        @DisplayName("get by wrong ownerId")
        public void getByWrongOwnerId() {
            assertThrows(IllegalArgumentException.class, () -> service.getByCatalogIdAndOwnerId(UUID.randomUUID().toString(), "wrong_id"));
        }

        @Test
        @Order(5)
        @DisplayName("get not existing movie")
        public void getNotExistingMovieTest() {
            assertThrows(SourceNotFoundException.class, () -> service.getByCatalogIdAndOwnerId(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
            try {
                service.getByCatalogIdAndOwnerId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(MovieService.SOURCE_NAME));
            }
        }

        @Test
        @Order(6)
        @DisplayName("get movie")
        public void getByIdTest() throws SourceNotFoundException {
            Movie savedMovie = service.getByCatalogIdAndOwnerId(movie.getCatalogId().toString(), collection1.getOwnerId().toString());
            assertThat(savedMovie, Matchers.notNullValue());
            assertThat(savedMovie.getId(), Matchers.equalTo(movie.getId()));
        }
    }

    @Nested
    @Order(4)
    @DisplayName("get ownerId")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class GetOwnerIdTests {
        @Test
        @Order(1)
        @DisplayName("get by null")
        public void getByNullTest() {
            assertThrows(NullPointerException.class, () -> service.getOwnerId(null));
        }

        @Test
        @Order(2)
        @DisplayName("get by movie without linked any collections")
        public void getByMovieWithoutLinkedCollectionTest() {
            assertThrows(SourceNotFoundException.class, () -> service.getOwnerId(unlinkedMovie.getId().toString()));
            try {
                service.getOwnerId(unlinkedMovie.getId().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }

        @Test
        @Order(1)
        @DisplayName("get ownerId")
        public void getOwnerIdTest() throws SourceNotFoundException {
            String ownerId = service.getOwnerId(movie.getId().toString());
            assertThat(ownerId, Matchers.notNullValue());
            assertThat(ownerId, Matchers.equalTo(collection1.getOwnerId().toString()));
        }
    }

    @Nested
    @Order(5)
    @DisplayName("update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class UpdateTests {
        @Test
        @Order(1)
        @DisplayName("update null")
        public void updateNullTest() {
            assertThrows(NullPointerException.class, () -> service.update(null));
        }

        @Test
        @Order(2)
        @DisplayName("update empty")
        public void updateEmptyTest() throws SourceNotFoundException {
            Movie movie1 = new Movie();
            movie1.setId(movie.getId());
            String id = service.update(movie);
            assertThat(id, Matchers.notNullValue());
            assertThat(id, Matchers.equalTo(movie.getId().toString()));
            Movie updatedMovie = service.getById(id);
            assertThat(updatedMovie.getCatalogId(), Matchers.equalTo(movie.getCatalogId()));
            assertThat(updatedMovie.getOwnerRating(), Matchers.equalTo(movie.getOwnerRating()));
        }

        @Test
        @Order(3)
        @DisplayName("update ownerRating")
        public void saveOwnerRatingTest() throws SourceNotFoundException {
            Movie movie1 = new Movie();
            movie1.setId(movie.getId());
            movie1.setOwnerRating(3.3);
            String id = service.update(movie1);
            assertThat(id, Matchers.notNullValue());
            assertThat(id, Matchers.equalTo(movie.getId().toString()));
            Movie updatedMovie = service.getById(id);
            assertThat(updatedMovie.getCatalogId(), Matchers.equalTo(movie.getCatalogId()));
            assertThat(updatedMovie.getOwnerRating(), Matchers.not(Matchers.equalTo(movie.getOwnerRating())));
            assertThat(updatedMovie.getStatus().getId(), Matchers.equalTo(movie.getStatus().getId()));
            movie = updatedMovie;
        }

        @Test
        @Order(4)
        @DisplayName("update status by id")
        public void updateStatusByIdTest() throws SourceNotFoundException {
            MovieStatus status = new MovieStatus();
            status.setId(status2.getId());
            Movie movie1 = new Movie();
            movie1.setId(movie.getId());
            movie1.setStatus(status);
            String id = service.update(movie1);
            assertThat(id, Matchers.notNullValue());
            assertThat(id, Matchers.equalTo(movie.getId().toString()));
            Movie updatedMovie = service.getById(id);
            assertThat(updatedMovie.getCatalogId(), Matchers.equalTo(movie.getCatalogId()));
            assertThat(updatedMovie.getOwnerRating(), Matchers.equalTo(movie.getOwnerRating()));
            assertThat(updatedMovie.getStatus().getId(), Matchers.equalTo(status2.getId()));
            movie = updatedMovie;
        }

        @Test
        @Order(5)
        @DisplayName("update status by id")
        public void updateStatusByOrderTest() throws SourceNotFoundException {
            MovieStatus status = new MovieStatus();
            status.setOrder(status1.getOrder());
            Movie movie1 = new Movie();
            movie1.setId(movie.getId());
            movie1.setStatus(status);
            String id = service.update(movie1);
            assertThat(id, Matchers.notNullValue());
            assertThat(id, Matchers.equalTo(movie.getId().toString()));
            Movie updatedMovie = service.getById(id);
            assertThat(updatedMovie.getCatalogId(), Matchers.equalTo(movie.getCatalogId()));
            assertThat(updatedMovie.getOwnerRating(), Matchers.equalTo(movie.getOwnerRating()));
            assertThat(updatedMovie.getStatus().getId(), Matchers.equalTo(status1.getId()));
            movie = updatedMovie;
        }

        @Test
        @Order(6)
        @DisplayName("update catalogId")
        public void updateCatalogIdTest() throws SourceNotFoundException {
            Movie movie1 = new Movie();
            movie1.setId(movie.getId());
            movie1.setCatalogId(UUID.randomUUID());
            String id = service.update(movie1);
            assertThat(id, Matchers.notNullValue());
            assertThat(id, Matchers.equalTo(movie.getId().toString()));
            Movie updatedMovie = service.getById(id);
            assertThat(updatedMovie.getCatalogId(), Matchers.not(Matchers.equalTo(movie1.getCatalogId())));
            assertThat(updatedMovie.getOwnerRating(), Matchers.equalTo(movie.getOwnerRating()));
            assertThat(updatedMovie.getStatus().getId(), Matchers.equalTo(movie.getStatus().getId()));
            movie = updatedMovie;
        }
    }

    @Nested
    @Order(6)
    @DisplayName("delete")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class DeleteTests {
        @Test
        @Order(1)
        @DisplayName("delete by null")
        public void deleteByNullTest() {
            assertThrows(NullPointerException.class, () -> service.deleteById(null));
        }

        @Test
        @Order(2)
        @DisplayName("delete by wrong id")
        public void deleteByWrongIdTest() {
            assertThrows(IllegalArgumentException.class, () -> service.deleteById("wrong_id"));
        }

        @Test
        @Order(3)
        @DisplayName("delete relation by unlinked entities")
        public void deleteRelationByUnlinkedEntitiesTest() throws SourceNotFoundException {
            assertThat(movie.getCollections().contains(collection3), Matchers.equalTo(false));
            service.deleteCollectionRelationById(movie.getId().toString(), collection3.getId().toString());
            assertThat(service.getById(movie.getId().toString()), Matchers.notNullValue());
            assertThat(collectionRepository.findById(collection3.getId()).isPresent(), Matchers.equalTo(true));
        }

        @Test
        @Order(4)
        @DisplayName("delete relation by linked entities")
        public void deleteRelationByLinkedEntitiesTest() throws SourceNotFoundException {
            assertThat(movie.getCollections().contains(collection1), Matchers.equalTo(true));
            service.deleteCollectionRelationById(movie.getId().toString(), collection1.getId().toString());
            Movie movie1 = service.getById(movie.getId().toString());
            assertThat(movie1, Matchers.notNullValue());
            assertThat(collectionRepository.findById(collection1.getId()).isPresent(), Matchers.equalTo(true));
            assertThat(movie1.getCollections().contains(collection1), Matchers.equalTo(false));
        }

        @Test
        @Order(5)
        @DisplayName("delete relation and movie by linked entities")
        public void deleteRelationAndMovieByLinkedEntitiesTest() throws SourceNotFoundException {
            assertThat(movie.getCollections().contains(collection2), Matchers.equalTo(true));
            service.deleteCollectionRelationById(movie.getId().toString(), collection2.getId().toString());
            assertThrows(SourceNotFoundException.class, () -> service.getById(movie.getId().toString()));
            assertThat(collectionRepository.findById(collection2.getId()).isPresent(), Matchers.equalTo(true));
        }
    }

    @AfterAll
    public static void destroy(@Autowired CollectionScopeRepository scopeRepository, @Autowired CollectionRepository collectionRepository, @Autowired MovieStatusRepository statusRepository, @Autowired MovieRepository movieRepository) {
        collectionRepository.delete(collection1);
        collectionRepository.delete(collection2);
        collectionRepository.delete(collection3);
        movieRepository.delete(movie);
        movieRepository.delete(unlinkedMovie);
        scopeRepository.delete(scope);
        statusRepository.delete(status1);
        statusRepository.delete(status2);
    }
}