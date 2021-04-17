package io.github.emcthye.fxrate.util;

public interface ErrorMessageFactory {

    String getErrorMessage();

    class NetworkConnectionException extends Exception implements ErrorMessageFactory {
        @Override
        public String getErrorMessage() {
            return "There is no internet connection";
        }
    }

    class LocalDataException extends Exception implements ErrorMessageFactory {
        @Override
        public String getErrorMessage() {
            return "Unable to retrieve file cache";
        }
    }

    class UnknownException extends Exception implements ErrorMessageFactory {

        private final Exception exception;

        public UnknownException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String getErrorMessage() {
            return (exception != null) ? this.exception.getMessage() : "Unknown error";
        }
    }
}
