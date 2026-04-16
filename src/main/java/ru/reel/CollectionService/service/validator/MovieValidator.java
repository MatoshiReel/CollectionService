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
    private static final double MIN_RATING_VALUE = 1.0;
    private static final double MAX_RATING_VALUE = 10.0;
    private static final short MIN_STATUS_ORDER_VALUE = 1;
    @Override
    public List<FieldRequestError> validateBeforeCreating(MovieDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(isEmpty(dto.catalogId)) {
            errors.add(FieldRequestError.builder().field("catalogId").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "catalogId")).build());
        } else if(!isUuidFormat(dto.catalogId)) {
            errors.add(FieldRequestError.builder().field("catalogId").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        } if(hasRatingLessValue(dto.ownerRating)) {
            errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "ownerRating", MIN_RATING_VALUE)).build());
        } else if(hasRatingGreaterValue(dto.ownerRating)) {
            errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), "ownerRating", MAX_RATING_VALUE)).build());
        }
        if(isEmpty(dto.status)) {
            errors.add(FieldRequestError.builder().field("status").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "status")).build());
        } else {
            if(isEmpty(dto.status.id) && isEmpty(dto.status.order)) {
                errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "status.id")).build());
                errors.add(FieldRequestError.builder().field("scope.order").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.order")).build());
            } else if(!isEmpty(dto.status.id) && !isUuidFormat(dto.status.id)) {
                errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if(!isEmpty(dto.status.order) && hasStatusOrderLessValue(dto.status.order)) {
                errors.add(FieldRequestError.builder().field("status.order").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "status.order", MIN_STATUS_ORDER_VALUE)).build());
            }
        }
        return errors;
    }

    @Override
    public List<FieldRequestError> validateBeforeUpdating(MovieDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(dto.ownerRating != 0.0) {
            if(hasRatingLessValue(dto.ownerRating)) {
                errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "ownerRating", MIN_RATING_VALUE)).build());
            } else if(hasRatingGreaterValue(dto.ownerRating)) {
                errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), "ownerRating", MAX_RATING_VALUE)).build());
            }
        }
        if(!isEmpty(dto.status)) {
            if (isEmpty(dto.status.id) && isEmpty(dto.status.order)) {
                errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "status.id")).build());
                errors.add(FieldRequestError.builder().field("status.order").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "status.order")).build());
            } else if (!isEmpty(dto.status.id) && !isUuidFormat(dto.status.id)) {
                errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if (!isEmpty(dto.status.order) && hasStatusOrderLessValue(dto.status.order)) {
                errors.add(FieldRequestError.builder().field("status.order").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "status.order", MIN_STATUS_ORDER_VALUE)).build());
            }
        }
        return errors;
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
