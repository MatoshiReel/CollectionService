package ru.reel.CollectionService.unit.service.validator;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.dto.CollectionScopeDto;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.FieldRequestError;
import ru.reel.CollectionService.service.validator.CollectionValidator;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@DisplayName("collection validator")
public class CollectionValidatorTests {
    @Autowired
    private CollectionValidator validator;

    @Nested
    @DisplayName("validate before create")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class ValidateBeforeCreateTests {
        @Test
        @Order(1)
        @DisplayName("validate null/empty name")
        public void validateNullAndEmptyNameTest() {
            CollectionDto dto = new CollectionDto();
            dto.name = null;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));

            dto.name = "";
            errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(2)
        @DisplayName("validate less size name")
        public void validateLessSizeNameTest() {
            CollectionDto dto = new CollectionDto();
            StringBuilder name = new StringBuilder();
            for(int i = 0; i < CollectionValidator.MIN_NAME_SIZE-1; i++) {
                name.append(i);
            }
            dto.name = name.toString();

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }

        @Test
        @Order(3)
        @DisplayName("validate greater size name")
        public void validateGreaterSizeNameTest() {
            CollectionDto dto = new CollectionDto();
            StringBuilder name = new StringBuilder();
            for(int i = 0; i < CollectionValidator.MAX_NAME_SIZE+1; i++) {
                name.append(i);
            }
            dto.name = name.toString();

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.GREATER_SIZE));
        }

        @Test
        @Order(4)
        @DisplayName("validate bad patterned name")
        public void validateBadPatternedNameTest() {
            CollectionDto dto = new CollectionDto();
            dto.name = "<script/>";

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.PATTERN));
        }

        @Test
        @Order(5)
        @DisplayName("validate null (0) order")
        public void validateNullOrderTest() {
            CollectionDto dto = new CollectionDto();
            dto.name = "abc";
            dto.order = 0;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(6)
        @DisplayName("validate less order value")
        public void validateLessOrderValueTest() {
            CollectionDto dto = new CollectionDto();
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE - 10;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }

        @Test
        @Order(7)
        @DisplayName("validate null scope")
        public void validateNullScopeTest() {
            CollectionDto dto = new CollectionDto();
            dto.scope = null;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(8)
        @DisplayName("validate null scope.id and scope.priority")
        public void validateNullScopeIdAndPriorityTest() {
            CollectionDto dto = new CollectionDto();
            CollectionScopeDto scopeDto = new CollectionScopeDto();
            scopeDto.id = null;
            scopeDto.priority = 0;
            dto.scope = scopeDto;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors.size(), Matchers.equalTo(2));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));

            error = errors.get(1);
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_PRIORITY_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(9)
        @DisplayName("validate bad format scope.id")
        public void validateBadScopeIdTest() {
            CollectionDto dto = new CollectionDto();
            CollectionScopeDto scopeDto = new CollectionScopeDto();
            scopeDto.id = "null";
            dto.scope = scopeDto;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.BAD_UUID));
        }

        @Test
        @Order(9)
        @DisplayName("validate less scope.priority value")
        public void validateLessScopePriorityValueTest() {
            CollectionDto dto = new CollectionDto();
            CollectionScopeDto scopeDto = new CollectionScopeDto();
            scopeDto.priority = CollectionValidator.MIN_SCOPE_PRIORITY_VALUE - 10;
            dto.scope = scopeDto;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_PRIORITY_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }
    }

    @Nested
    @DisplayName("validate before update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class ValidateBeforeUpdateTests {
        @Test
        @Order(1)
        @DisplayName("validate less size name")
        public void validateLessSizeNameTest() {
            CollectionDto dto = new CollectionDto();
            StringBuilder name = new StringBuilder();
            for(int i = 0; i < CollectionValidator.MIN_NAME_SIZE-1; i++)
                name.append(i);
            dto.name = name.toString();

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }

        @Test
        @Order(2)
        @DisplayName("validate greater size name")
        public void validateGreaterSizeNameTest() {
            CollectionDto dto = new CollectionDto();
            StringBuilder name = new StringBuilder();
            for(int i = 0; i < CollectionValidator.MAX_NAME_SIZE+1; i++)
                name.append(i);
            dto.name = name.toString();

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.GREATER_SIZE));
        }

        @Test
        @Order(3)
        @DisplayName("validate bad patterned name")
        public void validateBadPatternedNameTest() {
            CollectionDto dto = new CollectionDto();
            dto.name = "<script/>";

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.NAME_FIELD_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.PATTERN));
        }

        @Test
        @Order(4)
        @DisplayName("validate less order value")
        public void validateLessOrderValueTest() {
            CollectionDto dto = new CollectionDto();
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE - 10;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }

        @Test
        @Order(5)
        @DisplayName("validate null scope.id and scope.priority")
        public void validateNullScopeIdAndPriorityTest() {
            CollectionDto dto = new CollectionDto();
            CollectionScopeDto scopeDto = new CollectionScopeDto();
            scopeDto.id = null;
            scopeDto.priority = 0;
            dto.scope = scopeDto;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors.size(), Matchers.equalTo(2));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));

            error = errors.get(1);
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_PRIORITY_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(6)
        @DisplayName("validate bad format scope.id")
        public void validateBadScopeIdTest() {
            CollectionDto dto = new CollectionDto();
            CollectionScopeDto scopeDto = new CollectionScopeDto();
            scopeDto.id = "null";
            dto.scope = scopeDto;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.BAD_UUID));
        }

        @Test
        @Order(7)
        @DisplayName("validate less scope.priority value")
        public void validateLessScopePriorityValueTest() {
            CollectionDto dto = new CollectionDto();
            CollectionScopeDto scopeDto = new CollectionScopeDto();
            scopeDto.priority = CollectionValidator.MIN_SCOPE_PRIORITY_VALUE - 10;
            dto.scope = scopeDto;
            dto.name = "abc";
            dto.order = CollectionValidator.MIN_ORDER_VALUE;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(CollectionValidator.SCOPE_PRIORITY_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }
    }
}