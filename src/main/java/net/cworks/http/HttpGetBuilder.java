/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:29 PM
 */
package net.cworks.http;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * GET-request builder.
 *
 */
public class HttpGetBuilder extends HttpRequestBuilder {

    public HttpGetBuilder(final String url) {
        super(url);
    }

    public HttpGetBuilder(final String url, final HttpClient client) {
        this(url);
        use(client);
    }

    @Override
    protected HttpUriRequest createRequest() throws IOException {
        try {
            String url = urlWithParameters();
            return new HttpGet(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
