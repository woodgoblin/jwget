package com.myzone.jwget.io;

import com.myzone.jwget.utils.Factory;
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
    public NetworkInputConnector create() throws CreationException {
        try {
            return new NetworkInputConnector(url);
        } catch (IOException e) {
            throw new CreationException(e);
        }
    }

}
