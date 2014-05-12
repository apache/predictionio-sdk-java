package io.prediction.samples;

import io.prediction.Client;
import io.prediction.FutureAPIResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Sample data import client using MovieLens data set.
 *
 * @author Cong Qin, Donald Szeto
 */
public class SampleImport {
    public static void main(String[] args) {
    	/* set appurl to your API server */
        String appurl = "http://localhost:8000";
        /* Handle command line arguments */
        String appkey = null;
        String inputFile = null;
        try {
            appkey = args[0];
            inputFile = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("You must provide appkey (1st arg) and input file name (2nd arg)");
            System.exit(1);
        }

        Client client = null;
        Reader fileReader = null;

        /* Read input MovieLens data and send requests to API */
        try {
            /* Create a client with an app key */
            client = new Client(appkey, appurl);

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
            List<FutureAPIResponse> rs = new ArrayList<FutureAPIResponse>();

            while ((line = reader.readLine()) != null) {
                /* Break the line up */
                StringTokenizer st = new StringTokenizer(line);

                /* The 1st field is User ID, the 2nd field is Item ID, and the 3rd field is rating */
                String uid = st.nextToken();
                String iid = st.nextToken();
                int rate = Integer.parseInt(st.nextToken());

                /* Save User IDs and Item IDs for adding later */
                uids.add(uid);
                iids.add(iid);

                /* Send out async request */
                client.identify(uid);

                int j;
                for (j=0; j<5; j++) {
                    FutureAPIResponse r;

                    // create all types of actions for testing purpose
                    switch (j) {
                        case 0:
                            r = client.userActionItemAsFuture(client.getUserActionItemRequestBuilder("view", iid));
                            break;
                        case 1:
                            r = client.userActionItemAsFuture(client.getUserActionItemRequestBuilder("like", iid));
                            break;
                        case 2:
                            r = client.userActionItemAsFuture(client.getUserActionItemRequestBuilder("dislike", iid));
                            break;
                        case 3:
                            r = client.userActionItemAsFuture(client.getUserActionItemRequestBuilder("conversion", iid));
                            break;
                        default:
                            r = client.userActionItemAsFuture(client.getUserActionItemRequestBuilder("rate", iid).rate(rate));
                            break;
                    }
                    
                    /* Add async handler to array for synchronization later */
                    rs.add(r);
                    i++;

                    /* Print status per 2000 requests */
                    if (i % 2000 == 0) {
                        System.out.println("Sent "+i+" requests so far");
                    }

                }

            }

            /* Add User and Item IDs asynchronously */
            System.out.println("Sending "+uids.size()+" create User ID requests");
            for (String uid : uids) {
                rs.add(client.createUserAsFuture(client.getCreateUserRequestBuilder(uid)));
            }

            System.out.println("Sending "+iids.size()+" create Item ID requests");
            String[] itypes = {"movies"};
            for (String iid : iids) {
                rs.add(client.createItemAsFuture(client.getCreateItemRequestBuilder(iid, itypes).attribute("url", "http://localhost/" + iid + ".html").attribute("startT", "ignored")));
            }

            /* Synchronize all requests before the program exits */
            for (FutureAPIResponse r : rs) {
                if (r.getStatus() != 201) {
                    System.err.println(r.getMessage());
                }
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
}
