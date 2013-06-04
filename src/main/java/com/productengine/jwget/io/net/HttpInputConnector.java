package com.productengine.jwget.io.net;

import com.productengine.jwget.io.AbstractInputConnector;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class HttpInputConnector extends AbstractInputConnector {

    protected static final String PROTOCOL_NAME = "http";

    public HttpInputConnector(@NotNull URL targetUrl) {
        super(targetUrl);

        if (!PROTOCOL_NAME.equals(targetUrl.getProtocol()))
            throw new IllegalArgumentException(format("%s is not HTTP-url", targetUrl));
    }

    @Override
    public HttpInputStream getInputStream() {
        return new HttpInputStream(super.getInputStream());
    }

}
