package com.productengine.jwget.io;

import com.productengine.jwget.utils.Factory;
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
    public synchronized InputStream getSubStream(long offset, long length) {
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
        protected volatile long bytesLeft;

        public SubStream(@NotNull InputStream inputStream, long bytesLeft) {
            this.inputStream = inputStream;
            this.bytesLeft = bytesLeft;
        }

        @Override
        public synchronized int read() throws IOException {
            if (bytesLeft < 1)
                return -1;

            int readByte = inputStream.read();

            if (readByte != -1)
                bytesLeft--;

            return readByte;
        }

        @Override
        public synchronized int read(byte b[], int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || len < 0 || len > b.length - off) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }

            int c = read();
            if (c == -1) {
                return -1;
            }
            b[off] = (byte)c;

            int i = 1;
            try {
                for (; i < len ; i++) {
                    c = read();
                    if (c == -1) {
                        break;
                    }
                    b[off + i] = (byte)c;
                }
            } catch (IOException ee) {
            }
            return i;
        }

        @Override
        public synchronized long skip(long n) throws IOException {
            long bytesSkipped = inputStream.skip(min(n, bytesLeft));

            bytesLeft -= bytesSkipped;

            return bytesSkipped;
        }

        @Override
        public synchronized int available() throws IOException {
            return (int) bytesLeft;
        }

        @Override
        public synchronized void close() throws IOException {
            skip(bytesLeft);

            if (inputStream.available() < 1) {
                inputStream.close();
            }
        }

    }

}
