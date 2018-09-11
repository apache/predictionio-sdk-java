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

package org.apache.predictionio.samples;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import org.apache.predictionio.APIResponse;
import org.apache.predictionio.EventClient;
import org.apache.predictionio.FutureAPIResponse;

/**
 * Sample data import client using MovieLens data set.
 *
 * @author Cong Qin, Donald Szeto, Tom Chan
 */
public class SampleImport {

  private static final int HTTP_CREATED = 201;

  public static void main(String[] args) {
    /* set appurl to your API server */
    String appurl = "http://localhost:7070";
    /* Handle command line arguments */
    String accessKey = null;
    String inputFile = null;
    try {
      accessKey = args[0];
      inputFile = args[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println("You must provide access key (1st arg) and input file name (2nd arg)");
      System.exit(1);
    }

    EventClient client = null;
    Reader fileReader = null;

    /* Read input MovieLens data and send requests to API */
    List<FutureAPIResponse> listOfFutures = new ArrayList<>(); // keeping track of requests
    try {
      /* Create a client with the access key */
      client = new EventClient(accessKey, appurl);

      /* Data structure */
      Set<String> uids = new TreeSet<String>();
      Set<String> iids = new TreeSet<String>();

      /* Get API status */
      System.out.println(client.getStatus());

      /* Open data file for reading */
      fileReader = new FileReader(inputFile);
      BufferedReader reader = new BufferedReader(fileReader);

      /* Some local variables */
      String line;
      int i = 0;
      FutureAPIResponse future;
      Map<String, Object> userProperties = new HashMap<>(); // empty properties for user

      while ((line = reader.readLine()) != null) {
        /* Break the line up */
        StringTokenizer st = new StringTokenizer(line);

        /* The 1st field is User ID, the 2nd field is Item ID, and the 3rd field is rating */
        String uid = st.nextToken();
        String iid = st.nextToken();
        int rate = Integer.parseInt(st.nextToken());

        /* Add user and item if they are not seen before */
        if (uids.add(uid)) {
          // event time is omitted since we're not using it
          future = client.setUserAsFuture(uid, userProperties);
          listOfFutures.add(future);
          Futures.addCallback(future.getAPIResponse(), getFutureCallback("user " + uid));
        }
        if (iids.add(iid)) {
          Map<String, Object> itemProperties = new HashMap<>();
          List<String> genre = new ArrayList<>();
          genre.add("comedy");
          itemProperties.put("genre", genre);
          future = client.setItemAsFuture(iid, itemProperties);
          listOfFutures.add(future);
          Futures.addCallback(future.getAPIResponse(), getFutureCallback("item " + iid));
        }

        /* User rates the movie. We do this asynchronously */
        Map<String, Object> properties = new HashMap<>(); // properties with rating
        properties.put("rating", rate);
        future = client.userActionItemAsFuture("rate", uid, iid, properties);
        listOfFutures.add(future);
        Futures.addCallback(future.getAPIResponse(),
            getFutureCallback("event " + uid + " rates " + iid + " with " + rate));
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (fileReader != null) {
        try {
          fileReader.close();
        } catch (IOException e) {
          System.err.println("Error: " + e.getMessage());
        }
      }
      // wait for the import result
      ListenableFuture<List<APIResponse>> futures = Futures.allAsList(listOfFutures);
      try {
        List<APIResponse> responses = futures.get();
        for (APIResponse response : responses) {
          if (response.getStatus() != HTTP_CREATED) {
            System.err.println("Error importing some record, first error message is: "
                + response.getMessage());
            // only print the first error
            break;
          }
        }
      } catch (InterruptedException | ExecutionException e) {
        System.err.println("Error importing some record, error message: " + e.getStackTrace());
      }
      if (client != null) {
        client.close();
      }
    }
  }

  private static FutureCallback<APIResponse> getFutureCallback(final String name) {
    return new FutureCallback<APIResponse>() {
      public void onSuccess(APIResponse response) {
        System.out.println(name + " added: " + response.getMessage());
      }

      public void onFailure(Throwable thrown) {
        System.out.println("failed to add " + name + ": " + thrown.getMessage());
      }
    };
  }
}
