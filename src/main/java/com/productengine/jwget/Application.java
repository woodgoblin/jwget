package com.productengine.jwget;

import com.productengine.jwget.io.FileOutputConnectorFactory;
import com.productengine.jwget.io.NetworkInputConnectorFactory;
import com.productengine.jwget.utils.ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Iterator;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final String PROPERTY_PREFIX = "jwget";
    private static final Downloader DOWNLOADER = new Downloader();

    public static void main(String[] args) throws Exception {
        URL url = new URL(System.getProperty(PROPERTY_PREFIX + "." + "url"));
        File destination = new File(System.getProperty(PROPERTY_PREFIX + "." + "destination", url.getFile()));
        int workersCount = Integer.parseInt(System.getProperty(PROPERTY_PREFIX + "." + "workersCount", "1"));
        int chunkSize = Integer.parseInt(System.getProperty(PROPERTY_PREFIX + "." + "chunkSize", "4096"));

        LOGGER.info("Url: {}", url);
        LOGGER.info("Destination: {}", destination);
        LOGGER.info("Workers count: {}", workersCount);
        LOGGER.info("Chunk size: {}", chunkSize);

        Iterator<ChunkGenerator.Chunk> chunkIterator = new ChunkGenerator(url.openConnection().getContentLength(), 10).iterator();
        NetworkInputConnectorFactory inputConnectorFactory = new NetworkInputConnectorFactory(url);
        try (RandomAccessFile file = new RandomAccessFile(destination, "rws")) {
            FileOutputConnectorFactory outputConnectorFactory = new FileOutputConnectorFactory(file);

            DOWNLOADER.download(inputConnectorFactory, outputConnectorFactory, chunkIterator, workersCount);
        }
    }

}
