package com.myzone.jwget.utils;

import org.jetbrains.annotations.NotNull;

public interface Factory<T> {

    @NotNull
    T create() throws CreationException;

    class CreationException extends Exception {

        public CreationException() {
            super();
        }

        public CreationException(String message) {
            super(message);
        }

        public CreationException(String message, Throwable cause) {
            super(message, cause);
        }

        public CreationException(Throwable cause) {
            super(cause);
        }

        public CreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }

}
