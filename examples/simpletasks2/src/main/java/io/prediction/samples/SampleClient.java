package io.prediction.samples;

import io.prediction.Client;
import io.prediction.CreateItemRequestBuilder;
import io.prediction.CreateUserRequestBuilder;
import io.prediction.Item;
import io.prediction.User;
import io.prediction.ItemSimGetTopNRequestBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cqin
 * Date: 2/10/13
 * Time: 11:11 PM
 */
public class SampleClient {
    public static void main(String[] args) {
    	/* set appurl to your API server */
        String appurl = "http://localhost:8000";
        /* Handle command line arguments */
        String appkey = null;
        String engine = null;
        try {
            appkey = args[0];
            engine = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("You must provide appkey (1st arg) and engine name (2nd arg)");
            System.exit(1);
        }

        Client client = new Client(appkey, appurl);
        // Get API system status
        try {
            System.out.println(client.getStatus());
        } catch (Exception e) {
            System.out.println("Unable to get status: "+e.getMessage());
        }

        // Get similar items (request more than available)
        System.out.print("10 similar items for Item ID 3:");
        try {
            for (String iid : client.getItemSimTopN(engine, "3", 10)) {
                System.out.print(" " + iid);
            }
        } catch (Exception e) {
            System.out.println("Unable to get similar items: "+e.getMessage());
        }
        System.out.println();

        // Get similar items (request less than available)
        System.out.print("1 similar item for Item ID 2:");
        try {
            for (String iid : client.getItemSimTopN(engine, "2", 1)) {
                System.out.print(" " + iid);
            }
        } catch (Exception e) {
            System.out.println("Unable to get similar items: "+e.getMessage());
        }
        System.out.println();

        // Get similar items using request builder
        System.out.print("10 similar items for Item ID 3:");
        try {
            ItemSimGetTopNRequestBuilder requestBuilder = client.getItemSimGetTopNRequestBuilder(engine, "3", 10);
            for (String iid : client.getItemSimTopN(requestBuilder)) {
                System.out.print(" " + iid);
            }
        } catch (Exception e) {
            System.out.println("Unable to get similar items: "+e.getMessage());
        }
        System.out.println();

        // Get similar items with attributes
        System.out.print("10 similar items for Item ID 3:");
        try {
            String[] attr = {"attr1", "attr2"};
            Map<String, String[]> data = client.getItemSimTopNWithAttributes(engine, "3", 10, attr);

            for (int i = 0; i < data.size(); i++) {
                System.out.print("[ " + data.get("pio_iids")[i]);
                System.out.print(", " + data.get("attr1")[i]);
                System.out.print(", " + data.get("attr2")[i] + " ]");
            }
        } catch (Exception e) {
            System.out.println("Unable to get similar items: "+e.getMessage());
        }
        System.out.println();

        // Get similar items with attributes using request builder 
        System.out.print("10 similar items for Item ID 3:");
        try {
            String[] attr = {"attr1", "attr2"};
            ItemSimGetTopNRequestBuilder requestBuilder = client.getItemSimGetTopNRequestBuilder(engine, "3", 10, attr);
            Map<String, String[]>  data = client.getItemSimTopNWithAttributes(requestBuilder);
            for (int i = 0; i < data.size(); i++) {
                System.out.print("[ " + data.get("pio_iids")[i]);
                System.out.print(", " + data.get("attr1")[i]);
                System.out.print(", " + data.get("attr2")[i] + " ]");
            }
        } catch (Exception e) {
            System.out.println("Unable to get similar items: "+e.getMessage());
        }
        System.out.println();



        client.close();
    }
}
