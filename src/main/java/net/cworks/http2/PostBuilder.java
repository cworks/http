/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: http
 * Package: net.cworks.http2
 * Class: PostBuilder
 * Created: 9/3/14 10:31 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.http2;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostBuilder<T> extends HttpBuilder<PostBuilder<T>> {

    // only 1 of these 3 can be set {entity, data, body}
    protected HttpEntity entity;
    protected List<NameValuePair> data;
    protected String body;

    protected PostBuilder(final String url) {
        super(url);
    }

    public PostBuilder entity(final HttpEntity entity) {
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
    public PostBuilder data(final String name, final String value) {
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
    public PostBuilder data(final NameValuePair... data) {
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
    public PostBuilder data(final Map<?, ?> data) {
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
    public PostBuilder body(final String body) {
        ensureNoData();
        ensureNoEntity();
        this.body = body;
        return this;
    }

    @Override
    protected HttpUriRequest createRequest() throws IOException {
        final HttpPost request;
        try {
            String url = urlWithParameters();
            request = new HttpPost(url);
            HttpEntity httpEntity = createEntity();
            request.setEntity(httpEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

    protected HttpEntity createEntity() throws UnsupportedEncodingException {
        HttpEntity httpEntity = null;

        if(Http2.isNull(entity) && Http2.isNull(data) && Http2.isNull(body)) {
            throw new IllegalArgumentException("one of {entity, data, body} must be non-null");
        } else if(Http2.isNull(entity) && Http2.isNull(data) && !Http2.isNull(body)) {
            httpEntity = new StringEntity(body, charset);
        } else if(Http2.isNull(entity) && !Http2.isNull(data) && Http2.isNull(body)) {
            httpEntity = new UrlEncodedFormEntity(data, charset);
        } else if(!Http2.isNull(entity) && Http2.isNull(data) && Http2.isNull(body)) {
            httpEntity = entity;
        }

        return httpEntity;
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
}
