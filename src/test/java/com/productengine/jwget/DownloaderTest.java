package com.productengine.jwget;

import com.productengine.jwget.io.ByteArrayInputConnector;
import com.productengine.jwget.io.ByteArrayOutputConnector;
import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;
import com.productengine.jwget.utils.ChunkGenerator;
import com.productengine.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DownloaderTest {

    private Downloader downloader;

    @Before
    public void setUp() throws Exception {
        downloader = new Downloader();
    }

    @Test
    public void testDownloadInOneThread() throws Exception {
        final byte[] expected = "oololoololoololololoololoololoololoololoololoololoololooololoololoololololo".getBytes();
        final byte[] actual = new byte[expected.length];

        ChunkGenerator chunks = new ChunkGenerator(expected.length, 32);

        downloader.download(
                new Factory<InputConnector>() {
                    @NotNull
                    @Override
                    public InputConnector create() {
                        return new ByteArrayInputConnector(expected);
                    }
                },
                new Factory<OutputConnector>() {
                    @NotNull
                    @Override
                    public OutputConnector create() throws Exception {
                        return new ByteArrayOutputConnector(actual);
                    }
                },
                chunks.iterator(),
                1
        );

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDownloadManyThreads() throws Exception {
        final byte[] expected = "oololoololoololololoololoololoololoololoololoololoololooololoololoololololo".getBytes();
        final byte[] actual = new byte[expected.length];

        ChunkGenerator chunks = new ChunkGenerator(expected.length, 32);

        downloader.download(
                new Factory<InputConnector>() {
                    @NotNull
                    @Override
                    public InputConnector create() {
                        return new ByteArrayInputConnector(expected);
                    }
                },
                new Factory<OutputConnector>() {
                    @NotNull
                    @Override
                    public OutputConnector create() throws Exception {
                        return new ByteArrayOutputConnector(actual);
                    }
                },
                chunks.iterator(),
                10
        );

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testDownloadFailedData() throws Exception {
        final byte[] expected = "oololoololoololololoololoololoololoololoololoololoololooololoololoololololo".getBytes();
        final byte[] actual = new byte[expected.length];

        ChunkGenerator chunks = new ChunkGenerator(expected.length + 5, 32);

        downloader.download(
                new Factory<InputConnector>() {
                    @NotNull
                    @Override
                    public InputConnector create() {
                        return new ByteArrayInputConnector(expected);
                    }
                },
                new Factory<OutputConnector>() {
                    @NotNull
                    @Override
                    public OutputConnector create() throws Exception {
                        return new ByteArrayOutputConnector(actual);
                    }
                },
                chunks.iterator(),
                10
        );

        assertArrayEquals(expected, actual);
    }

}
