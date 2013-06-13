package com.productengine.jwget.io;

import com.productengine.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class NetworkInputConnectorFactory implements Factory<NetworkInputConnector> {

    private final URL url;

    public NetworkInputConnectorFactory(@NotNull URL url) {
        this.url = url;
    }

    @NotNull
    @Override
    public NetworkInputConnector create() throws IOException {
        return new NetworkInputConnector(url);
    }

}
