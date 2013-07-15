package io.prediction.samples;

import io.prediction.Client;
import io.prediction.CreateItemRequestBuilder;
import io.prediction.CreateUserRequestBuilder;
import io.prediction.Item;
import io.prediction.User;

import org.joda.time.DateTime;

import java.io.IOException;

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

        // Try recommendations
        System.out.print("10 recommendations for User ID 1:");
        try {
            for (String iid : client.getItemRecTopN(engine, "1", 10)) {
                System.out.print(" " + iid);
            }
        } catch (Exception e) {
            System.out.println("Unable to get recommendations: "+e.getMessage());
        }
        System.out.println();

        // Try similar items
        System.out.print("5 recommendations for User ID 2:");
        try {
            for (String iid : client.getItemRecTopN(engine, "2", 5)) {
                System.out.print(" " + iid);
            }
        } catch (Exception e) {
            System.out.println("Unable to get recommendations: "+e.getMessage());
        }
        System.out.println();

        // Try creating dummy user
        System.out.print("Create dummy user with User ID 'foobar': ");
        try {
            client.createUser("foobar");
            System.out.println("succeeded");
        } catch (Exception e) {
            System.out.println("failed ("+e.getMessage()+")");
        }
        try {
            client.deleteUser("foobar");
        } catch (Exception e) {
            System.out.println("clean up failed: "+e.getMessage());
        }

        System.out.print("Create dummy user with User ID 'barbaz' with GPS location (21.109,-48.7479): ");
        CreateUserRequestBuilder builder = client.getCreateUserRequestBuilder("barbaz");
        builder.latitude(21.109).longitude(-48.7479);
        try {
            client.createUser(builder);
            System.out.println("succeeded");
        } catch (Exception e) {
            System.out.println("failed ("+e.getMessage()+")");
        }

        System.out.println("Get user just created:");
        try {
            User user = client.getUser("barbaz");
            System.out.println("       uid: " + user.getUid());
            System.out.println("       lat: " + user.getLatitude());
            System.out.println("       lng: " + user.getLongitude());
        } catch (Exception e) {
            System.out.println("  FAILED! ("+e.getMessage()+")");
        }
        try {
            client.deleteUser("barbaz");
        } catch (Exception e) {
            System.out.println("clean up failed: "+e.getMessage());
        }

        System.out.print("Create dummy user with User ID 'bla' and delete it: ");
        try {
            client.createUser("bla");
            client.deleteUser("bla");
            System.out.println("succeeded");
        } catch (Exception e) {
            System.out.println("failed ("+e.getMessage()+")");
        }

        // Try creating dummy item
        System.out.print("Create dummy item with Item ID 'barbaz' and item types 'dead', 'beef': ");
        String[] itypes = new String[2];
        itypes[0] = "dead";
        itypes[1] = "beef";
        CreateItemRequestBuilder ibuilder = client.getCreateItemRequestBuilder("barbaz", itypes);
        long ts = 478308922;
        DateTime startT = new DateTime(ts);
        ibuilder.startT(startT).latitude(-58.24089).longitude(48.17890);
        try {
            client.createItem(ibuilder);
            System.out.println("succeeded");
        } catch (Exception e) {
            System.out.println("failed ("+e.getMessage()+")");
        }

        System.out.println("Get item just created:");
        try {
            Item item = client.getItem("barbaz");
            System.out.println("       iid: " + item.getIid());
            System.out.println("    itypes: " + item.getItypes()[0] + ", " + item.getItypes()[1]);
            System.out.println("    startT: " + item.getStartT());
            System.out.println("      endT: " + item.getEndT());
            System.out.println("       lat: " + item.getLatitude());
            System.out.println("       lng: " + item.getLongitude());
            client.deleteItem("barbaz");
        } catch (Exception e) {
            System.out.println("  FAILED! ("+e.getMessage()+")");
        }

        System.out.print("Create dummy item with Item ID 'xyz' and delete it: ");
        try {
            client.createItem("xyz", itypes);
            client.deleteItem("xyz");
            System.out.println("succeeded");
        } catch (Exception e) {
            System.out.println("failed ("+e.getMessage()+")");
        }
        //System.exit(0);
        client.close();
    }
}
