package ru.reel.CollectionService.unit.mapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.dto.MovieStatusDto;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.mapper.CollectionMapper;
import ru.reel.CollectionService.mapper.MovieMapper;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("movie mapper")
public class MovieMapperTests {
    @Autowired
    private MovieMapper mapper;
    @Autowired
    private CollectionMapper collectionMapper;

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
            MovieDto dto = new MovieDto();
            Movie entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getCatalogId(), Matchers.nullValue());
            assertThat(entity.getOwnerRating(), Matchers.equalTo(0.0));
            assertThat(entity.getStatus(), Matchers.nullValue());
            assertThat(entity.getCollections(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full DTO to Entity")
        public void convertFullDtoObjTest() {
            String id = UUID.randomUUID().toString();
            String catalogId = UUID.randomUUID().toString();
            MovieStatusDto movieStatusDto = new MovieStatusDto();
            movieStatusDto.name = "mov_name";
            CollectionDto collectionDto = new CollectionDto();
            collectionDto.name = "coll_name";

            MovieDto dto = new MovieDto();
            dto.id = id;
            dto.catalogId = catalogId;
            dto.ownerRating = 1.1;
            dto.status = movieStatusDto;
            dto.collections.add(collectionDto);

            Movie entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getCatalogId(), Matchers.equalTo(UUID.fromString(catalogId)));
            assertThat(entity.getOwnerRating(), Matchers.equalTo(1.1));
            assertThat(entity.getStatus(), Matchers.notNullValue());
            assertThat(entity.getStatus().getName(), Matchers.equalTo(movieStatusDto.name));
            assertThat(entity.getCollections(), Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("Convert full DTO with id include to Entity")
        public void convertFullDtoObjWithIdIncludeTest() {
            String id = UUID.randomUUID().toString();
            String catalogId = UUID.randomUUID().toString();
            MovieStatusDto movieStatusDto = new MovieStatusDto();
            movieStatusDto.name = "mov_name";
            CollectionDto collectionDto = new CollectionDto();
            collectionDto.name = "coll_name";

            MovieDto dto = new MovieDto();
            dto.id = id;
            dto.catalogId = catalogId;
            dto.ownerRating = 1.1;
            dto.status = movieStatusDto;
            dto.collections.add(collectionDto);

            Movie entity = mapper.from(dto, true);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.equalTo(UUID.fromString(id)));
            assertThat(entity.getCatalogId(), Matchers.equalTo(UUID.fromString(catalogId)));
            assertThat(entity.getOwnerRating(), Matchers.equalTo(1.1));
            assertThat(entity.getStatus(), Matchers.notNullValue());
            assertThat(entity.getStatus().getName(), Matchers.equalTo(movieStatusDto.name));
            assertThat(entity.getCollections(), Matchers.empty());
        }

        @Test
        @Order(5)
        @DisplayName("Convert DTO with bad UUID format to Entity")
        public void convertDtoObjWithBadUuidTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = "some_text";
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
            Movie entity = new Movie();
            MovieDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.nullValue());
            assertThat(dto.catalogId, Matchers.nullValue());
            assertThat(dto.ownerRating, Matchers.equalTo(0.0));
            assertThat(dto.status, Matchers.nullValue());
            assertThat(dto.collections, Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full Entity to DTO")
        public void convertFullEntityObjTest() {
            UUID id = UUID.randomUUID();
            UUID catalogId = UUID.randomUUID();
            MovieStatus movieStatus = new MovieStatus();
            movieStatus.setName("mov_name");
            Collection collection = new Collection();
            collection.setName("coll_name");

            Movie entity = new Movie();
            entity.setId(id);
            entity.setCatalogId(catalogId);
            entity.setOwnerRating(1.1);
            entity.setStatus(movieStatus);
            entity.getCollections().add(collection);

            MovieDto dto = mapper.to(entity);
            assertThat(entity, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.catalogId, Matchers.equalTo(catalogId.toString()));
            assertThat(dto.ownerRating, Matchers.equalTo(1.1));
            assertThat(dto.status, Matchers.notNullValue());
            assertThat(dto.status.name, Matchers.equalTo(movieStatus.getName()));
            assertThat(dto.collections, Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("Convert full Entity with deep mapping but null mapper obj to DTO")
        public void convertFullEntityObjWithDeepMappingAndNullCollectionMapperTest() {
            UUID id = UUID.randomUUID();
            UUID catalogId = UUID.randomUUID();
            MovieStatus movieStatus = new MovieStatus();
            movieStatus.setName("mov_name");
            Collection collection = new Collection();
            collection.setName("coll_name");

            Movie entity = new Movie();
            entity.setId(id);
            entity.setCatalogId(catalogId);
            entity.setOwnerRating(1.1);
            entity.setStatus(movieStatus);
            entity.getCollections().add(collection);

            MovieDto dto = mapper.to(entity, true, null);
            assertThat(entity, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.catalogId, Matchers.equalTo(catalogId.toString()));
            assertThat(dto.ownerRating, Matchers.equalTo(1.1));
            assertThat(dto.status, Matchers.notNullValue());
            assertThat(dto.status.name, Matchers.equalTo(movieStatus.getName()));
            assertThat(dto.collections, Matchers.empty());
        }

        @Test
        @Order(5)
        @DisplayName("Convert full Entity with deep mapping but empty collections set to DTO")
        public void convertFullEntityObjWithDeepMappingAndEmptyCollectionsSetTest() {
            UUID id = UUID.randomUUID();
            UUID catalogId = UUID.randomUUID();
            MovieStatus movieStatus = new MovieStatus();
            movieStatus.setName("mov_name");

            Movie entity = new Movie();
            entity.setId(id);
            entity.setCatalogId(catalogId);
            entity.setOwnerRating(1.1);
            entity.setStatus(movieStatus);

            MovieDto dto = mapper.to(entity, true, collectionMapper);
            assertThat(entity, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.catalogId, Matchers.equalTo(catalogId.toString()));
            assertThat(dto.ownerRating, Matchers.equalTo(1.1));
            assertThat(dto.status, Matchers.notNullValue());
            assertThat(dto.status.name, Matchers.equalTo(movieStatus.getName()));
            assertThat(dto.collections, Matchers.empty());
        }

        @Test
        @Order(6)
        @DisplayName("Convert full Entity with deep mapping mapper obj to DTO")
        public void convertFullEntityObjWithDeepMappingTest() {
            UUID id = UUID.randomUUID();
            UUID catalogId = UUID.randomUUID();
            MovieStatus movieStatus = new MovieStatus();
            movieStatus.setName("mov_name");
            Collection collection = new Collection();
            collection.setName("coll_name");

            Movie entity = new Movie();
            entity.setId(id);
            entity.setCatalogId(catalogId);
            entity.setOwnerRating(1.1);
            entity.setStatus(movieStatus);
            entity.getCollections().add(collection);

            MovieDto dto = mapper.to(entity, true, collectionMapper);
            assertThat(entity, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.catalogId, Matchers.equalTo(catalogId.toString()));
            assertThat(dto.ownerRating, Matchers.equalTo(1.1));
            assertThat(dto.status, Matchers.notNullValue());
            assertThat(dto.status.name, Matchers.equalTo(movieStatus.getName()));
            assertThat(dto.collections, Matchers.not(Matchers.empty()));
            assertThat(dto.collections.stream().toList().getFirst().name, Matchers.equalTo(collection.getName()));
        }
    }
}