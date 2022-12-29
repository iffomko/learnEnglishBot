package org.matmech.paramsCollector;

/**
 * Внутренний набор ошибок для класса ParamsCollector
 */
public final class ParamsCollectorException extends Exception {
    private static class ErrorCode {
        private final int errorCode;
        private final String errorMessage;

        protected ErrorCode(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static final int UNKNOWN = 0;
    public static final int NOT_EXIST_GROUP = 1;
    public static final int INVALID_TEST_MODE = 2;
    public static final int INVALID_COUNT_WORDS = 3;
    public static final int NOT_EXIST_WORD = 4;
    public static final int INVALID_PARAMETER = 5;

    public static final ErrorCode NOT_EXIST_GROUP_ERROR = new ErrorCode(
            NOT_EXIST_GROUP,
            "Ой, кажется, ты ввел несуществующую группу слов. Попробуй заново:"
    );
    public static final ErrorCode INVALID_TEST_MODE_ERROR = new ErrorCode(
            INVALID_TEST_MODE,
            "Ой, кажется, ты ввел неправильный режим. Попробуй заново:"
    );
    public static final ErrorCode INVALID_COUNT_WORDS_ERROR = new ErrorCode(
            INVALID_COUNT_WORDS,
            "Ой, кажется, ты ввел неправильное значение для количество слов. Попробуй заново:"
    );
    public static final ErrorCode NOT_EXIST_WORD_ERROR = new ErrorCode(
            NOT_EXIST_WORD,
            "Ой, кажется, ты ввел несуществующее слово. Попробуй заново:"
    );
    public static final ErrorCode INVALID_PARAMETER_ERROR = new ErrorCode(
            INVALID_PARAMETER,
            "Ой, кажется, ты ввел неверный параметр. Надо написать либо translation, либо group. Попробуй заново:"
    );
    public static final ErrorCode UNKNOWN_ERROR = new ErrorCode(
            UNKNOWN,
            "Неизвестный код ошибки. Повторите попытку:"
    );

    private ErrorCode currentError;

    public ParamsCollectorException(int errorCode) {
        switch (errorCode) {
            case NOT_EXIST_GROUP -> this.currentError = NOT_EXIST_GROUP_ERROR;
            case INVALID_TEST_MODE -> this.currentError = INVALID_TEST_MODE_ERROR;
            case INVALID_COUNT_WORDS -> this.currentError = INVALID_COUNT_WORDS_ERROR;
            case NOT_EXIST_WORD -> this.currentError = NOT_EXIST_WORD_ERROR;
            case INVALID_PARAMETER -> this.currentError = INVALID_PARAMETER_ERROR;
            default -> currentError = UNKNOWN_ERROR;
        }
    }

    public String getMessage() {
        return "Error: " + currentError.getErrorMessage();
    }
}
