package com.productengine.jwget.io;

import java.io.InputStream;

public interface InputConnector {

    InputStream getSubStream(long offset, long length);

}
