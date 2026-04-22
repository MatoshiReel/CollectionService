package ru.reel.CollectionService.unit.mapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.dto.CollectionScopeDto;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.entity.*;
import ru.reel.CollectionService.mapper.CollectionMapper;
import ru.reel.CollectionService.mapper.MovieMapper;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection mapper")
public class CollectionMapperTests {
    @Autowired
    private CollectionMapper mapper;
    @Autowired
    private MovieMapper movieMapper;

    @Nested
    @DisplayName("from")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class FromTests {
        @Test
        @Order(1)
        @DisplayName("Convert null DTO to Entity")
        public void convertNullDtoObjTest() {
            assertThat(mapper.from(null), Matchers.nullValue());
        }

        @Test
        @Order(2)
        @DisplayName("Convert empty DTO to Entity")
        public void convertEmptyDtoObjTest() {
            CollectionDto dto = new CollectionDto();
            Collection entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getName(), Matchers.nullValue());
            assertThat(entity.getOrder(), Matchers.equalTo((short)0));
            assertThat(entity.getCreatedAt(), Matchers.notNullValue());
            assertThat(entity.getOwnerId(), Matchers.nullValue());
            assertThat(entity.getScope(), Matchers.nullValue());
            assertThat(entity.getMovies(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full DTO to Entity")
        public void convertFullDtoObjTest() {
            String id = UUID.randomUUID().toString();
            String name = "name";
            Date createdAt = new Date();
            String ownerId = UUID.randomUUID().toString();
            CollectionScopeDto collectionScopeDto = new CollectionScopeDto();
            collectionScopeDto.title = "sc_name";
            MovieDto movieDto = new MovieDto();
            movieDto.ownerRating = 1.1;

            CollectionDto dto = new CollectionDto();
            dto.id = id;
            dto.name = name;
            dto.order = 1;
            dto.ownerId = ownerId;
            dto.scope = collectionScopeDto;
            dto.movies.add(movieDto);

            Collection entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getName(), Matchers.equalTo(name));
            assertThat(entity.getOrder(), Matchers.equalTo((short)1));
            assertThat(entity.getCreatedAt(), Matchers.notNullValue());
            assertThat(entity.getOwnerId(), Matchers.equalTo(UUID.fromString(ownerId)));
            assertThat(entity.getScope(), Matchers.notNullValue());
            assertThat(entity.getScope().getTitle(), Matchers.equalTo(collectionScopeDto.title));
            assertThat(entity.getMovies(), Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("Convert full DTO with id include to Entity")
        public void convertFullDtoObjWithIdIncludeTest() {
            String id = UUID.randomUUID().toString();
            String name = "name";
            Date createdAt = new Date(System.currentTimeMillis() + 1000);
            String ownerId = UUID.randomUUID().toString();
            CollectionScopeDto collectionScopeDto = new CollectionScopeDto();
            collectionScopeDto.title = "sc_name";
            MovieDto movieDto = new MovieDto();
            movieDto.ownerRating = 1.1;

            CollectionDto dto = new CollectionDto();
            dto.id = id;
            dto.name = name;
            dto.order = 1;
            dto.createdAt = createdAt;
            dto.ownerId = ownerId;
            dto.scope = collectionScopeDto;
            dto.movies.add(movieDto);

            Collection entity = mapper.from(dto, true);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.equalTo(UUID.fromString(id)));
            assertThat(entity.getName(), Matchers.equalTo(name));
            assertThat(entity.getOrder(), Matchers.equalTo((short)1));
            assertThat(entity.getCreatedAt(), Matchers.not(Matchers.equalTo(createdAt)));
            assertThat(entity.getOwnerId(), Matchers.equalTo(UUID.fromString(ownerId)));
            assertThat(entity.getScope(), Matchers.not(Matchers.nullValue()));
            assertThat(entity.getScope().getTitle(), Matchers.equalTo(collectionScopeDto.title));
            assertThat(entity.getMovies(), Matchers.empty());
        }

        @Test
        @Order(5)
        @DisplayName("Convert DTO with bad UUID format to Entity")
        public void convertDtoObjWithBadUuidTest() {
            CollectionDto dto = new CollectionDto();
            dto.ownerId = "some_text";
            assertThrows(IllegalArgumentException.class, () -> mapper.from(dto));
        }
    }

    @Nested
    @DisplayName("to")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class ToTests {
        @Test
        @Order(1)
        @DisplayName("Convert null Entity to DTO")
        public void convertNullEntityObjTest() {
            assertThat(mapper.to(null), Matchers.nullValue());
        }

        @Test
        @Order(2)
        @DisplayName("Convert empty Entity to DTO")
        public void convertEmptyEntityObjTest() {
            Collection entity = new Collection();
            CollectionDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.nullValue());
            assertThat(dto.name, Matchers.nullValue());
            assertThat(dto.order, Matchers.equalTo((short)0));
            assertThat(dto.createdAt, Matchers.notNullValue());
            assertThat(dto.ownerId, Matchers.nullValue());
            assertThat(dto.ownerId, Matchers.nullValue());
            assertThat(dto.scope, Matchers.nullValue());
            assertThat(dto.movies, Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full Entity to DTO")
        public void convertFullEntityObjTest() {
            UUID id = UUID.randomUUID();
            String name = "name";
            Date createdAt = new Date();
            UUID ownerId = UUID.randomUUID();
            CollectionScope collectionScope = new CollectionScope();
            collectionScope.setTitle("sc_name");
            Movie movie = new Movie();
            movie.setOwnerRating(1.1);

            Collection entity = new Collection();
            entity.setId(id);
            entity.setName(name);
            entity.setOrder((short)1);
            entity.setCreatedAt(createdAt);
            entity.setOwnerId(ownerId);
            entity.setScope(collectionScope);
            entity.getMovies().add(movie);

            CollectionDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.name, Matchers.equalTo(name));
            assertThat(dto.order, Matchers.equalTo((short)1));
            assertThat(dto.createdAt, Matchers.equalTo(createdAt));
            assertThat(dto.scope, Matchers.notNullValue());
            assertThat(dto.scope.title, Matchers.equalTo(collectionScope.getTitle()));
            assertThat(dto.movies, Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("Convert full Entity with deep mapping but null mapper obj to DTO")
        public void convertFullEntityObjWithDeepMappingAndNullCollectionMapperTest() {
            UUID id = UUID.randomUUID();
            String name = "name";
            Date createdAt = new Date();
            UUID ownerId = UUID.randomUUID();
            CollectionScope collectionScope = new CollectionScope();
            collectionScope.setTitle("sc_name");
            Movie movie = new Movie();
            movie.setOwnerRating(1.1);

            Collection entity = new Collection();
            entity.setId(id);
            entity.setName(name);
            entity.setOrder((short)1);
            entity.setCreatedAt(createdAt);
            entity.setOwnerId(ownerId);
            entity.setScope(collectionScope);
            entity.getMovies().add(movie);

            CollectionDto dto = mapper.to(entity, true, null);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.name, Matchers.equalTo(name));
            assertThat(dto.order, Matchers.equalTo((short)1));
            assertThat(dto.createdAt, Matchers.equalTo(createdAt));
            assertThat(dto.scope, Matchers.notNullValue());
            assertThat(dto.scope.title, Matchers.equalTo(collectionScope.getTitle()));
            assertThat(dto.movies, Matchers.empty());
        }

        @Test
        @Order(5)
        @DisplayName("Convert full Entity with deep mapping but empty movies set to DTO")
        public void convertFullEntityObjWithDeepMappingAndEmptyCollectionsSetTest() {
            UUID id = UUID.randomUUID();
            String name = "name";
            Date createdAt = new Date();
            UUID ownerId = UUID.randomUUID();
            CollectionScope collectionScope = new CollectionScope();
            collectionScope.setTitle("sc_name");

            Collection entity = new Collection();
            entity.setId(id);
            entity.setName(name);
            entity.setOrder((short)1);
            entity.setCreatedAt(createdAt);
            entity.setOwnerId(ownerId);
            entity.setScope(collectionScope);

            CollectionDto dto = mapper.to(entity, true, movieMapper);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.name, Matchers.equalTo(name));
            assertThat(dto.order, Matchers.equalTo((short)1));
            assertThat(dto.createdAt, Matchers.equalTo(createdAt));
            assertThat(dto.scope, Matchers.notNullValue());
            assertThat(dto.scope.title, Matchers.equalTo(collectionScope.getTitle()));
            assertThat(dto.movies, Matchers.empty());
        }

        @Test
        @Order(6)
        @DisplayName("Convert full Entity with deep mapping mapper obj to DTO")
        public void convertFullEntityObjWithDeepMappingTest() {
            UUID id = UUID.randomUUID();
            String name = "name";
            Date createdAt = new Date();
            UUID ownerId = UUID.randomUUID();
            CollectionScope collectionScope = new CollectionScope();
            collectionScope.setTitle("sc_name");
            Movie movie = new Movie();
            movie.setOwnerRating(1.1);

            Collection entity = new Collection();
            entity.setId(id);
            entity.setName(name);
            entity.setOrder((short)1);
            entity.setCreatedAt(createdAt);
            entity.setOwnerId(ownerId);
            entity.setScope(collectionScope);
            entity.getMovies().add(movie);

            CollectionDto dto = mapper.to(entity, true, movieMapper);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.name, Matchers.equalTo(name));
            assertThat(dto.order, Matchers.equalTo((short)1));
            assertThat(dto.createdAt, Matchers.equalTo(createdAt));
            assertThat(dto.scope, Matchers.notNullValue());
            assertThat(dto.scope.title, Matchers.equalTo(collectionScope.getTitle()));
            assertThat(dto.movies, Matchers.not(Matchers.empty()));
            assertThat(dto.movies.stream().toList().getFirst().ownerRating, Matchers.equalTo(movie.getOwnerRating()));
        }
    }
}
