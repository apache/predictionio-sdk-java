package io.prediction;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * DateTimeAdapter turns a String in ISO 8601 format into a DateTime object, and vice versa.
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.8.3
 * @since 0.8.0
 */
public class DateTimeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    @Override
    public JsonElement serialize(DateTime src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public DateTime deserialize(final JsonElement json, final Type type,
            final JsonDeserializationContext context) throws JsonParseException {
        return new DateTime(json.getAsJsonPrimitive().getAsString());
    }
}
