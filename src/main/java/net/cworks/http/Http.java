/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:15 PM
 */
package net.cworks.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

public final class Http {

    /**
     * Default HTTP connection timeout
     */
    public static final int CONNECTION_TIMEOUT = 60000;

    /**
     * Default timeout to use for requests to Cs2
     */
    public static final int READ_TIMEOUT = 60000;

    /**
     * create default HttpClient
     */
    private static HttpClient createHttpClient() {
        HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        HttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig())
            .setConnectionManager(connectionManager)
            .build();
        return httpClient;
    }

    /**
     * create default Http request config for HttpClient
     * @return
     */
    private static RequestConfig requestConfig() {
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setConnectionRequestTimeout(READ_TIMEOUT)
            .build();
        return config;
    }

    Http() { }

    /**
     * Creates a builder object for a GET-request. Supports no params nor entity
     * modifications.
     *
     * @param url the URL to use for this request.
     * @return the builder object for the this URL.
     */
    public static HttpGetBuilder get(final String url) {
        return new HttpGetBuilder(url, createHttpClient());
    }

    /**
     * Creates a builder object for a POST-request. Supports params and entity
     * modifications.
     *
     * @param url the URL to use for this request.
     * @return the builder object for this URL.
     */
    public static HttpPostBuilder post(final String url) {
        return new HttpPostBuilder(url, createHttpClient());
    }

    /**
     * Creates a builder object for a Multipart Post request.
     *
     * @param url the URL to use for this request.
     * @return the builder object for this URL.
     */
    public static HttpMultiPartBuilder multipart(final String url) {
        return new HttpMultiPartBuilder(url, createHttpClient());
    }

    /**
     * Creates a builder object for a Put request
     * @param url the URL to use for Put
     * @return the builder object for this URL.
     */
    public static HttpPutBuilder put(final String url) {
        return new HttpPutBuilder(url, createHttpClient());
    }

    /**
     * Creates a builder object for a Delete request
     * @param url the URL to use for Delete
     * @return the builder object for this URL.
     */
    public static HttpDeleteBuilder delete(final String url) {
        return new HttpDeleteBuilder(url, createHttpClient());
    }

    /**
     * Converts a {@linkplain HttpResponse} to a String by calling
     * {@link EntityUtils#toString(HttpEntity)} on its {@linkplain HttpEntity
     * entity}.
     *
     * @param response the {@linkplain HttpResponse response} to convert.
     * @param defaultCharset character set to be applied if none found in the entity.
     * @return the response body as a String or {@code null}, if no
     *         response body exists or an error occurred while converting.
     * @throws NullPointerException if the given response was null
     */
    public static String asString(final HttpResponse response, final String defaultCharset) {
        if (response == null) {
            throw new NullPointerException();
        }

        final HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }

        try {
            return EntityUtils.toString(entity, defaultCharset == null ? (Charset) null : Charset.forName(defaultCharset));
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Utility to check an object for being null
     * @param o
     * @return
     */
    static boolean isNull(Object o) {
        if(o == null) {
            return true;
        }
        return false;
    }

    /**
     * Quietly close a Closeable
     * @param closable
     */
    public static void closeQuietly(Closeable closable) {
        try {
            closable.close();
        } catch (final IOException ignore) { }
    }

}

