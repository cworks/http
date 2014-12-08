/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: http
 * Package: net.cworks.http2
 * Class: Http2
 * Created: 9/3/14 11:02 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.http2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

public class Http2 {

    /**
     * Default HTTP connection timeout
     */
    static final int CONNECTION_TIMEOUT = 60000;

    /**
     * Default timeout to use for requests
     */
    static final int READ_TIMEOUT = 60000;

    /**
     * Creates a builder object for a GET-request. Supports no params nor entity
     * modifications.
     *
     * @param url the URL to use for this request.
     * @return the builder object for the this URL.
     */
    public static GetBuilder get(final String url) {
        GetBuilder builder = new GetBuilder(url);
        builder.use(createHttpClientBuilder());
        return builder;
    }

    /**
     * Creates a builder object for a POST-request. Supports params and entity
     * modifications.
     *
     * @param url the URL to use for this request.
     * @return the builder object for this URL.
     */
    public static PostBuilder post(final String url) {
        PostBuilder builder = new PostBuilder(url);
        builder.use(createHttpClientBuilder());
        return builder;
    }

    /**
     * Converts a {@linkplain org.apache.http.HttpResponse} to a String by calling
     * {@link org.apache.http.util.EntityUtils#toString(org.apache.http.HttpEntity)} on
     * its {@linkplain org.apache.http.HttpEntity entity}.
     *
     * @param response the {@linkplain org.apache.http.HttpResponse response} to convert.
     * @param defaultCharset character set to be applied if none found in the entity.
     * @return the response body as a String or {@code null}, if no
     *         response body exists or an error occurred while converting.
     * @throws NullPointerException if the given response was null
     */
    static String asString(final HttpResponse response, final String defaultCharset) {
        if(response == null) {
            throw new NullPointerException();
        }

        final HttpEntity entity = response.getEntity();
        if(entity == null) {
            return null;
        }

        try {
            return EntityUtils.toString(
                entity, defaultCharset == null ? (Charset) null : Charset.forName(defaultCharset));
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * create HttpClientBuilder
     * @return
     */
    static HttpClientBuilder createHttpClientBuilder() {
        HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig())
            .setConnectionManager(connectionManager);

        return httpClientBuilder;
    }

    /**
     * create default Http request config for HttpClient
     * @return
     */
     static RequestConfig requestConfig() {
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setConnectionRequestTimeout(READ_TIMEOUT)
            .build();
        return config;
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
}
