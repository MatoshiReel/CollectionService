package ru.reel.CollectionService.service.issue.error;

/**
 * The {@code ErrorMessageFactory} is a factory class, that standardized messages for {@link RequestError} message field.
 * @see RequestError
 */
public class ErrorMessageFactory {
    /**
     * @param reason one of a {@link ErrorReason} enum value.
     * @return unformatted {@link String} message for {@link RequestError} or his child classes.
     * @see ErrorReason
     * @see RequestError
     */
    public static String get(ErrorReason reason) {
        return switch(reason) {
            case EMPTY -> "%s is empty or null.";
            case JSON_FORMAT -> "Invalid JSON format.";
            case PATTERN -> "%s can only consist this characters: %s";
            case BAD_UUID -> "Invalid UUID format.";
            case BAD_DATA_TYPE -> "%s has invalid data type.";
            case NOT_SUIT -> "%s doesn't suit. Suitable values: %s.";
            case NOT_EXIST -> "%s doesn't exist.";
            case NOT_FOUND -> "%s isn't found.";
            case SCOPE -> "This %s has %s scope, which doesn't allow you.";
            case OWNER_ACCESS -> "You don't have access for this request, because you are not the owner of this %s.";
            case LESS_SIZE -> "%s can't has value less then %.2f.";
            case GREATER_SIZE -> "%s can't has value greater then %.2f.";
            case NOT_ALLOW -> "%s is not allow for this user.";
        };
    }
}
