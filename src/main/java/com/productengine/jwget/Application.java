package com.productengine.jwget;

import com.productengine.jwget.io.*;
import com.productengine.jwget.utils.ChunkGenerator;
import com.productengine.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final String PROPERTY_PREFIX = "jwget";
    private static final String DEFAULT_FILENAME = "file";

    public static void main(String[] args) throws Exception {
        URL url = new URL(System.getProperty(PROPERTY_PREFIX + "." + "url"));
        File destination = new File(System.getProperty(PROPERTY_PREFIX + "." + "destination", url.getFile()));
        int workersCount = Integer.parseInt(System.getProperty(PROPERTY_PREFIX + "." + "workersCount", "1"));
        int chunkSize = Integer.parseInt(System.getProperty(PROPERTY_PREFIX + "." + "chunkSize", "4096"));

        NetworkInputConnectorFactory inputConnectorFactory = new NetworkInputConnectorFactory(url);
        Iterator<ChunkGenerator.Chunk> chunkIterator = new ChunkGenerator(url.openConnection().getContentLength(), 10).iterator();
        try (RandomAccessFile file = new RandomAccessFile(destination, "rws")) {
            FileOutputConnectorFactory outputConnectorFactory = new FileOutputConnectorFactory(file);

            download(inputConnectorFactory, outputConnectorFactory, chunkIterator, workersCount);
        }
    }

    private static void download(
            final @NotNull Factory<? extends InputConnector> inputConnectorFactory,
            final @NotNull Factory<? extends OutputConnector> outputConnectorFactory,
            final @NotNull Iterator<ChunkGenerator.Chunk> chunkIterator,
            final int workersCount
    ) throws InterruptedException, Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(workersCount);

        for (int i = 0; i < workersCount; i++) {
            executorService.submit(new Downloader(
                    inputConnectorFactory.create(),
                    outputConnectorFactory.create(),
                    chunkIterator
            ));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }
}
