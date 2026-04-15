package ru.reel.CollectionService.service.validator;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.service.issue.error.ErrorMessageFactory;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.FieldRequestError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CollectionValidator implements Validator<CollectionDto, FieldRequestError> {
    public static final String ALLOW_PATTERN = "а-я А-Я a-z A-Z _ - . [space]";
    public static final short MIN_ORDER_VALUE = 1;
    public static final short MIN_SCOPE_PRIORITY_VALUE = 1;

    @Override
    public List<FieldRequestError> validateBeforeCreating(CollectionDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(isEmpty(dto.name)) {
            errors.add(FieldRequestError.builder().field("name").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "name")).build());
        } else if(!isPatternMatch(dto.name)) {
            errors.add(FieldRequestError.builder().field("name").errorReason(ErrorReason.PATTERN).message(String.format(ErrorMessageFactory.get(ErrorReason.PATTERN), "name", ALLOW_PATTERN)).build());
        }
        if(hasOrderLessValue(dto.order)) {
            errors.add(FieldRequestError.builder().field("order").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "order", (double) MIN_ORDER_VALUE)).build());
        }
        if(isEmpty(dto.scope)) {
            errors.add(FieldRequestError.builder().field("scope").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope")).build());
        } else {
            if(isEmpty(dto.scope.id) && isEmpty(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.id")).build());
                errors.add(FieldRequestError.builder().field("scope.priority").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.priority")).build());
            } else if(!isEmpty(dto.scope.id) && !isUuidFormat(dto.scope.id)) {
                errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if(!isEmpty(dto.scope.priority) && hasScopePriorityLessValue(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field("scope.priority").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "scope.priority", (double) MIN_SCOPE_PRIORITY_VALUE)).build());
            }
        }
        return errors;
    }

    @Override
    public List<FieldRequestError> validateBeforeUpdating(CollectionDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(!isEmpty(dto.name)) {
            if(!isPatternMatch(dto.name)) {
                errors.add(FieldRequestError.builder().field("name").errorReason(ErrorReason.PATTERN).message(String.format(ErrorMessageFactory.get(ErrorReason.PATTERN), "name", ALLOW_PATTERN)).build());
            }
        }
        if(dto.order != 0.0) {
            if(hasOrderLessValue(dto.order)) {
                errors.add(FieldRequestError.builder().field("order").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "order", MIN_ORDER_VALUE)).build());
            }
        }
        if(!isEmpty(dto.scope)) {
            if(isEmpty(dto.scope.id) && isEmpty(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.id")).build());
                errors.add(FieldRequestError.builder().field("scope.priority").errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), "scope.priority")).build());
            } else if(!isEmpty(dto.scope.id) && !isUuidFormat(dto.scope.id)) {
                errors.add(FieldRequestError.builder().field("scope.id").errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if(!isEmpty(dto.scope.priority) && hasScopePriorityLessValue(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field("scope.priority").errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), "scope.priority", MIN_SCOPE_PRIORITY_VALUE)).build());
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

    public boolean hasScopePriorityLessValue(short priority) {
        return priority < MIN_SCOPE_PRIORITY_VALUE;
    }

    public boolean hasOrderLessValue(short order) {
        return order < MIN_ORDER_VALUE;
    }

    public boolean isPatternMatch(String text) {
        return text.matches("^[а-яА-Я\\w -.]+$");
    }
}
