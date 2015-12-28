package io.prediction;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.joda.time.DateTime;

public class FileExporter {

    private FileOutputStream out;

    public FileExporter(String pathname) throws FileNotFoundException {
        out = new FileOutputStream(pathname);
    }

    /**
     * Create and write a json-encoded event to the underlying file.
     *
     * @param eventName        Name of the event.
     * @param entityType       The entity type.
     * @param entityId         The entity ID.
     * @param targetEntityType The target entity type (optional).
     * @param targetEntityId   The target entity ID (optional).
     * @param properties       Properties (optional).
     * @param eventTime        The time of the event (optional).
     * @throws IOException
     */
    public void createEvent(String eventName, String entityType, String entityId,
                            String targetEntityType, String targetEntityId, Map<String, Object> properties,
                            DateTime eventTime) throws IOException {

        if (eventTime == null) {
            eventTime = new DateTime();
        }

        Event event = new Event()
                .event(eventName)
                .entityType(entityType)
                .entityId(entityId)
                .eventTime(eventTime);

        if (targetEntityType != null) {
            event.targetEntityType(targetEntityType);
        }

        if (targetEntityId != null) {
            event.targetEntityId(targetEntityId);
        }

        if (properties != null) {
            event.properties(properties);
        }

        out.write(event.toJsonString().getBytes("UTF8"));
        out.write('\n');
    }

    public void close() throws IOException {
        out.close();
    }

}
