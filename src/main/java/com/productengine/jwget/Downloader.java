package com.productengine.jwget;

import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;
import com.productengine.jwget.utils.ChunkGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import static org.apache.commons.io.IOUtils.copyLarge;

public class Downloader implements Runnable {

    private final InputConnector inputConnector;
    private final OutputConnector outputConnector;
    private final Iterator<ChunkGenerator.Chunk> chunkIterator;

    public Downloader(InputConnector inputConnector, OutputConnector outputConnector, Iterator<ChunkGenerator.Chunk> chunkIterator) {
        this.inputConnector = inputConnector;
        this.outputConnector = outputConnector;
        this.chunkIterator = chunkIterator;
    }

    @Override
    public void run() {
        while (chunkIterator.hasNext()) {
            ChunkGenerator.Chunk chunk = chunkIterator.next();

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
