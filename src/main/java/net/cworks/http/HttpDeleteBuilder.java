/**
 * Created with love by corbett.
 * User: corbett
 * Date: 6/5/14
 * Time: 10:16 AM
 */
package net.cworks.http;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpDeleteBuilder extends HttpRequestBuilder {

    public HttpDeleteBuilder(final String url) {
        super(url);
    }

    public HttpDeleteBuilder(final String url, final HttpClientBuilder builder) {
        this(url);
        use(builder);
    }

    @Override
    protected HttpUriRequest createRequest() throws IOException {
        try {
            String url = urlWithParameters();
            return new HttpDelete(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
