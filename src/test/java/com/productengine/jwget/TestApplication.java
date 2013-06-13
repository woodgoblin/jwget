package com.productengine.jwget;

import com.productengine.jwget.io.ByteArrayInputConnector;
import com.productengine.jwget.io.ByteArrayOutputConnector;
import com.productengine.jwget.io.InputConnector;
import com.productengine.jwget.io.OutputConnector;
import com.productengine.jwget.utils.ChunkGenerator;
import com.productengine.jwget.utils.Factory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class TestApplication {

    @Test
    public void testDownload() throws Exception {
        final byte[] expected = "oololoololoololololoololoololoololoololoololoololoololooololoololoololololo".getBytes();
        final byte[] actual = new byte[expected.length];

        ChunkGenerator chunks = new ChunkGenerator(expected.length, 32);

        Application.download(
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



}
