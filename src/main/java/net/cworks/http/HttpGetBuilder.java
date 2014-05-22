/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:29 PM
 */
package net.cworks.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * GET-request builder.
 *
 */
public class HttpGetBuilder extends HttpRequestBuilder {

    public HttpGetBuilder(final String url) {
        super(url);
    }

    @Override
    protected HttpUriRequest createRequest() throws IOException {
        try {
            final URIBuilder builder = new URIBuilder(url);
            final List<NameValuePair> params = getParams();
            for (final NameValuePair param : params) {
                builder.addParameter(param.getName(), param.getValue());
            }
            return new HttpGet(builder.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
