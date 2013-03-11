package io.prediction;

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
        /** Customize these */
        String appkey = null;
        String engine = null;
        try {
            appkey = args[0];
            engine = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("You must provide appkey (1st arg) and engine name (2nd arg)");
            System.exit(1);
        }

        Client client = new Client(appkey);
        try {
            // Get API system status
            System.out.println(client.getStatus().getMessage());

            // Try recommendations
            System.out.print("10 recommendations for User ID 1:");
            for (String iid : client.getRecommendations(engine, "1", 10)) {
                System.out.print(" " + iid);
            }
            System.out.println();

            // Try similar items
            System.out.print("5 recommendations for User ID 2:");
            for (String iid : client.getRecommendations(engine, "2", 5)) {
                System.out.print(" " + iid);
            }
            System.out.println();

            // Try creating dummy user
            System.out.print("Create dummy user with User ID 'foobar': ");
            if (client.createUser("foobar")) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }
            client.deleteUser("foobar");

            System.out.print("Create dummy user with User ID 'barbaz' with GPS location (21.109,-48.7479): ");
            CreateUserRequestBuilder builder = client.getCreateUserRequestBuilder("barbaz");
            builder.latitude(21.109).longitude(-48.7479);
            if (client.createUser(builder)) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            System.out.println("Get user just created:");
            User user = client.getUser("barbaz");
            if (user != null) {
                System.out.println("       uid: " + user.getUid());
                System.out.println("       lat: " + user.getLatitude());
                System.out.println("       lng: " + user.getLongitude());
                System.out.println("   created: " + user.getCreated());
            } else {
                System.out.println("  FAILED!");
            }
            client.deleteUser("barbaz");

            System.out.print("Create dummy user with User ID 'bla' and delete it: ");
            if (client.createUser("bla") && client.deleteUser("bla")) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
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
            if (client.createItem(ibuilder)) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            System.out.println("Get item just created:");
            Item item = client.getItem("barbaz");
            if (item != null) {
                System.out.println("       iid: " + item.getIid());
                System.out.println("    itypes: " + item.getItypes()[0] + ", " + item.getItypes()[1]);
                System.out.println("    startT: " + item.getStartT());
                System.out.println("      endT: " + item.getEndT());
                System.out.println("       lat: " + item.getLatitude());
                System.out.println("       lng: " + item.getLongitude());
                System.out.println("   created: " + item.getCreated());
            } else {
                System.out.println("  FAILED!");
            }
            client.deleteItem("barbaz");

            System.out.print("Create dummy item with Item ID 'xyz' and delete it: ");
            if (client.createItem("xyz", itypes) && client.deleteItem("xyz")) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }
        } catch (IOException e) {
            System.err.println("Caught an IOException! (" + e.getMessage() + ")");
        }
        System.exit(0);
    }
}
