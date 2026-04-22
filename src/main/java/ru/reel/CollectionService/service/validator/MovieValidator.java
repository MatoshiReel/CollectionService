package ru.reel.CollectionService.service.validator;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.service.issue.error.ErrorMessageFactory;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.FieldRequestError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MovieValidator implements Validator<MovieDto, FieldRequestError> {
    public static final String CATALOG_ID_NAME = "catalogId";
    public static final String OWNER_RATING_NAME = "ownerRating";
    public static final String STATUS_NAME = "status";
    public static final String STATUS_ID_NAME = String.format("%s.id", STATUS_NAME);
    public static final String STATUS_ORDER_NAME = String.format("%s.order", STATUS_NAME);
    public static final double MIN_RATING_VALUE = 1.0;
    public static final double MAX_RATING_VALUE = 10.0;
    public static final short MIN_STATUS_ORDER_VALUE = 1;
    @Override
    public List<FieldRequestError> validateBeforeCreating(MovieDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(isEmpty(dto.catalogId)) {
            errors.add(FieldRequestError.builder().field(CATALOG_ID_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), CATALOG_ID_NAME)).build());
        } else if(!isUuidFormat(dto.catalogId)) {
            errors.add(FieldRequestError.builder().field(CATALOG_ID_NAME).errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        }
        if(isEmpty(dto.ownerRating)) {
            errors.add(FieldRequestError.builder().field(OWNER_RATING_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), OWNER_RATING_NAME)).build());
        } else if(hasRatingLessValue(dto.ownerRating)) {
            errors.add(FieldRequestError.builder().field(OWNER_RATING_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), OWNER_RATING_NAME, MIN_RATING_VALUE)).build());
        } else if(hasRatingGreaterValue(dto.ownerRating)) {
            errors.add(FieldRequestError.builder().field(OWNER_RATING_NAME).errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), OWNER_RATING_NAME, MAX_RATING_VALUE)).build());
        }
        if(isEmpty(dto.status)) {
            errors.add(FieldRequestError.builder().field(STATUS_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), STATUS_NAME)).build());
        } else {
            if(isEmpty(dto.status.id) && isEmpty(dto.status.order)) {
                errors.add(FieldRequestError.builder().field(STATUS_ID_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), STATUS_ID_NAME)).build());
                errors.add(FieldRequestError.builder().field(STATUS_ORDER_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), STATUS_ORDER_NAME)).build());
            } else if(!isEmpty(dto.status.id) && !isUuidFormat(dto.status.id)) {
                errors.add(FieldRequestError.builder().field(STATUS_ID_NAME).errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if(!isEmpty(dto.status.order) && hasStatusOrderLessValue(dto.status.order)) {
                errors.add(FieldRequestError.builder().field(STATUS_ORDER_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), STATUS_ORDER_NAME, (double) MIN_STATUS_ORDER_VALUE)).build());
            }
        }
        return errors;
    }

    @Override
    public List<FieldRequestError> validateBeforeUpdating(MovieDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(!isEmpty(dto.ownerRating)) {
            if(hasRatingLessValue(dto.ownerRating)) {
                errors.add(FieldRequestError.builder().field(OWNER_RATING_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), OWNER_RATING_NAME, MIN_RATING_VALUE)).build());
            } else if(hasRatingGreaterValue(dto.ownerRating)) {
                errors.add(FieldRequestError.builder().field(OWNER_RATING_NAME).errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), OWNER_RATING_NAME, MAX_RATING_VALUE)).build());
            }
        }
        if(!isEmpty(dto.status)) {
            if (isEmpty(dto.status.id) && isEmpty(dto.status.order)) {
                errors.add(FieldRequestError.builder().field(STATUS_ID_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), STATUS_ID_NAME)).build());
                errors.add(FieldRequestError.builder().field(STATUS_ORDER_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), STATUS_ORDER_NAME)).build());
            } else if (!isEmpty(dto.status.id) && !isUuidFormat(dto.status.id)) {
                errors.add(FieldRequestError.builder().field(STATUS_ID_NAME).errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if (!isEmpty(dto.status.order) && hasStatusOrderLessValue(dto.status.order)) {
                errors.add(FieldRequestError.builder().field(STATUS_ORDER_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), STATUS_ORDER_NAME, (double) MIN_STATUS_ORDER_VALUE)).build());
            }
        }
        return errors;
    }

    public boolean isEmpty(double num) {
        return num == 0.0;
    }

    public boolean isEmpty(short num) {
        return num == 0;
    }

    public boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public boolean isEmpty(Object obj) {
        return obj == null;
    }

    public boolean isUuidFormat(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean hasRatingLessValue(double rating) {
        return rating < MIN_RATING_VALUE;
    }

    public boolean hasRatingGreaterValue(double rating) {
        return rating > MAX_RATING_VALUE;
    }

    public boolean hasStatusOrderLessValue(short priority) {
        return priority < MIN_STATUS_ORDER_VALUE;
    }
}
