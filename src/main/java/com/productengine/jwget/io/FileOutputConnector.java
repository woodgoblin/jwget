package com.productengine.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class FileOutputConnector implements OutputConnector {

    protected final RandomAccessFile file;

    public FileOutputConnector(@NotNull RandomAccessFile file) {
        this.file = file;
    }

    @NotNull
    @Override
    public OutputStream getSubstream(long offset, long length) {
        return new BufferedSubstream(file, offset, length);
    }

    protected static class BufferedSubstream extends OutputStream {

        protected final RandomAccessFile file;

        protected final long offset;
        protected final ByteArrayOutputStream outputStream;

        public BufferedSubstream(@NotNull RandomAccessFile file, long offset, long length) {
            if (length > Integer.MAX_VALUE)
                throw new RuntimeException("length bigger then Integer.MAX_VALUE isn't supported");

            this.file = file;
            this.offset = offset;
            this.outputStream = new ByteArrayOutputStream((int) length);
        }

        @Override
        public void write(int b) {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) {
            outputStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            try (FileLock fileLock = file.getChannel().lock(offset, outputStream.size(), true)) {
                long currentPosition = file.getFilePointer();

                try {
                    file.seek(offset);
                    file.write(outputStream.toByteArray());
                } finally {
                    file.seek(currentPosition);
                }
            }
        }

        @Override
        public void close() throws IOException {
            flush();
        }
    }

}
