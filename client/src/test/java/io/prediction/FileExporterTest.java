package io.prediction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileExporterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testIt() throws IOException {

        String pathname = folder.getRoot().getCanonicalPath() + "/testIt.out";

        FileExporter exporter = new FileExporter(pathname);

        Map<String, Object> properties = new HashMap<>();
        properties.put("birthday", new DateTime("1758-05-06T00:00:00+00:00"));

        DateTime then = new DateTime("1794-07-27T00:00:00+00:00");

        exporter.createEvent("event-1", "entity-type-1", "entity-id-1",
                null, null, null, null);

        exporter.createEvent("event-2", "entity-type-2", "entity-id-2",
                "target-entity-type-2", null, null, null);

        exporter.createEvent("event-3", "entity-type-3", "entity-id-3",
                null, "target-entity-id-3", null, null);

        exporter.createEvent("event-4", "entity-type-4", "entity-id-4",
                null, null, properties, then);

        exporter.createEvent("event-5", "entity-type-5", "entity-id-5",
                "target-entity-type-5", "target-entity-id-5", properties, then);

        exporter.close();

        File out = new File(pathname);
        assertTrue(pathname + " exists", out.exists());

        BufferedReader reader = new BufferedReader(new FileReader(pathname));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
        Gson gson = gsonBuilder.create();

        String json1 = reader.readLine();
        Event event1 = gson.fromJson(json1, Event.class);
        assertEquals("event-1", event1.getEvent());
        assertEquals("entity-type-1", event1.getEntityType());
        assertEquals("entity-id-1", event1.getEntityId());
        assertNull(event1.getTargetEntityType());
        assertNull(event1.getTargetEntityId());
        assertTrue(event1.getProperties().isEmpty());
        assertEquals(new DateTime().getMillis(), event1.getEventTime().getMillis(), 1000);

        String json2 = reader.readLine();
        Event event2 = gson.fromJson(json2, Event.class);
        assertEquals("event-2", event2.getEvent());
        assertEquals("entity-type-2", event2.getEntityType());
        assertEquals("entity-id-2", event2.getEntityId());
        assertEquals("target-entity-type-2", event2.getTargetEntityType());
        assertNull(event2.getTargetEntityId());
        assertTrue(event2.getProperties().isEmpty());
        assertEquals(new DateTime().getMillis(), event2.getEventTime().getMillis(), 1000);

        String json3 = reader.readLine();
        Event event3 = gson.fromJson(json3, Event.class);
        assertEquals("event-3", event3.getEvent());
        assertEquals("entity-type-3", event3.getEntityType());
        assertEquals("entity-id-3", event3.getEntityId());
        assertNull(event3.getTargetEntityType());
        assertEquals("target-entity-id-3", event3.getTargetEntityId());
        assertTrue(event3.getProperties().isEmpty());
        assertEquals(new DateTime().getMillis(), event3.getEventTime().getMillis(), 1000);

        String json4 = reader.readLine();
        Event event4 = gson.fromJson(json4, Event.class);
        assertEquals("event-4", event4.getEvent());
        assertEquals("entity-type-4", event4.getEntityType());
        assertEquals("entity-id-4", event4.getEntityId());
        assertNull(event4.getTargetEntityType());
        assertNull(event4.getTargetEntityId());
        assertEquals(1, event4.getProperties().size());
        assertEquals(properties.get("birthday"), new DateTime(event4.getProperties().get("birthday")));
        assertEquals(then.getMillis(), event4.getEventTime().getMillis());

        String json5 = reader.readLine();
        Event event5 = gson.fromJson(json5, Event.class);
        assertEquals("event-5", event5.getEvent());
        assertEquals("entity-type-5", event5.getEntityType());
        assertEquals("entity-id-5", event5.getEntityId());
        assertEquals("target-entity-type-5", event5.getTargetEntityType());
        assertEquals("target-entity-id-5", event5.getTargetEntityId());
        assertEquals(1, event5.getProperties().size());
        assertEquals(properties.get("birthday"), new DateTime(event5.getProperties().get("birthday")));
        assertEquals(then.getMillis(), event4.getEventTime().getMillis());

        String empty = reader.readLine();
        assertNull("no more data", empty);
    }
}
