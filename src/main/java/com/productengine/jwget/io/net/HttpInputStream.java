package com.productengine.jwget.io.net;

import sun.nio.ch.IOUtil;

import java.io.*;

import static java.lang.Integer.hashCode;
import static java.lang.Integer.parseInt;

public class HttpInputStream extends InputStream {
    private final InputStream origin;

    private final String httpVersion;
    private final HttpStatus status;

    public HttpInputStream(InputStream origin) {
        StringBuilder httpVersionBuilder = new StringBuilder();
        StringBuilder statusBuilder = new StringBuilder();

        State currentState = State.VERSION;

        try {
            while (currentState != State.INPUT_STREAM) {
                int read = origin.read();

                if (read < 0)
                    throw new IOException("Early EOF");

                switch (currentState) {
                    case VERSION:
                        if (read == ' ') {
                            currentState = State.STATUS;
                        } else {
                            httpVersionBuilder.append((char) read);
                        }
                        break;
                    case STATUS:
                        if (read == ' ') {
                            currentState = State.STATUS_MESSAGE;
                        } else {
                            statusBuilder.append((char) read);
                        }
                        break;
                    case STATUS_MESSAGE:
                        if (read == '\r') {
                            currentState = State.STATUS_MESSAGE_END;
                        }
                        break;
                    case STATUS_MESSAGE_END:
                        if (read == '\n') {
                            currentState = State.LAST_HEADER;
                        } else {
                            currentState = State.STATUS_MESSAGE;
                        }
                        break;
                    case HEADER:
                        if (read == '\r') {
                            currentState = State.HEADER_END;
                        }
                        break;
                    case HEADER_END:
                        if (read == '\n') {
                            currentState = State.LAST_HEADER;
                        } else {
                            currentState = State.HEADER;
                        }
                        break;
                    case LAST_HEADER:
                        if (read == '\r') {
                            currentState = State.LAST_HEADER_END;
                        } else {
                            currentState = State.HEADER;
                        }
                        break;
                    case LAST_HEADER_END:
                        if (read == '\n') {
                            currentState = State.INPUT_STREAM;
                        } else {
                            currentState = State.HEADER;
                        }
                        break;
                }
            }
        } catch (IOException ignored) {
        }

        this.origin = origin;
        this.httpVersion = httpVersionBuilder.toString();
        this.status = HttpStatus.byCode(parseInt(statusBuilder.toString()));
    }

    @Override
    public int read() throws IOException {
        return origin.read();
    }

    @Override
    public void close() throws IOException {
        origin.close();
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public enum HttpStatus {
        OK(200, "OK"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found");

        private final int code;
        private final String defaultMessage;

        private HttpStatus(int code, String defaultMessage) {
            this.code = code;
            this.defaultMessage = defaultMessage;
        }

        public int getCode() {
            return code;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public static HttpStatus byCode(int code) {
            switch (code) {
                case 200:
                    return OK;
                case 403:
                    return FORBIDDEN;
                case 404:
                    return NOT_FOUND;

                default:
                    return null;
            }
        }
    }

    protected enum State {
        VERSION,
        STATUS,
        STATUS_MESSAGE,
        STATUS_MESSAGE_END,
        HEADER,
        HEADER_END,
        LAST_HEADER,
        LAST_HEADER_END,
        INPUT_STREAM
    }

}