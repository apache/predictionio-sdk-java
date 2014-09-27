package io.prediction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * EngineClient contains generic methods sendQuery() and sendQueryAsFuture()
 * for sending queries.
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.8.0
 * @since 0.8.0
 */
public class EngineClient extends BaseClient {
    private static final String defaultEngineUrl = "http://localhost:8000";

    /**
     * Instantiates a PredictionIO RESTful API Engine Client using default values for API URL
     * and default values in BaseClient.
     * <p>
     * The default API URL is http://localhost:8000.
     */
    public EngineClient() {
        super(defaultEngineUrl);
    }

    /**
     * Instantiates a PredictionIO RESTful API Engine Client using default values in BaseClient.
     *
     * @param engineURL the URL of the PredictionIO API
     */
    public EngineClient(String engineURL) {
        super(engineURL);
    }

    /**
     * Instantiates a PredictionIO RESTful API Engine Client using default values in BaseClient for
     * parameters that are not specified.
     *
     * @param engineURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     */
    public EngineClient(String engineURL, int threadLimit) {
        super(engineURL, threadLimit);
    }

    /**
     * Instantiates a PredictionIO RESTful API Engine Client using default values in BaseClient for
     * parameters that are not specified.
     *
     * @param engineURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     * @param qSize size of the queue
     */
    public EngineClient(String engineURL, int threadLimit, int qSize) {
        super(engineURL, threadLimit, qSize);
    }

    /**
     * Instantiates a PredictionIO RESTful API Engine Client.
     *
     * @param engineURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     * @param qSize size of the queue
     * @param timeout timeout in seconds for the connections
     */
    public EngineClient(String engineURL, int threadLimit, int qSize, int timeout) {
        super(engineURL, threadLimit, qSize, timeout);
    }

    /**
     * Sends a query asynchronously.
     */
    public FutureAPIResponse sendQueryAsFuture(Map<String, Object> query)
            throws ExecutionException, InterruptedException, IOException {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(apiUrl + "/queries.json");

        // handle DateTime separately
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
        Gson gson = gsonBuilder.create();

        String requestJsonString = gson.toJson(query);
        builder.setBody(requestJsonString);
        builder.setHeader("Content-Type","application/json");
        builder.setHeader("Content-Length", ""+requestJsonString.length());
        return new FutureAPIResponse(client.executeRequest(builder.build(), getHandler()));
    }

    /**
     * Sends a query synchronously.
     */
    public JsonObject sendQuery(Map<String, Object> query)
            throws ExecutionException, InterruptedException, IOException {
        return sendQuery(sendQueryAsFuture(query));
    }

    /**
     * Gets query result from a previously sent asynchronous request.
     */
    public JsonObject sendQuery(FutureAPIResponse response)
            throws ExecutionException, InterruptedException, IOException {
        int status = response.get().getStatus();
        String message = response.get().getMessage();

        if (status != BaseClient.HTTP_OK) {
            throw new IOException(status + " " + message);
        }
        return ((JsonObject) parser.parse(message));
    }

}
