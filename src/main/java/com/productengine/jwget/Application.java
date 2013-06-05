package com.productengine.jwget;

import com.productengine.jwget.io.FileOutputConnector;
import com.productengine.jwget.io.NetworkInputConnector;
import com.productengine.jwget.utils.ChunkGenerator;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        URL url = new URL("http://programming-motherfucker.com");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        RandomAccessFile file = new RandomAccessFile("file", "rws");

        ChunkGenerator chunkGenerator = new ChunkGenerator(url.openConnection().getContentLength(), 1024);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Downloader(
                    new NetworkInputConnector(url),
                    new FileOutputConnector(file),
                    chunkGenerator
            ));
        }

        executorService.awaitTermination(10, TimeUnit.MINUTES);
        file.close();
    }
}
