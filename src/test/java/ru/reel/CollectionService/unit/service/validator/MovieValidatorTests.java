package ru.reel.CollectionService.unit.service.validator;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.dto.MovieStatusDto;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.FieldRequestError;
import ru.reel.CollectionService.service.validator.MovieValidator;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@DisplayName("movie validator")
public class MovieValidatorTests {
    @Autowired
    private MovieValidator validator;

    @Nested
    @DisplayName("validate before create")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class ValidateBeforeCreateTests {
        @Test
        @Order(1)
        @DisplayName("validate null/empty catalogId")
        public void validateNullAndEmptyCatalogIdTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = null;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.CATALOG_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));

            dto.catalogId = "";
            errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.CATALOG_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(2)
        @DisplayName("validate bad UUID catalogId")
        public void validateBadUuidCatalogIdTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = "wrong_id";

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.CATALOG_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.BAD_UUID));
        }

        @Test
        @Order(3)
        @DisplayName("validate null ownerRating")
        public void validateNullOwnerRatingTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = 0.0;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.OWNER_RATING_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(4)
        @DisplayName("validate less size ownerRating value")
        public void validateLessOwnerRatingValueTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = MovieValidator.MIN_RATING_VALUE - 0.1;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.OWNER_RATING_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }

        @Test
        @Order(5)
        @DisplayName("validate greater size ownerRating value")
        public void validateGreaterOwnerRatingValueTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = MovieValidator.MAX_RATING_VALUE + 0.1;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.OWNER_RATING_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.GREATER_SIZE));
        }

        @Test
        @Order(6)
        @DisplayName("validate null status")
        public void validateNullStatusTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = MovieValidator.MAX_RATING_VALUE;
            dto.status = null;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(7)
        @DisplayName("validate null status.id and status.order")
        public void validateNullStatusIdAndOrderTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = MovieValidator.MAX_RATING_VALUE;
            MovieStatusDto statusDto = new MovieStatusDto();
            statusDto.id = null;
            statusDto.order = 0;
            dto.status = statusDto;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));

            error = errors.get(1);
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(8)
        @DisplayName("validate bad UUID status.id")
        public void validateBadUuidStatusIdTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = MovieValidator.MAX_RATING_VALUE;
            MovieStatusDto statusDto = new MovieStatusDto();
            statusDto.id = "wrong_id";
            dto.status = statusDto;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.BAD_UUID));
        }

        @Test
        @Order(9)
        @DisplayName("validate less size status.order value")
        public void validateLessSizeStatusOrderValueTest() {
            MovieDto dto = new MovieDto();
            dto.catalogId = UUID.randomUUID().toString();
            dto.ownerRating = MovieValidator.MAX_RATING_VALUE;
            MovieStatusDto statusDto = new MovieStatusDto();
            statusDto.order = MovieValidator.MIN_STATUS_ORDER_VALUE - 10;
            dto.status = statusDto;

            List<FieldRequestError> errors = validator.validateBeforeCreating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }
    }

    @Nested
    @DisplayName("validate before update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class ValidateBeforeUpdateTests {

        @Test
        @Order(1)
        @DisplayName("validate less size ownerRating value")
        public void validateLessOwnerRatingValueTest() {
            MovieDto dto = new MovieDto();
            dto.ownerRating = MovieValidator.MIN_RATING_VALUE - 0.1;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.OWNER_RATING_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }

        @Test
        @Order(2)
        @DisplayName("validate greater size ownerRating value")
        public void validateGreaterOwnerRatingValueTest() {
            MovieDto dto = new MovieDto();
            dto.ownerRating = MovieValidator.MAX_RATING_VALUE + 0.1;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.OWNER_RATING_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.GREATER_SIZE));
        }

        @Test
        @Order(3)
        @DisplayName("validate null status.id and status.order")
        public void validateNullStatusIdAndOrderTest() {
            MovieDto dto = new MovieDto();
            MovieStatusDto statusDto = new MovieStatusDto();
            statusDto.id = null;
            statusDto.order = 0;
            dto.status = statusDto;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));

            error = errors.get(1);
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.EMPTY));
        }

        @Test
        @Order(4)
        @DisplayName("validate bad UUID status.id")
        public void validateBadUuidStatusIdTest() {
            MovieDto dto = new MovieDto();
            MovieStatusDto statusDto = new MovieStatusDto();
            statusDto.id = "wrong_id";
            dto.status = statusDto;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ID_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.BAD_UUID));
        }

        @Test
        @Order(5)
        @DisplayName("validate less size status.order value")
        public void validateLessSizeStatusOrderValueTest() {
            MovieDto dto = new MovieDto();
            MovieStatusDto statusDto = new MovieStatusDto();
            statusDto.order = MovieValidator.MIN_STATUS_ORDER_VALUE - 10;
            dto.status = statusDto;

            List<FieldRequestError> errors = validator.validateBeforeUpdating(dto);
            assertThat(errors, Matchers.not(Matchers.empty()));

            FieldRequestError error = errors.getFirst();
            assertThat(error.getField(), Matchers.equalTo(MovieValidator.STATUS_ORDER_NAME));
            assertThat(error.getErrorReason(), Matchers.equalTo(ErrorReason.LESS_SIZE));
        }
    }
}