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
    public static final double MIN_RATING_VALUE = 1.0;
    public static final double MAX_RATING_VALUE = 10.0;
    @Override
    public List<FieldRequestError> validateBeforeCreating(MovieDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(isEmpty(dto.catalogId))
            errors.add(FieldRequestError.builder().field("catalogId").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "catalogId")).build());
        else if(!isUuidFormat(dto.catalogId))
            errors.add(FieldRequestError.builder().field("catalogId").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        if(hasRatingLessValue(dto.ownerRating))
            errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "ownerRating", MIN_RATING_VALUE)).build());
        else if(hasRatingGreaterValue(dto.ownerRating))
            errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), "ownerRating", MAX_RATING_VALUE)).build());
        if(isEmpty(dto.status) || isEmpty(dto.status.id))
            errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "status.id")).build());
        else if(!isUuidFormat(dto.status.id))
            errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        return errors;
    }

    @Override
    public List<FieldRequestError> validateBeforeUpdating(MovieDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(dto.ownerRating != 0.0)
            if(hasRatingLessValue(dto.ownerRating))
                errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "ownerRating", MIN_RATING_VALUE)).build());
            else if(hasRatingGreaterValue(dto.ownerRating))
                errors.add(FieldRequestError.builder().field("ownerRating").errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), "ownerRating", MAX_RATING_VALUE)).build());
        if(!isEmpty(dto.status))
            if(isEmpty(dto.status.id))
                errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "status.id")).build());
            else if(!isUuidFormat(dto.status.id))
                errors.add(FieldRequestError.builder().field("status.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        return errors;
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
}
