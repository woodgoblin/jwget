package com.productengine.jwget.io;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AbstractInputConnector implements InputConnector {

    protected final URL targetUrl;

    public AbstractInputConnector(@NotNull URL targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return targetUrl.openStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractInputConnector)) return false;

        AbstractInputConnector that = (AbstractInputConnector) o;

        if (!targetUrl.equals(that.targetUrl)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return targetUrl.hashCode();
    }
}
