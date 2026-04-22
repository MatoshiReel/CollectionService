package ru.reel.CollectionService.unit.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.repository.CollectionScopeRepository;
import ru.reel.CollectionService.service.CollectionScopeService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection scope service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CollectionScopeServiceTests {
    @Autowired
    private CollectionScopeService service;
    private static UUID SCOPE_ID;
    private static CollectionScope scope1;
    private static CollectionScope scope2;

    @BeforeAll
    static void init(@Autowired CollectionScopeRepository repository) {
        scope1 = new CollectionScope();
        scope1.setTitle("Private");
        scope1.setPriority((short)1);
        repository.save(scope1);
        SCOPE_ID = scope1.getId();
        scope2 = new CollectionScope();
        scope2.setTitle("Public");
        scope2.setPriority((short)2);
        repository.save(scope2);
    }

    @Test
    @Order(1)
    @DisplayName("get all")
    public void getAllTest() {
        assertThat(service.getAll(), Matchers.not(Matchers.empty()));
    }

    @Test
    @Order(2)
    @DisplayName("get by null id")
    public void getByNullIdTest() {
        assertThrows(NullPointerException.class, () -> service.getById(null));
    }

    @Test
    @Order(3)
    @DisplayName("get by wrong id")
    public void getByWrongIdTest() {
        assertThrows(IllegalArgumentException.class, () -> service.getById("wrong_id"));
    }

    @Test
    @Order(4)
    @DisplayName("get by valid id but not existing scope")
    public void getByValidIdNotExistingScopeTest() {
        assertThrows(SourceNotFoundException.class, () -> service.getById(UUID.randomUUID().toString()));
        try {
            service.getById(UUID.randomUUID().toString());
        } catch (SourceNotFoundException e) {
            assertThat(e.getSource(), Matchers.equalTo(CollectionScopeService.SOURCE_NAME));
        }
    }

    @Test
    @Order(5)
    @DisplayName("get by valid id")
    public void getByValidIdTest() throws SourceNotFoundException {
        assertThat(service.getById(SCOPE_ID.toString()), Matchers.notNullValue());
    }

    @Test
    @Order(6)
    @DisplayName("get by priority but not existing scope")
    public void getByValidPriorityNotExistingScopeTest() {
        assertThrows(SourceNotFoundException.class, () -> service.getByPriority((short) 0));
        try {
            service.getByPriority((short) 0);
        } catch (SourceNotFoundException e) {
            assertThat(e.getSource(), Matchers.equalTo(CollectionScopeService.SOURCE_NAME));
        }
    }

    @Test
    @Order(7)
    @DisplayName("get by valid priority")
    public void getByValidPriorityTest() throws SourceNotFoundException {
        assertThat(service.getByPriority((short) 1), Matchers.notNullValue());
    }

    @AfterAll
    public static void destroy(@Autowired CollectionScopeRepository repository) {
        repository.delete(scope1);
        repository.delete(scope2);
    }
}