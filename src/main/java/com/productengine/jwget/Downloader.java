package com.productengine.jwget;

import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.copyLarge;

public class Downloader implements Runnable {

    private final InputConnector inputConnector;
    private final OutputConnector outputConnector;

    public Downloader(InputConnector inputConnector, OutputConnector outputConnector) {
        this.inputConnector = inputConnector;
        this.outputConnector = outputConnector;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = inputConnector.getInputStream();
                OutputStream outputStream = outputConnector.getOutputStream()
        ) {
            copyLarge(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
