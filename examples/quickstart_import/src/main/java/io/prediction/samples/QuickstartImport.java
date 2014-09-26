package io.prediction.samples;

import io.prediction.EventClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class QuickstartImport {
    public static void main(String[] args)
            throws ExecutionException, InterruptedException, IOException {
        EventClient client = new EventClient(10101);
        Random rand = new Random();

        // generate 10 users, with user ids 1 to 10
        for (int user = 1; user <= 10; user++) {
            System.out.println("Add user " + user);
            client.setUser(""+user, new HashMap<String, Object>());
        }

        // generate 50 items, with item ids 1 to 50
        // assign type id 1 to all of them
        Map<String, Object> itemProperty = new HashMap();
        List<String> types = new ArrayList();
        types.add("1");
        itemProperty.put("pio_itypes", types);
        for (int item = 1; item <= 50; item++) {
            System.out.println("Add item " + item);
            client.setItem(""+item, itemProperty);
        }

        // each user randomly views 10 items
        for (int user = 1; user <= 10; user++) {
            for (int i = 1; i <= 10; i++) {
                int item = rand.nextInt(50) + 1;
                System.out.println("User " + user + " views item " + item);
                client.userActionItem("view", ""+user, ""+item, new HashMap<String, Object>());
            }
        }

        client.close();
    }
}
