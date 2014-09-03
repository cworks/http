/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Baked with love by comartin
 * Package: net.cworks.http
 * User: comartin
 * Created: 5/28/2014 3:37 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpPutBuilder extends HttpPostBuilder {

    protected HttpPutBuilder(final String url) {
        super(url);
    }

    protected HttpPutBuilder(final String url, final HttpClientBuilder builder) {
        super(url, builder);

    }

    @Override
    protected HttpUriRequest createRequest() throws IOException {
        final HttpPut request;
        try {
            String url = urlWithParameters();
            request = new HttpPut(url);
            HttpEntity httpEntity = createEntity();
            request.setEntity(httpEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return request;
    }
}
