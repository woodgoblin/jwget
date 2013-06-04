package com.productengine.jwget;

import com.productengine.jwget.utils.ChunkGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChunkGeneratorTest {

    @Test
    public void testLastChunk() {
        ChunkGenerator chunkGenerator = new ChunkGenerator(17 * 1024 + 763, 1024);

        ChunkGenerator.Chunk lastChunk = null;

        for (ChunkGenerator.Chunk chunk : chunkGenerator) {
            lastChunk = chunk;
        }

        assertNotNull(lastChunk);
        assertEquals(763, lastChunk.getLength());
        assertEquals(17 * 1024, lastChunk.getOffset());
    }

    @Test
    public void testOneChunk() {
        ChunkGenerator chunkGenerator = new ChunkGenerator(1024 + 763, 2048);

        int chunksCount = 0;
        ChunkGenerator.Chunk lastChunk = null;

        for (ChunkGenerator.Chunk chunk : chunkGenerator) {
            chunksCount++;
            lastChunk = chunk;
        }

        assertEquals(1, chunksCount);

        assertNotNull(lastChunk);
        assertEquals(1024 + 763, lastChunk.getLength());
        assertEquals(0, lastChunk.getOffset());
    }

    @Test(expected = NoSuchElementException.class)
    public void testConcurrentAccess() throws Throwable {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ChunkGenerator chunkGenerator = new ChunkGenerator(17 * 1024, 1024);


        for (final Iterator<ChunkGenerator.Chunk> iterator = chunkGenerator.iterator(); iterator.hasNext(); ) {
            try {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        iterator.next();
                    }
                }).get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }

            ChunkGenerator.Chunk chunk = iterator.next();
        }
    }

}
