package com.myzone.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public interface InputConnector extends AutoCloseable, Closeable {

    @NotNull
    InputStream getSubstream(long offset, long length) throws IOException;

}
