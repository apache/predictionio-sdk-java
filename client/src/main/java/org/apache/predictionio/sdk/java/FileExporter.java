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
   * @param eventName Name of the event.
   * @param entityType The entity type.
   * @param entityId The entity ID.
   * @param targetEntityType The target entity type (optional).
   * @param targetEntityId The target entity ID (optional).
   * @param properties Properties (optional).
   * @param eventTime The time of the event (optional).
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
