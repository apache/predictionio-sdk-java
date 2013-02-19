package io.prediction;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: cqin
 * Date: 2/18/13
 * Time: 11:07 PM
 */
public class SampleImport {
    public static void main(String[] args) {
        PredictionIO predictionIO = new PredictionIO("6ztw3ty3P1Pgqj4cOPXdTUlSR6ZAYQNhNdYAfWb77LfnfrbBZDex58hcb3e4ehIt");
        try {
            FileInputStream fstream = new FileInputStream(args[0]);
            DataInputStream dstream = new DataInputStream(fstream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(dstream));
            String line;
            String uid;
            String iid;
            int itype = 0;
            int rate;
            DateTime t;
            int i = 0;
            ArrayList<FutureAPIResponse> rs = new ArrayList<FutureAPIResponse>();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                StringTokenizer st = new StringTokenizer(line);
                uid = st.nextToken();
                iid = st.nextToken();
                rate = Math.round((Float.parseFloat(st.nextToken()) - 1) / 4 * 10);
                t = new DateTime();
                FutureAPIResponse r = predictionIO.userRateItemAsFuture(predictionIO.getUserRateItemRequestBuilder(uid, iid, rate).t(t));
                rs.add(r);
                i++;
                Thread.sleep(250);
            }
            while (rs.size() < i) {
                Thread.sleep(1000);
            }
            for (FutureAPIResponse r : rs) {
                r.getMessage();
            }
            dstream.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.exit(0);
    }
}
