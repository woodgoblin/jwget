package com.productengine.jwget;

import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;
import com.productengine.jwget.utils.ChunkGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.copyLarge;

public class Downloader implements Runnable {

    private final InputConnector inputConnector;
    private final OutputConnector outputConnector;
    private final ChunkGenerator chunkGenerator;

    public Downloader(InputConnector inputConnector, OutputConnector outputConnector, ChunkGenerator chunkGenerator) {
        this.inputConnector = inputConnector;
        this.outputConnector = outputConnector;
        this.chunkGenerator = chunkGenerator;
    }

    @Override
    public void run() {
        for (ChunkGenerator.Chunk chunk : chunkGenerator) {
            try (
                    InputStream inputStream = inputConnector.getSubStream(chunk.getOffset(), chunk.getLength());
                    OutputStream outputStream = outputConnector.getSubStream(chunk.getOffset(), chunk.getLength());
            ) {
                copyLarge(inputStream, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
