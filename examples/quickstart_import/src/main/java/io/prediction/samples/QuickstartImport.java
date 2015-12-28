package io.prediction.samples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import io.prediction.EventClient;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.LinkedList;
import org.joda.time.DateTime;

import io.prediction.Event;


public class QuickstartImport {
    public static void main(String[] args)
            throws ExecutionException, InterruptedException, IOException {
        String accessKey = null;
        try {
            accessKey = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("You must provide access key as the parameter");
            System.exit(1);
        }
        EventClient client = new EventClient(accessKey);
        Random rand = new Random();
        Map<String, Object> emptyProperty = ImmutableMap.of();

        // generate 10 users, with user ids 1 to 10
        for (int user = 1; user <= 10; user++) {
            System.out.println("Add user " + user);
            client.setUser(""+user, emptyProperty);
        }

        // generate 50 items, with item ids 1 to 50
        for (int item = 1; item <= 50; item++) {
            System.out.println("Add item " + item);
            client.setItem(""+item, emptyProperty);
        }

        // each user randomly views 10 items
        for (int user = 1; user <= 10; user++) {
            for (int i = 1; i <= 10; i++) {
                int item = rand.nextInt(50) + 1;
                System.out.println("User " + user + " views item " + item);
                client.userActionItem("view", ""+user, ""+item, emptyProperty);
            }
        }

        List<Event> events = new LinkedList<Event>();
     
        // Use only 5 users because max batch size is 50
        // Throws IOException w/ details inside if this is exceeded
        for (int user = 1; user <= 5; user++) {
            for (int i = 1; i <= 10; i++) {
                int item = rand.nextInt(50) + 1;
                System.out.println("User " + user + " views item " + item);
                events.add(new Event()
            .event("view")
            .entityType("user")
            .entityId(""+user)
            .targetEntityType("item")
            .targetEntityId(""+item)
            .properties(emptyProperty)
            .eventTime(new DateTime()));
            }
        }
    
        client.createEvents(events);

        client.close();
    }
}
