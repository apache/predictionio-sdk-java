package com.tappingstone.predictionio.examples.client;

import com.tappingstone.predictionio.*;

import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: cqin
 * Date: 2/10/13
 * Time: 11:11 PM
 */
public class SampleClient {
    public static void main(String[] args) {
        PredictionIO predictionIO = new PredictionIO("145s436ni365033v1w4z4s6tz1i");
        try {
            // Get API system status
            System.out.println(predictionIO.getStatus().getMessage());

            // Try recommendations
            System.out.print("10 recommendations for uid 308:");
            for (String iid : predictionIO.getRecommendations("308", 10)) {
                System.out.print(" " + iid);
            }
            System.out.println();

            // Try similar items
            System.out.print("5 similar items for iid 196:");
            for (String iid : predictionIO.getRecommendations("196", 5)) {
                System.out.print(" " + iid);
            }
            System.out.println();

            // Try creating dummy user
            System.out.print("Create dummy user with uid foobar: ");
            if (predictionIO.createUser("foobar")) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            System.out.print("Create dummy female user with uid barbaz with latlng: ");
            CreateUserRequestBuilder builder = predictionIO.getCreateUserRequestBuilder("barbaz");
            builder.latitude(21.109).longitude(-48.7479).gender("F");
            if (predictionIO.createUser(builder)) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            System.out.println("Get user just created:");
            User user = predictionIO.getUser("barbaz");
            if (user != null) {
                System.out.println("       uid: " + user.getUid());
                System.out.println("    gender: " + user.getGender());
                System.out.println("       lat: " + user.getLatitude());
                System.out.println("       lng: " + user.getLongitude());
                System.out.println("   created: " + user.getCreated());
                System.out.println("  modified: " + user.getModified());
            } else {
                System.out.println("  FAILED!");
            }

            System.out.print("Create dummy user with uid bla and delete it: ");
            if (predictionIO.createUser("bla") && predictionIO.deleteUser("bla")) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            // Try creating dummy item
            System.out.print("Create dummy item with iid barbaz and itypes 5, 8: ");
            int[] itypes = new int[2];
            itypes[0] = 5;
            itypes[1] = 8;
            CreateItemRequestBuilder ibuilder = predictionIO.getCreateItemRequestBuilder("barbaz", itypes);
            long ts = 478308922;
            Date startT = new Date(ts);
            ibuilder.startT(startT).latitude(-58.24089).longitude(48.17890);
            if (predictionIO.createItem(ibuilder)) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            System.out.println("Get item just created:");
            Item item = predictionIO.getItem("barbaz");
            if (item != null) {
                System.out.println("       iid: " + item.getIid());
                System.out.println("    itypes: " + item.getItypes()[0] + ", " + item.getItypes()[1]);
                System.out.println("    startT: " + item.getStartT());
                System.out.println("      endT: " + item.getEndT());
                System.out.println("       lat: " + item.getLatitude());
                System.out.println("       lng: " + item.getLongitude());
                System.out.println("   created: " + item.getCreated());
                System.out.println("  modified: " + item.getModified());
            } else {
                System.out.println("  FAILED!");
            }

            System.out.print("Create dummy item with iid xyz and delete it: ");
            if (predictionIO.createItem("xyz", itypes) && predictionIO.deleteItem("xyz")) {
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
