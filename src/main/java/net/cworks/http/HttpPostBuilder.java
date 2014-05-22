/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:31 PM
 */
package net.cworks.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * POST-request builder. Supports data, body and entity modifications.
 *
 */
public class HttpPostBuilder extends HttpRequestBuilder {

    // only 1 of these 3 can be set {entity, data, body}
    protected HttpEntity entity;
    protected List<NameValuePair> data;
    protected String body;

    public HttpPostBuilder(final String url) {
        super(url);
    }

    @Override
    public HttpRequestBuilder entity(final HttpEntity entity) {
        ensureNoData();
        ensureNoBody();
        this.entity = entity;
        return this;
    }

    /**
     * encoded form data
     * @param name
     * @param value
     * @return
     */
    public HttpRequestBuilder data(final String name, final String value) {
        ensureNoEntity();
        ensureNoBody();
        getData().add(new BasicNameValuePair(name, value));
        return this;
    }

    /**
     * encoded form data
     * @param data
     * @return
     */
    public HttpRequestBuilder data(final NameValuePair... data) {
        ensureNoEntity();
        ensureNoBody();
        if (data != null) {
            final List<NameValuePair> dataList = getData();
            for (final NameValuePair d : data) {
                if (d != null) {
                    dataList.add(d);
                }
            }
        }
        return this;
    }

    /**
     * encoded form data
     * @param data
     * @return
     */
    public HttpRequestBuilder data(final Map<?, ?> data) {
        ensureNoEntity();
        ensureNoBody();
        final List<NameValuePair> dataList = getData();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            final String name = entry.getKey().toString();
            final String value = entry.getValue() == null ? null : entry.getValue().toString();
            dataList.add(new BasicNameValuePair(name, value));
        }
        return this;
    }

    /**
     * encoded string body
     * @param body
     * @return
     */
    public HttpRequestBuilder body(final String body) {
        ensureNoData();
        ensureNoEntity();
        this.body = body;
        return this;
    }

    @Override
    protected HttpUriRequest createRequest() throws IOException {
        final HttpPost request;
        try {
            final URIBuilder builder = new URIBuilder(url);
            final List<NameValuePair> dataList = getParams();
            for (final NameValuePair d : dataList) {
                builder.addParameter(d.getName(), d.getValue());
            }

            request = new HttpPost(builder.toString());
            HttpEntity httpEntity = null;

            if(isNull(entity) && isNull(data) && isNull(body)) {
                throw new IllegalArgumentException("one of {entity, data, body} must be non-null");
            } else if(isNull(entity) && isNull(data) && !isNull(body)) {
                httpEntity = new StringEntity(body, charset);
            } else if(isNull(entity) && !isNull(data) && isNull(body)) {
                httpEntity = new UrlEncodedFormEntity(data, charset);
            } else if(!isNull(entity) && isNull(data) && isNull(body)) {
                httpEntity = entity;
            }
            request.setEntity(httpEntity);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

    protected List<NameValuePair> getData() {
        if (data == null) {
            data = new ArrayList<NameValuePair>();
        }
        return data;
    }

    private void ensureNoEntity() {
        if(entity != null) {
            throw new IllegalStateException(
                "You can't set a body OR data after specifying an entity.");
        }
    }

    private void ensureNoData() {
        if(data != null) {
            throw new IllegalStateException(
                "You can't specify an entity OR body after setting POST data.");
        }
    }

    private void ensureNoBody() {
        if(body != null) {
            throw new IllegalStateException(
                "You can't specify an entity OR data after setting the POST body.");
        }
    }

    private static boolean isNull(Object o) {
        if(o == null) {
            return true;
        }
        return false;
    }


}
