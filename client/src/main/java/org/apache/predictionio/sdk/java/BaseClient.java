/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.predictionio.sdk.java;

import com.google.gson.JsonParser;
import com.ning.http.client.*;
import com.ning.http.client.extra.ThrottleRequestFilter;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * BaseClient contains code common to both {@link EventClient} and {@link EngineClient}.
 *
 * @version 0.8.3
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
                .setAllowPoolingConnections(true)
                .setAllowPoolingSslConnections(true)
                .addRequestFilter(new ThrottleRequestFilter(threadLimit))
                .setMaxConnectionsPerHost(threadLimit)
                .setRequestTimeout(timeout * 1000)
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
