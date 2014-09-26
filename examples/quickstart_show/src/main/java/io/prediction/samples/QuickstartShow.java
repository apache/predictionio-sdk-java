package io.prediction.samples;

import com.google.common.collect.ImmutableList;

import io.prediction.EngineClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QuickstartShow {
    public static void main(String[] args)
            throws ExecutionException, InterruptedException, IOException {
        EngineClient client = new EngineClient();

        // rank item 1 to 5 for each user
        Map<String, Object> query = new HashMap<>();
        query.put("iids", ImmutableList.of("1", "2", "3", "4", "5"));
        for (int user = 1; user <= 10; user++) {
            query.put("uid", user);
            System.out.println("Rank item 1 to 5 for user " + user);
            System.out.println(client.sendQuery(query));
        }

        client.close();
    }
}
