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
    public static final String NAME_FIELD_NAME = "name";
    public static final String ORDER_NAME = "order";
    public static final String SCOPE_NAME = "scope";
    public static final String SCOPE_ID_NAME = String.format("%s.id", SCOPE_NAME);
    public static final String SCOPE_PRIORITY_NAME = String.format("%s.priority", SCOPE_NAME);
    public static final short MIN_NAME_SIZE = 3;
    public static final short MAX_NAME_SIZE = 35;
    public static final String ALLOW_PATTERN = "а-я А-Я a-z A-Z _ - . [space]";
    public static final short MIN_ORDER_VALUE = 1;
    public static final short MIN_SCOPE_PRIORITY_VALUE = 1;

    @Override
    public List<FieldRequestError> validateBeforeCreating(CollectionDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(isEmpty(dto.name)) {
            errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), NAME_FIELD_NAME)).build());
        } else if(hasNameLessSize(dto.name)) {
            errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), NAME_FIELD_NAME, (double) MIN_NAME_SIZE)).build());
        } else if(hasNameGreaterSize(dto.name)) {
            errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), NAME_FIELD_NAME, (double) MAX_NAME_SIZE)).build());
        } else if(!isPatternMatch(dto.name)) {
            errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.PATTERN).message(String.format(ErrorMessageFactory.get(ErrorReason.PATTERN), NAME_FIELD_NAME, ALLOW_PATTERN)).build());
        }
        if(isEmpty(dto.order)) {
            errors.add(FieldRequestError.builder().field(ORDER_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), ORDER_NAME)).build());
        } else if(hasOrderLessValue(dto.order)) {
            errors.add(FieldRequestError.builder().field(ORDER_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), ORDER_NAME, (double) MIN_ORDER_VALUE)).build());
        }
        if(isEmpty(dto.scope)) {
            errors.add(FieldRequestError.builder().field(SCOPE_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), SCOPE_NAME)).build());
        } else {
            if(isEmpty(dto.scope.id) && isEmpty(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field(SCOPE_ID_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), SCOPE_ID_NAME)).build());
                errors.add(FieldRequestError.builder().field(SCOPE_PRIORITY_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), SCOPE_PRIORITY_NAME)).build());
            } else if(!isEmpty(dto.scope.id) && !isUuidFormat(dto.scope.id)) {
                errors.add(FieldRequestError.builder().field(SCOPE_ID_NAME).errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if(!isEmpty(dto.scope.priority) && hasScopePriorityLessValue(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field(SCOPE_PRIORITY_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), SCOPE_PRIORITY_NAME, (double) MIN_SCOPE_PRIORITY_VALUE)).build());
            }
        }
        return errors;
    }

    @Override
    public List<FieldRequestError> validateBeforeUpdating(CollectionDto dto) {
        List<FieldRequestError> errors = new ArrayList<>();
        if(!isEmpty(dto.name)) {
            if(hasNameLessSize(dto.name)) {
                errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), NAME_FIELD_NAME, (double) MIN_NAME_SIZE)).build());
            } else if(hasNameGreaterSize(dto.name)) {
                errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.GREATER_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.GREATER_SIZE), NAME_FIELD_NAME, (double) MAX_NAME_SIZE)).build());
            } else if(!isPatternMatch(dto.name)) {
                errors.add(FieldRequestError.builder().field(NAME_FIELD_NAME).errorReason(ErrorReason.PATTERN).message(String.format(ErrorMessageFactory.get(ErrorReason.PATTERN), NAME_FIELD_NAME, ALLOW_PATTERN)).build());
            }
        }
        if(!isEmpty(dto.order)) {
             if(hasOrderLessValue(dto.order)) {
                 errors.add(FieldRequestError.builder().field(ORDER_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), ORDER_NAME, (double) MIN_ORDER_VALUE)).build());
             }
        }
        if(!isEmpty(dto.scope)) {
            if(isEmpty(dto.scope.id) && isEmpty(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field(SCOPE_ID_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), SCOPE_ID_NAME)).build());
                errors.add(FieldRequestError.builder().field(SCOPE_PRIORITY_NAME).errorReason(ErrorReason.EMPTY).message(String.format(ErrorMessageFactory.get(ErrorReason.EMPTY), SCOPE_PRIORITY_NAME)).build());
            } else if(!isEmpty(dto.scope.id) && !isUuidFormat(dto.scope.id)) {
                errors.add(FieldRequestError.builder().field(SCOPE_ID_NAME).errorReason(ErrorReason.BAD_UUID).message(ErrorMessageFactory.get(ErrorReason.BAD_UUID)).build());
            } else if(!isEmpty(dto.scope.priority) && hasScopePriorityLessValue(dto.scope.priority)) {
                errors.add(FieldRequestError.builder().field(SCOPE_PRIORITY_NAME).errorReason(ErrorReason.LESS_SIZE).message(String.format(ErrorMessageFactory.get(ErrorReason.LESS_SIZE), SCOPE_PRIORITY_NAME, (double) MIN_SCOPE_PRIORITY_VALUE)).build());
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

    public boolean hasNameGreaterSize(String name) {
        return name.length() > MAX_NAME_SIZE;
    }

    public boolean hasNameLessSize(String name) {
        return name.length() < MIN_NAME_SIZE;
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
