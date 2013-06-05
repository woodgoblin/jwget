package com.productengine.jwget;

import com.productengine.jwget.io.FileOutputConnector;
import com.productengine.jwget.io.NetworkInputConnector;
import com.productengine.jwget.utils.ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        URL url = new URL("http://programming-motherfucker.com");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        RandomAccessFile file = new RandomAccessFile("file", "rws");

        Iterator<ChunkGenerator.Chunk> chunkIterator = new ChunkGenerator(url.openConnection().getContentLength(), 10).iterator();

        for (int i = 0; i < 10; i++) {
            executorService.submit(new Downloader(
                    new NetworkInputConnector(url),
                    new FileOutputConnector(file),
                    chunkIterator
            ));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        file.close();
    }
}
