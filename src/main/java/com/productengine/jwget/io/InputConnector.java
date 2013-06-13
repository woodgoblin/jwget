package com.productengine.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface InputConnector {

    @NotNull
    InputStream getSubstream(long offset, long length);

}
