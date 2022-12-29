package org.matmech.paramsCollector.paramsStorage;

/**
 * Внутренние ошибки для класса ParamsStorage
 */
public final class ParamsStorageException extends Exception {
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
    public static final int EXIST_PARAM = 1;
    public static final int NOT_EXIST_PARAM = 2;

    public static final ErrorCode UNKNOWN_ERROR = new ErrorCode(
            UNKNOWN,
            "Неизвестная ошибка"
    );
    public static final ErrorCode EXIST_PARAMS_ERROR = new ErrorCode(
            EXIST_PARAM,
            "Параметры у пользователя уже существуют"
    );
    public static final ErrorCode NOT_EXIST_PARAM_ERROR = new ErrorCode(
            NOT_EXIST_PARAM,
            "Для пользователя объекта с параметрами нет. Создайте его перед добавлением!"
    );

    private final ErrorCode currentError;

    public ParamsStorageException(int errorCode) {
        switch (errorCode) {
            case EXIST_PARAM -> currentError = EXIST_PARAMS_ERROR;
            case NOT_EXIST_PARAM -> currentError = NOT_EXIST_PARAM_ERROR;
            default -> currentError = UNKNOWN_ERROR;
        }
    }

    public String getMessage() {
        return "Error: " + currentError.getErrorMessage();
    }
}
