package com.productengine.jwget;

import com.productengine.jwget.io.net.HttpInputStream;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.apache.commons.io.IOUtils.contentEquals;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpInputStreamTest {

    @Test
    public void testOkStatus() throws IOException {
        HttpInputStream httpInputStream = new HttpInputStream(toInputStream(
                "HTTP/1.1 200 OK\r\n" +
                        "Date: Tue, 04 Jun 2013 01:42:51 GMT\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 13092\r\n" +
                        "Last-Modified: Tue, 25 Sep 2012 23:13:44 GMT\r\n" +
                        "ETag: 50623aa8-3324\r\n" +
                        "Server: Mongrel2/1.7.5\r\n" +
                        "\r\n" +
                        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"eng\">\n" +
                        "</html>"
        ));

        assertEquals(HttpInputStream.HttpStatus.OK, httpInputStream.getStatus());
    }

    @Test
    public void testNotFoundStatus() throws IOException {
        HttpInputStream httpInputStream = new HttpInputStream(toInputStream(
                "HTTP/1.1 404 Not Found\r\n" +
                        "Date: Tue, 04 Jun 2013 01:42:51 GMT\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 13092\r\n" +
                        "Last-Modified: Tue, 25 Sep 2012 23:13:44 GMT\r\n" +
                        "ETag: 50623aa8-3324\r\n" +
                        "Server: Mongrel2/1.7.5\r\n" +
                        "\r\n"
        ));

        assertEquals(HttpInputStream.HttpStatus.NOT_FOUND, httpInputStream.getStatus());
    }


    @Test
    public void testEntity() throws IOException {
        HttpInputStream httpInputStream = new HttpInputStream(toInputStream(
                "HTTP/1.1 200 OK\r\n" +
                        "Date: Tue, 04 Jun 2013 01:42:51 GMT\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 13092\r\n" +
                        "Last-Modified: Tue, 25 Sep 2012 23:13:44 GMT\r\n" +
                        "ETag: 50623aa8-3324\r\n" +
                        "Server: Mongrel2/1.7.5\r\n" +
                        "\r\n" +
                        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"eng\">\n" +
                        "</html>"
        ));

        assertTrue(contentEquals(
                toInputStream(
                        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                                "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"eng\">\n" +
                                "</html>"
                ),
                httpInputStream
        ));
    }

    @Test
    public void test() throws IOException {
        InputStream s1 = toInputStream("asdasd");
        InputStreamReader r = new InputStreamReader(s1);
        r.read();
        int read1 = s1.read();

        InputStream s2 = toInputStream("asdasd");
        s2.read();
        s2.read();
        char read2 = (char) r.read();


        int a = 1;

    }

}
