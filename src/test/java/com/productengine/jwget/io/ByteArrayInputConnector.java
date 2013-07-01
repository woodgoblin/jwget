package com.productengine.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputConnector implements InputConnector {

    protected final byte[] bytes;

    public ByteArrayInputConnector(@NotNull byte[] bytes) {
        this.bytes = bytes;
    }

    @NotNull
    @Override
    public InputStream getSubstream(final long offset, final long length) {
        if (offset > Integer.MAX_VALUE)
            throw new RuntimeException("offset bigger then Integer.MAX_VALUE isn't supported");

        return new InputStream() {

            private int currentPosition = (int) offset;
            private int endPosition = (int) offset + (int) length;

            @Override
            public int read() throws IOException {
                if (currentPosition >= endPosition)
                    return -1;

                return bytes[currentPosition++];
            }
        };
    }
}
