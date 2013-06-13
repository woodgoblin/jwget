package com.productengine.jwget.io;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public interface OutputConnector {

    @NotNull
    OutputStream getSubstream(long offset, long length);

}
