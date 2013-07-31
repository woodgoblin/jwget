package com.myzone.jwget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.net.URL;

import static com.myzone.jwget.Application.download;
import static com.myzone.jwget.io.FileMatchers.contentEqualsToIgnoreEOL;
import static java.nio.file.Files.delete;
import static org.apache.commons.io.IOUtils.copy;
import static org.junit.Assert.*;

public class ApplicationTest {

    private File origin;

    private URL url;
    private File destination;
    private int workersCount;
    private int chunkSize;

    @Before
    public void setUp() throws Exception {
        origin = new File("resources/origin.html");

        url = new URL("http://programming-motherfucker.com/");
        destination = new File("programming-motherfucker.html");
        workersCount = 4;
        chunkSize = 512;
    }

    @After
    public void tearDown() throws Exception {
        System.gc();

        delete(destination.toPath());
    }

    @Test
    public void testFileCreation() throws Exception {
        assertFalse(destination.exists());

        download(url, destination, workersCount, chunkSize);

        assertTrue(destination.exists());
        assertThat(destination, contentEqualsToIgnoreEOL(origin));
    }

    @Test
    public void testFileRewrite() throws Exception {
        destination.createNewFile();
        copy(new StringReader("bla-bla-bla"), new FileOutputStream(destination));

        download(url, destination, workersCount, chunkSize);

        assertTrue(destination.exists());
        assertThat(destination, contentEqualsToIgnoreEOL(origin));
    }

}
