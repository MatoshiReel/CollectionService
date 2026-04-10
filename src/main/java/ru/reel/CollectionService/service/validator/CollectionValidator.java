package ru.reel.CollectionService.service.validator;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.service.issue.error.ErrorMessageFactory;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.FieldRequestError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CollectionValidator implements Validator<CollectionDto, FieldRequestError> {
    public static final String ALLOW_PATTERN = "а-я А-Я a-z A-Z _ - . [space]";
    public static final double MIN_PRIORITY_VALUE = 1.0;
    @Override
    public List<FieldRequestError> validateBeforeCreating(CollectionDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(isEmpty(dto.name))
            errors.add(FieldRequestError.builder().field("name").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "name")).build());
        else if(!isPatternMatch(dto.name))
            errors.add(FieldRequestError.builder().field("name").errorReason(ErrorReason.PATTERN).message(String.format(ErrorMessageFactory.get(ErrorReason.PATTERN), "name", ALLOW_PATTERN)).build());
        if(hasPriorityLessValue(dto.priority))
            errors.add(FieldRequestError.builder().field("priority").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "priority", MIN_PRIORITY_VALUE)).build());
        if(isEmpty(dto.scope) || isEmpty(dto.scope.id))
            errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.id")).build());
        else if(!isUuidFormat(dto.scope.id))
            errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
        return errors;
    }

    @Override
    public List<FieldRequestError> validateBeforeUpdating(CollectionDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(!isEmpty(dto.name))
            if(!isPatternMatch(dto.name))
                errors.add(FieldRequestError.builder().field("name").errorReason(ErrorReason.PATTERN).message(String.format(ErrorMessageFactory.get(ErrorReason.PATTERN), "name", ALLOW_PATTERN)).build());
        if(dto.priority != 0.0)
            if(hasPriorityLessValue(dto.priority))
                errors.add(FieldRequestError.builder().field("priority").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "priority", MIN_PRIORITY_VALUE)).build());
        if(!isEmpty(dto.scope))
            if(isEmpty(dto.scope.id))
                errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.id")).build());
            else if(!isUuidFormat(dto.scope.id))
                errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
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

    public boolean hasPriorityLessValue(double priority) {
        return priority < MIN_PRIORITY_VALUE;
    }

    public boolean isPatternMatch(String text) {
        return text.matches("^[а-яА-Я\\w -.]+$");
    }
}
