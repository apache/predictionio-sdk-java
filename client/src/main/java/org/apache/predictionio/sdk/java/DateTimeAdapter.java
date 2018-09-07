/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.predictionio.sdk.java;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.joda.time.DateTime;

/**
 * DateTimeAdapter turns a String in ISO 8601 format into a DateTime object, and vice versa.
 *
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
