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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ning.http.client.RequestBuilder;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.joda.time.DateTime;

/**
 * EngineClient contains generic methods sendQuery() and sendQueryAsFuture() for sending queries.
 *
 * @version 0.8.3
 * @since 0.8.0
 */
public class EngineClient extends BaseClient {

  private static final String defaultEngineUrl = "http://localhost:8000";

  /**
   * Instantiates a PredictionIO RESTful API Engine Client using default values for API URL and
   * default values in BaseClient.
   *
   * <p>The default API URL is http://localhost:8000.
   */
  public EngineClient() {
    super(defaultEngineUrl);
  }

  /**
   * Instantiates a PredictionIO RESTful API Engine Client using default values in BaseClient.
   *
   * @param engineUrl the URL of the PredictionIO API
   */
  public EngineClient(String engineUrl) {
    super(engineUrl);
  }

  /**
   * Instantiates a PredictionIO RESTful API Engine Client using default values in BaseClient for
   * parameters that are not specified.
   *
   * @param engineUrl the URL of the PredictionIO API
   * @param threadLimit maximum number of simultaneous threads (connections) to the API
   */
  public EngineClient(String engineUrl, int threadLimit) {
    super(engineUrl, threadLimit);
  }

  /**
   * Instantiates a PredictionIO RESTful API Engine Client using default values in BaseClient for
   * parameters that are not specified.
   *
   * @param engineUrl the URL of the PredictionIO API
   * @param threadLimit maximum number of simultaneous threads (connections) to the API
   * @param queueSize size of the queue
   */
  public EngineClient(String engineUrl, int threadLimit, int queueSize) {
    super(engineUrl, threadLimit, queueSize);
  }

  /**
   * Instantiates a PredictionIO RESTful API Engine Client.
   *
   * @param engineUrl the URL of the PredictionIO API
   * @param threadLimit maximum number of simultaneous threads (connections) to the API
   * @param queueSize size of the queue
   * @param timeout timeout in seconds for the connections
   */
  public EngineClient(String engineUrl, int threadLimit, int queueSize, int timeout) {
    super(engineUrl, threadLimit, queueSize, timeout);
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
    builder.setHeader("Content-Type", "application/json");
    builder.setHeader("Content-Length", "" + requestJsonString.length());
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

    if (status != HTTP_OK) {
      throw new IOException(status + " " + message);
    }
    return ((JsonObject) parser.parse(message));
  }

}
