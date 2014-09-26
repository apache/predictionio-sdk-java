package io.prediction.samples;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import io.prediction.APIResponse;
import io.prediction.EventClient;
import io.prediction.FutureAPIResponse;

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

/**
 * Sample data import client using MovieLens data set.
 *
 * @author Cong Qin, Donald Szeto, Tom Chan
 */
public class SampleImport {
    public static void main(String[] args) {
    	/* set appurl to your API server */
        String appurl = "http://localhost:7070";
        /* Handle command line arguments */
        int appId = -1;
        String inputFile = null;
        try {
            appId = Integer.parseInt(args[0]);
            inputFile = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("You must provide appId (1st arg) and input file name (2nd arg)");
            System.exit(1);
        }

        EventClient client = null;
        Reader fileReader = null;

        /* Read input MovieLens data and send requests to API */
        try {
            /* Create a client with an appId */
            client = new EventClient(appId, appurl);

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
            List<FutureAPIResponse> listOfFutures = new ArrayList<>(); // keeping track of requests
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
                    // in case of movielens data, pio_itypes could be used to store genres
                    List<String> itypes = new ArrayList<>();
                    itypes.add("movie");
                    itemProperties.put("pio_itypes", itypes);
                    future = client.setItemAsFuture(iid, itemProperties);
                    listOfFutures.add(future);
                    final String name = "item";
                    Futures.addCallback(future.getAPIResponse(), getFutureCallback("item " + iid));
                }

                /* User rates the movie. We do this asynchronously */
                Map<String, Object> properties = new HashMap<>(); // properties with rating
                properties.put("pio_rating", rate);
                future = client.userActionItemAsFuture("rate", uid, iid, properties);
                listOfFutures.add(future);
                Futures.addCallback(future.getAPIResponse(), getFutureCallback("event " + uid + " rates " + iid + " with " + rate));
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
