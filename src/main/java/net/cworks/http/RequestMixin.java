/**
 * Created with love by corbett.
 * User: corbett
 * Date: 5/20/14
 * Time: 7:25 PM
 */
package net.cworks.http;

import org.apache.http.client.methods.HttpUriRequest;

/**
 * May be used to modify the
 * {@linkplain org.apache.http.client.methods.HttpUriRequest request}
 * before its executed.
 *
 */
public interface RequestMixin {

    /**
     * Customizes the request before the execution is done.
     *
     * @param request the request to mixin
     */
    public void mixin(final HttpUriRequest request);
}
