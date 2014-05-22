/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:15 PM
 */
package net.cworks.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

public final class Http {

    Http() { }

    /**
     * Creates a builder object for a POST-request. Supports params and entity
     * modifications.
     *
     * @param url the URL to use for this request.
     * @return the builder object for this URL.
     */
    public static HttpPostBuilder post(final String url) {
        return new HttpPostBuilder(url);
    }

    /**
     * Creates a builder object for a GET-request. Supports no params nor entity
     * modifications.
     *
     * @param url the URL to use for this request.
     * @return the builder object for the this URL.
     */
    public static HttpGetBuilder get(final String url) {
        return new HttpGetBuilder(url);
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

}

