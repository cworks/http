/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:32 PM
 */
package net.cworks.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Saves the stream to a file and returns <code>true</code>, if no error
 * occurred while saving.
 *
 * @see {@link HttpRequestBuilder#asFile(java.io.File)}
 */
public class FileResponseHandler implements ResponseHandler<Boolean> {

    protected final File file;
    protected final String url;

    public FileResponseHandler(final File file, final String url) {
        this.file = file;
        this.url = url;
    }

    @Override
    public Boolean handleResponse(final HttpResponse response) throws IOException {
        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode >= 300) {
            throw new FileNotFoundException("Source not found at " + url
                    + ", response code " + statusCode);
        }

        final HttpEntity entity = response.getEntity();
        if (entity == null) {
            return false;
        }

        return copyStreamToFile(entity.getContent(), file);
    }

    protected boolean copyStreamToFile(final InputStream source, final File target) throws IOException {
        final byte[] buffer = new byte[1024 * 4];
        final OutputStream out = new FileOutputStream(target);
        int read = 0;
        while ((read = source.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        try {
            out.close();
        } catch (final IOException ignore) {
        }
        return true;
    }

}
