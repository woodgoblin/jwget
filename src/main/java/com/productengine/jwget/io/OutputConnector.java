package com.productengine.jwget.io;

import java.io.OutputStream;

public interface OutputConnector {

    OutputStream getSubStream(long offset, long length);

}