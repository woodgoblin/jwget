package com.productengine.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.lang.Math.min;

public class NetworkInputConnector implements InputConnector {

    protected final InputStream inputStream;
    protected volatile long currentOffset;

    public NetworkInputConnector(@NotNull URL url) throws IOException {
        inputStream = url.openConnection().getInputStream();
        currentOffset = 0;
    }

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

            return new SubStream(inputStream, length);
        } catch (IOException e) {
            return null;
        }
    }

    protected static class SubStream extends InputStream {

        protected final InputStream inputStream;
        protected long bytesLeft;

        public SubStream(@NotNull InputStream inputStream, long bytesLeft) {
            this.inputStream = inputStream;
            this.bytesLeft = bytesLeft;
        }

        @Override
        public int read() throws IOException {
            if (bytesLeft < 1)
                return -1;

            int readByte = inputStream.read();

            if (readByte != -1)
                bytesLeft--;

            return readByte;
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            return super.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            long bytesSkipped = inputStream.skip(min(n, bytesLeft));

            bytesLeft -= bytesSkipped;

            return bytesSkipped;
        }

        @Override
        public int available() {
            return (int) bytesLeft;
        }

        @Override
        public void close() throws IOException {
            skip(bytesLeft);

            if (inputStream.available() < 1) {
                inputStream.close();
            }
        }

    }
}
