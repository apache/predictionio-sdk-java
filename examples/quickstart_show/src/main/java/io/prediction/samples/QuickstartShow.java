package io.prediction.samples;

import io.prediction.EngineClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QuickstartShow {
    public static void main(String[] args)
            throws ExecutionException, InterruptedException, IOException {
        EngineClient client = new EngineClient();

        // rank item 1 to 5 for each user
        List<String> itemIds = new ArrayList();
        for (int item = 1; item <= 5; item++) {
            itemIds.add(""+item);
        }

        Map<String, Object> query = new HashMap();
        query.put("iids", itemIds);
        for (int user = 1; user <= 10; user++) {
            query.put("uid", user);
            System.out.println("Rank item 1 to 5 for user " + user);
            System.out.println(client.sendQuery(query));
        }

        client.close();
    }
}
