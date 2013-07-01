package com.productengine.jwget;

import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;
import com.productengine.jwget.utils.ChunkGenerator;
import com.productengine.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.io.IOUtils.copyLarge;

public class Downloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);

    public void download(
        final @NotNull Factory<? extends InputConnector> inputConnectorFactory,
        final @NotNull Factory<? extends OutputConnector> outputConnectorFactory,
        final @NotNull Iterator<ChunkGenerator.Chunk> chunkIterator,
        final int workersCount
    ) throws Factory.CreationException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(workersCount);

        for (int i = 0; i < workersCount; i++) {
            executorService.submit(new ChunkDownloadTask(
                    inputConnectorFactory.create(),
                    outputConnectorFactory.create(),
                    chunkIterator
            ));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    protected static class ChunkDownloadTask implements Runnable {

        private final InputConnector inputConnector;
        private final OutputConnector outputConnector;
        private final Iterator<ChunkGenerator.Chunk> chunkIterator;

        public ChunkDownloadTask(InputConnector inputConnector, OutputConnector outputConnector, Iterator<ChunkGenerator.Chunk> chunkIterator) {
            this.inputConnector = inputConnector;
            this.outputConnector = outputConnector;
            this.chunkIterator = chunkIterator;
        }

        @Override
        public void run() {
            while (chunkIterator.hasNext()) {
                ChunkGenerator.Chunk chunk = chunkIterator.next();

                try (
                        InputStream inputStream = inputConnector.getSubstream(chunk.getOffset(), chunk.getLength());
                        OutputStream outputStream = outputConnector.getSubstream(chunk.getOffset(), chunk.getLength());
                ) {
                    LOGGER.info("Downloading of {} has been started", chunk);

                    copyLarge(inputStream, outputStream);

                    LOGGER.info("Downloading of {} has been done", chunk);
                } catch (IOException exception) {
                    LOGGER.error("Failed to download {} in cause of {}", chunk, exception);
                }
            }
        }
    }
}
