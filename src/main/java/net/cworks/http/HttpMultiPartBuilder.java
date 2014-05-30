/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/29/14
 * Time: 2:15 PM
 */
package net.cworks.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HttpMultiPartBuilder extends HttpPostBuilder {

    private static final Integer MAX_UPLOAD = 100;

    private static class FileInfo {
        File file = null;
        ContentType contentType;
        FileInfo(File file, ContentType contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }

    protected List<FileInfo> files = null;

    public HttpMultiPartBuilder(String url) {
        super(url);
        files = new ArrayList<FileInfo>();
    }

    public HttpMultiPartBuilder(String url, HttpClient client) {
        super(url, client);
        files = new ArrayList<FileInfo>();
    }

    /**
     * Add to the upload list
     * @param file
     */
    public HttpMultiPartBuilder upload(File file) {
        upload(file, ContentType.APPLICATION_OCTET_STREAM);
        return this;
    }

    public HttpMultiPartBuilder upload(File file, ContentType contentType) {
        if(files.size() < MAX_UPLOAD) {
            files.add(new FileInfo(file, contentType));
        }
        return this;
    }

    /**
     * Create a Multipart form data request which can include typical form data
     * and/or an actual file to upload.
     *
     * @return
     * @throws IOException
     */
    @Override
    protected HttpUriRequest createRequest() throws IOException {
        final HttpPost request;
        try {
            String url = urlWithParameters();
            request = new HttpPost(url);
            // creates a multipart entity
            HttpEntity httpEntity = createEntity();
            request.setEntity(httpEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

    protected HttpEntity createEntity() throws UnsupportedEncodingException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // add usual form parameters this way
        for(FileInfo fileInfo : files) {
            builder.addBinaryBody(fileInfo.file.getName(), fileInfo.file);
        }

        for(NameValuePair pair : getData()) {
            builder.addPart(pair.getName(),
                new StringBody(pair.getValue(), ContentType.TEXT_PLAIN));
        }

        return builder.build();
    }
}
