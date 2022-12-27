package org.matmech.params.paramsStorage;

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
    public static final int EXIST_PARAMS = 1;

    public static final ErrorCode UNKNOWN_ERROR = new ErrorCode(
            UNKNOWN,
            "Неизвестная ошибка"
    );
    public static final ErrorCode EXIST_PARAMS_ERROR = new ErrorCode(
            EXIST_PARAMS,
            "Параметры у пользователя уже существуют"
    );

    private ErrorCode currentError;

    public ParamsStorageException(int errorCode) {
        switch (errorCode) {
            case EXIST_PARAMS -> currentError = EXIST_PARAMS_ERROR;
            default -> currentError = UNKNOWN_ERROR;
        }
    }

    public String getMessage() {
        return "Error: " + currentError.getErrorMessage();
    }
}
