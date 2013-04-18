package io.prediction.samples;

import io.prediction.Client;
import io.prediction.FutureAPIResponse;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.StringTokenizer;

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

        /* Create a client with an app key */
        Client client = new Client(appkey);

        /* Data structure */
        Set uids = new TreeSet<String>();
        Set iids = new TreeSet<String>();

        /* Read input MovieLens data and send requests to API */
        try {
            /* Get API status */
            System.out.println(client.getStatus());

            /* Open data file for reading */
            FileInputStream fstream = new FileInputStream(inputFile);
            DataInputStream dstream = new DataInputStream(fstream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(dstream));

            /* Some local variables */
            String line;
            int i = 0;
            ArrayList<FutureAPIResponse> rs = new ArrayList<FutureAPIResponse>();

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
                FutureAPIResponse r = client.userRateItemAsFuture(client.getUserRateItemRequestBuilder(uid, iid, rate));

                /* Add async handler to array for synchronization later */
                rs.add(r);
                i++;

                /* Print status per 2000 requests */
                if (i % 2000 == 0) {
                    System.out.println("Sent "+i+" requests so far");
                }
            }

            dstream.close();

            /* Add User and Item IDs asynchronously */
            System.out.println("Sending "+uids.size()+" create User ID requests");
            for (Iterator uidIter = uids.iterator(); uidIter.hasNext();) {
                String uid = (String)uidIter.next();
                rs.add(client.createUserAsFuture(client.getCreateUserRequestBuilder(uid)));
            }

            System.out.println("Sending "+iids.size()+" create Item ID requests");
            String[] itypes = {"movies"};
            for (Iterator iidIter = iids.iterator(); iidIter.hasNext();) {
                String iid = (String)iidIter.next();
                rs.add(client.createItemAsFuture(client.getCreateItemRequestBuilder(iid, itypes).attribute("url", "http://localhost/"+iid+".html").attribute("startT", "ignored")));
            }

            /* Synchronize all requests before the program exits */
            for (FutureAPIResponse r : rs) {
                if (r.getStatus() != 201) {
                    System.err.println(r.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        client.close();
    }
}
