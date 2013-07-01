package com.productengine.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.lang.Math.min;

public class NetworkInputConnector implements InputConnector {

    protected static final InputStream EMPTY_STREAM = new InputStream() {
        @Override
        public int read() throws IOException {
            return -1;
        }
    };

    protected final InputStream inputStream;
    protected volatile long currentOffset;

    public NetworkInputConnector(@NotNull URL url) throws IOException {
        inputStream = url.openConnection().getInputStream();
        currentOffset = 0;
    }

    @NotNull
    @Override
    public synchronized InputStream getSubstream(long offset, long length) {
        if (offset < 0)
            throw new IllegalArgumentException("offset can't be less then 0");

        if (length < 0)
            throw new IllegalArgumentException("length can't be less then 0");

        if (offset < currentOffset)
            throw new RuntimeException("Rollbacks are not supported");

        try {
            inputStream.skip(offset - currentOffset);

            currentOffset = offset + length;

            return new Substream(inputStream, length);
        } catch (IOException e) {
            return EMPTY_STREAM;
        }
    }

    protected static class Substream extends InputStream {

        protected final InputStream inputStream;
        protected long bytesLeft;

        public Substream(@NotNull InputStream inputStream, long bytesLeft) {
            this.inputStream = inputStream;
            this.bytesLeft = bytesLeft;
        }

        @Override
        public int read() throws IOException {
            synchronized (inputStream) {
                if (bytesLeft < 1)
                    return -1;

                int readByte = inputStream.read();

                if (readByte != -1)
                    bytesLeft--;

                return readByte;
            }
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            synchronized (inputStream) {
                return super.read(b, off, len);
            }
        }

        @Override
        public long skip(long n) throws IOException {
            synchronized (inputStream) {
                long bytesSkipped = inputStream.skip(min(n, bytesLeft));

                bytesLeft -= bytesSkipped;

                return bytesSkipped;
            }
        }

        @Override
        public int available() {
            synchronized (inputStream) {
                return (int) bytesLeft;
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (inputStream) {
                skip(bytesLeft);

                // TODO: fix this
                if (inputStream.available() == 0) {
                    inputStream.close();
                }
            }
        }

    }
}
