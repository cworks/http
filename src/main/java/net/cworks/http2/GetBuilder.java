/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: http
 * Package: net.cworks.http2
 * Class: GetBuilder
 * Created: 9/3/14 10:29 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.http2;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetBuilder<T> extends HttpBuilder<GetBuilder<T>> {

    protected GetBuilder(final String url) {
        super(url);
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
