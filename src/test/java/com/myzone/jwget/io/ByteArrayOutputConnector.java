package com.myzone.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayOutputConnector implements OutputConnector {

    protected final byte[] bytes;
    protected volatile boolean closed;

    public ByteArrayOutputConnector(byte[] bytes) {
        this.bytes = bytes;
    }

    @NotNull
    @Override
    public OutputStream getSubstream(final long offset, final long length) {
        if (offset > Integer.MAX_VALUE)
            throw new RuntimeException("offset bigger then Integer.MAX_VALUE isn't supported");

        return new OutputStream() {

            private int currentPosition = (int) offset;
            private int endPosition = (int) offset + (int) length;

            @Override
            public void write(int b) throws IOException {
                if (currentPosition >= endPosition)
                    throw new IOException();

                bytes[currentPosition++] = (byte) b;
            }
        };
    }

    @Override
    public void close() {
        closed = true;
    }

}
