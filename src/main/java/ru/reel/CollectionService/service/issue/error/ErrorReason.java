package ru.reel.CollectionService.service.issue.error;

/**
 * The {@code ErrorReason} is an enum, that points on one of the reason of error for 4xx response status.
 */
public enum ErrorReason {
    EMPTY,
    JSON_FORMAT,
    PATTERN,
    BAD_UUID,
    BAD_DATA_TYPE,
    NOT_MATCH,
    NOT_EXIST,
    NOT_FOUND,
    SCOPE,
    OWNER_ACCESS,
    NOT_ALLOW,
    LESS_SIZE,
    GREATER_SIZE
}