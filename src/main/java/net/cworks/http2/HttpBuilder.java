/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: http
 * Package: net.cworks.http2
 * Class: HttpBuilder
 * Created: 9/3/14 10:28 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.http2;

import net.cworks.http.RequestMixin;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class HttpBuilder<CHILD extends HttpBuilder<CHILD>> {

    /**
     * Default HTTP connection timeout
     */
    public static final int CONNECTION_TIMEOUT = 60000;

    /**
     * Default timeout to use for requests
     */
    public static final int READ_TIMEOUT = 60000;

    /**
     * Url to use with this request
     */
    protected final String url;

    /**
     * Apache Http Client we're delegating to
     */
    //protected HttpClient client;

    /**
     * Apache Http Client Builder we're wrapping that will build the HttpClient
     */
    protected HttpClientBuilder builder;

    /**
     * If this client is set we use it not the builder
     */
    protected HttpClient client;

    /**
     * Port to which we're communicating, port 80 unless overridden
     */
    protected Integer port = 80;

    /**
     * Rendered path (i.e. has variable substitutions made)
     */
    protected String path;

    /**
     * username to use for basic authentication
     */
    protected String username;

    /**
     * password to use for basic authentication
     */
    protected String password;

    /**
     * Http request headers
     */
    protected List<Header> headers;

    /**
     * Http URL parameters
     */
    protected List<NameValuePair> params;

    /**
     * Request mixins that will be applied to request before its sent
     */
    protected List<RequestMixin> mixins;

    /**
     * default character set, can be overridden
     */
    protected String charset = "UTF-8";

    /**
     * The actual apache request instance we're building
     */
    protected HttpUriRequest request;

    /**
     * Creates a new builder object for the given URL.
     *
     * @param url the URL for the current request.
     * @throws NullPointerException if the given URL was null
     */
    protected HttpBuilder(final String url) {
        if (url == null) {
            throw new NullPointerException("URL must not be null.");
        }
        this.url = url;
    }

    /**
     * Appends params to send with this request.
     *
     * @param params the params to append to this request
     * @return this builder
     * @throws UnsupportedOperationException if this request not supports params modifications
     */
    public CHILD params(final NameValuePair... params) {
        if(params != null) {
            final List<NameValuePair> paramsList = getParams();
            for (final NameValuePair param : params) {
                if (param != null) {
                    paramsList.add(param);
                }
            }
        }
        return (CHILD)this;
    }

    /**
     * Appends a new {@link NameValuePair}, specified by the given
     * {@code name} and {@code value}, to this request.
     *
     * @param name  the name of the parameter to add to this request
     * @param value the value of the parameter to add to this request
     * @return this builder
     * @throws UnsupportedOperationException if this request not supports params modifications
     */
    public CHILD param(final String name, final String value) {
        getParams().add(new BasicNameValuePair(name, value));
        return (CHILD)this;
    }

    /**
     * Appends the String representation of each key-value-pair of the given
     * map to this request.
     *
     * @param params the {@link java.util.Map} containing the params to append to this request
     * @return this builder
     * @throws UnsupportedOperationException if this request not supports params modifications
     */
    public CHILD params(final Map<?, ?> params) {
        final List<NameValuePair> paramsList = getParams();
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            final String name = entry.getKey().toString();
            final String value = entry.getValue() == null ? null : entry.getValue().toString();
            paramsList.add(new BasicNameValuePair(name, value));
        }
        return (CHILD)this;
    }

    /**
     * Specifies the {@linkplain org.apache.http.client.HttpClient client} to use for this request.
     *
     * @param client the client
     * @return this builder
     * @throws NullPointerException if the given {@link org.apache.http.client.HttpClient} was null
     */
    public CHILD use(final HttpClient client) {
        if (client == null) {
            throw new NullPointerException("HttpClient must not be null.");
        }
        this.client = client;
        return (CHILD)this;
    }

    /**
     * Specifies the {@linkplain HttpClient client} to use for this request.
     *
     * @param builder the builder for the client
     * @return this builder
     * @throws NullPointerException if the given {@link HttpClient} was null
     */
    public CHILD use(final HttpClientBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("HttpClientBuilder must not be null.");
        }
        this.builder = builder;
        return (CHILD)this;
    }

    /**
     * Adds the given {@linkplain net.cworks.http.RequestMixin request mixins} to
     * this request. All mixins are being applied sequentially just
     * before the request is being executed.
     *
     * @param mixin the mixin to add to this request
     * @return this builder
     */
    public CHILD mixin(final RequestMixin mixin) {
        getMixins().add(mixin);
        return (CHILD)this;
    }

    /**
     * Adds a header with the given {@code name} and {@code value}
     * to this request.
     *
     * @param name
     * @param value
     * @return this builder
     */
    public CHILD header(final String name, final String value) {
        getHeaders().add(new BasicHeader(name, value));
        return (CHILD)this;
    }

    /**
     * Adds the given {@linkplain org.apache.http.Header header} to this request.
     *
     * @param header
     * @return this builder
     */
    public CHILD header(final Header header) {
        getHeaders().add(header);
        return (CHILD)this;
    }

    /**
     * Sets the encoding for this request.
     *
     * @param charset
     * @return this builder
     */
    public CHILD charset(final String charset) {
        this.charset = charset;
        return (CHILD)this;
    }

    /**
     * Sets the port to which this client will send the request and if set
     * provide basic userpass authentication.
     * @param port
     * @return
     */
    public CHILD port(Integer port) {
        this.port = port;
        return (CHILD)this;
    }

    /**
     * Username to use for basic user authentication
     * @param username
     * @return
     */
    public CHILD username(String username) {
        this.username = username;
        return (CHILD)this;
    }

    /**
     * Password to use for basic user authentication
     * @param password
     * @return
     */
    public CHILD password(String password) {
        this.password = password;
        return (CHILD)this;
    }

    protected List<NameValuePair> getParams() {
        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }
        return params;
    }

    protected List<Header> getHeaders() {
        if (headers == null) {
            headers = new ArrayList<Header>();
        }
        return headers;
    }

    protected List<RequestMixin> getMixins() {
        if (mixins == null) {
            mixins = new ArrayList<RequestMixin>();
        }
        return mixins;
    }

    protected String urlWithParameters() throws URISyntaxException {
        final URIBuilder builder = new URIBuilder(url);
        final List<NameValuePair> params = getParams();
        for (final NameValuePair param : params) {
            builder.addParameter(param.getName(), param.getValue());
        }
        return builder.toString();
    }

    /**
     * Executes this request and returns the content body of the result as a
     * String. If no response body exists, this returns {@code null}.
     *
     * @return the response body as a String or {@code null} if
     *         no response body exists.
     * @throws IOException if an error occurs while execution
     */
    public String asString() throws IOException {
        return Http2.asString(asResponse(), charset);
    }

    /**
     * Executes this request and returns the result as a
     * {@linkplain org.apache.http.HttpResponse} object.
     *
     * @return the response of this request
     * @throws IllegalStateException if no {@link HttpClient} was specified
     * @throws java.io.IOException           if an error occurs while execution
     */
    public HttpResponse asResponse() throws IOException {
        final HttpResponse response;
        if(client != null) {
            request = createFinalRequest();
            response = client.execute(request);
            return response;
        }

        // at this point if builder is null we're jacked
        if (builder == null) {
            throw new IllegalStateException(
                "Please specify either an HttpClient or HttpClientBuilder instance to use " +
                        "for this request.");
        }

        request = createFinalRequest();
        HttpClient myClient = null;
        if(!Http2.isNull(username) && !Http2.isNull(password)) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                new AuthScope(url, port),
                new UsernamePasswordCredentials(username, password));
            myClient = builder.setDefaultCredentialsProvider(credsProvider).build();
        } else {
            myClient = builder.build();
        }

        response = myClient.execute(request);
        return response;
    }

    private HttpUriRequest createFinalRequest() throws IOException {
        final HttpUriRequest request = createRequest();

        applyHeaders(request);
        applyMixins(request);

        return request;
    }

    private void applyHeaders(final HttpRequest request) {
        if (headers != null) {
            for (final Header h : headers) {
                request.setHeader(h);
            }
        }
    }

    private void applyMixins(final HttpUriRequest request) {
        if (mixins != null) {
            for (final RequestMixin modifier : mixins) {
                modifier.mixin(request);
            }
        }
    }

    @SuppressWarnings("unchecked")
    abstract protected HttpUriRequest createRequest() throws IOException;
}
