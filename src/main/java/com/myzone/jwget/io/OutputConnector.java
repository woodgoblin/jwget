package com.myzone.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public interface OutputConnector extends AutoCloseable, Closeable {

    @NotNull
    OutputStream getSubstream(long offset, long length) throws IOException;

}
