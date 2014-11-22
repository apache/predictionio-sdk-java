package io.prediction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.*;
import com.ning.http.client.extra.ThrottleRequestFilter;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * BaseClient contains code common to both {@link EventClient} and {@link EngineClient}.
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.8.2
 * @since 0.1
 */
public abstract class BaseClient implements Closeable {
    private static final int defaultThreadLimit = 1;
    private static final String defaultApiVersion = "";
    private static final int defaultQSize = 0;
    private static final int defaultTimeout = 5;

    // HTTP status code
    static final int HTTP_OK = 200;
    static final int HTTP_CREATED = 201;

    // API Url
    final String apiUrl;

    final AsyncHttpClient client;

    final JsonParser parser = new JsonParser();

    /**
     * @param apiURL the URL of the PredictionIO API
     */
    public BaseClient(String apiURL) {
        this(apiURL, BaseClient.defaultThreadLimit);
    }

    /**
     * @param apiURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     */
    public BaseClient(String apiURL, int threadLimit) {
        this(apiURL, threadLimit, defaultQSize);
    }

    /**
     * @param apiURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     * @param qSize size of the queue
     */
    public BaseClient(String apiURL, int threadLimit, int qSize) {
        this(apiURL, threadLimit, qSize, defaultTimeout);
    }

    /**
     * @param apiURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     * @param qSize size of the queue
     * @param timeout timeout in seconds for the connections
     */
    public BaseClient(String apiURL, int threadLimit, int qSize, int timeout) {
        this.apiUrl = apiURL;
        // Async HTTP client config
        AsyncHttpClientConfig config = (new AsyncHttpClientConfig.Builder())
                .setAllowPoolingConnection(true)
                .setAllowSslConnectionPool(true)
                .addRequestFilter(new ThrottleRequestFilter(threadLimit))
                .setMaximumConnectionsPerHost(threadLimit)
                .setRequestTimeoutInMs(timeout * 1000)
                .setIOThreadMultiplier(threadLimit)
                .build();
        this.client = new AsyncHttpClient(new NettyAsyncHttpProvider(config), config);
    }

    /**
     * Close all connections associated with this client.
     * It is a good practice to always close the client after use.
     */
    @Override
    public void close() {
        client.close();
    }

    AsyncHandler<APIResponse> getHandler() {
        return new AsyncHandler<APIResponse>() {
            private final Response.ResponseBuilder builder = new Response.ResponseBuilder();

            public void onThrowable(Throwable t) {
            }

            public STATE onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
                builder.accumulate(content);
                return STATE.CONTINUE;
            }

            public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
                builder.accumulate(status);
                return STATE.CONTINUE;
            }

            public STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
                builder.accumulate(headers);
                return STATE.CONTINUE;
            }

            public APIResponse onCompleted() throws Exception {
                Response r = builder.build();
                return new APIResponse(r.getStatusCode(), r.getResponseBody());
            }
        };
    }

    /**
     * Get status of the API.
     *
     * @throws ExecutionException indicates an error in the HTTP backend
     * @throws InterruptedException indicates an interruption during the HTTP operation
     * @throws IOException indicates an error from the API response
     */
    public String getStatus() throws ExecutionException, InterruptedException, IOException {
        return (new FutureAPIResponse(client.prepareGet(apiUrl).execute(getHandler()))).get().getMessage();
    }

}
