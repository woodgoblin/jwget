package com.productengine.jwget;

import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;
import com.productengine.jwget.utils.ChunkGenerator;
import com.productengine.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Downloader {

    public void download(
        final @NotNull Factory<? extends InputConnector> inputConnectorFactory,
        final @NotNull Factory<? extends OutputConnector> outputConnectorFactory,
        final @NotNull Iterator<ChunkGenerator.Chunk> chunkIterator,
        final int workersCount
    ) throws Exception, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(workersCount);

        for (int i = 0; i < workersCount; i++) {
            executorService.submit(new ChunkDownloader(
                    inputConnectorFactory.create(),
                    outputConnectorFactory.create(),
                    chunkIterator
            ));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

}
