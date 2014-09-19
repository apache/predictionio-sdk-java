package io.prediction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * EventClient contains the generic methods createEvent() and getEvent() for importing and
 * accessing events, as well as helper methods such as setUser(), unsetItem() and userActionItem()
 * for integrating with built-in engines. Methods with an "AsFuture" suffix are asynchronous.
 * <p>
 * Multiple simultaneous asynchronous requests is made possible by the high performance backend
 * provided by the <a href="https://github.com/AsyncHttpClient/async-http-client">Async Http Client</a>.
 *
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.8.0
 * @since 0.8.0
 */
public class EventClient extends BaseClient {
    private static final String defaultEventUrl = "http://localhost:7070";

    private final int appId;

    /**
     * Instantiate a PredictionIO RESTful API Event Client using default values for API URL
     * and default values in {@link BaseClient}.
     * <p>
     * The default API URL is http://localhost:7070.
     *
     * @param appId the app ID that this client will use to communicate with the API
     */
    public EventClient(int appId) {
      this(appId, defaultEventUrl);
    }

    /**
     * Instantiate a PredictionIO RESTful API Event Client using default values in
     * {@link BaseClient}.
     *
     * @param appId the app ID that this client will use to communicate with the API
     * @param eventURL the URL of the PredictionIO API
     */
    public EventClient(int appId, String eventURL) {
        super(eventURL);
        this.appId = appId;
    }

    /**
     * Instantiate a PredictionIO RESTful API Event Client using default values in
     * {@link BaseClient} for parameters that are not specified.
     *
     * @param appId the app ID that this client will use to communicate with the API
     * @param eventURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     */
    public EventClient(int appId, String eventURL, int threadLimit) {
        super(eventURL, threadLimit);
        this.appId = appId;
    }

    /**
     * Instantiate a PredictionIO RESTful API Event Client using default values in
     * {@link BaseClient} for parameters that are not specified.
     *
     * @param appId the app ID that this client will use to communicate with the API
     * @param eventURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     * @param qSize size of the queue
     */
    public EventClient(int appId, String eventURL, int threadLimit, int qSize) {
        super(eventURL, threadLimit, qSize);
        this.appId = appId;
    }

    /**
     * Instantiate a PredictionIO RESTful API Event Client.
     *
     * @param appId the app ID that this client will use to communicate with the API
     * @param eventURL the URL of the PredictionIO API
     * @param threadLimit maximum number of simultaneous threads (connections) to the API
     * @param qSize size of the queue
     * @param timeout timeout in seconds for the connections
     */
    public EventClient(int appId, String eventURL, int threadLimit, int qSize, int timeout) {
        super(eventURL, threadLimit, qSize, timeout);
        this.appId = appId;
    }

    /**
     * Sends an asynchronous create event request to the API.
     *
     * @param event an instance of {@link Event} that will be turned into a request
     */
    public FutureAPIResponse createEventAsFuture(Event event) throws IOException {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(apiUrl + "/events.json");
        // use the appId associated with the client instead of the one specified in event
        String requestJsonString = event.appId(this.appId).toJsonString();
        builder.setBody(requestJsonString);
        builder.setHeader("Content-Type","application/json");
        builder.setHeader("Content-Length", ""+requestJsonString.length());
        return new FutureAPIResponse(client.executeRequest(builder.build(), getHandler()));
    }

    /**
     * Sends a synchronous create event request to the API.
     *
     * @param event an instance of {@link Event} that will be turned into a request
     * @return event ID from the server
     *
     * @throws ExecutionException indicates an error in the HTTP backend
     * @throws InterruptedException indicates an interruption during the HTTP operation
     * @throws IOException indicates an error from the API response
     */
    public String createEvent(Event event)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(createEventAsFuture(event));
    }

    /**
     * Synchronize a previously sent asynchronous create event request.
     *
     * @param response an instance of {@link FutureAPIResponse} returned from
     * {@link #createEventAsFuture}
     * @return event ID from the server
     *
     * @throws ExecutionException indicates an error in the HTTP backend
     * @throws InterruptedException indicates an interruption during the HTTP operation
     * @throws IOException indicates an error from the API response
     */
    public String createEvent(FutureAPIResponse response)
            throws ExecutionException, InterruptedException, IOException {
        int status = response.get().getStatus();
        String message = response.get().getMessage();

        if (status != BaseClient.HTTP_CREATED) {
            throw new IOException(status + " " + message);
        }
        return ((JsonObject) parser.parse(message)).get("eventId").getAsString();
    }

    /**
     * Sends an asynchronous get event request to the API.
     *
     * @param eid ID of the event to get
     */
    public FutureAPIResponse getEventAsFuture(String eid) throws IOException {
        Request request = (new RequestBuilder("GET")).setUrl(apiUrl + "/events/" + eid + ".json")
            .build();
        return new FutureAPIResponse(client.executeRequest(request, getHandler()));
    }

    /**
     * Sends a synchronous get event request to the API.
     *
     * @param eid ID of the event to get
     *
     * @throws ExecutionException indicates an error in the HTTP backend
     * @throws InterruptedException indicates an interruption during the HTTP operation
     * @throws IOException indicates an error from the API response
     */
    public Event getEvent(String eid)
            throws ExecutionException, InterruptedException, IOException {
        return getEvent(getEventAsFuture(eid));
    }

    /**
     * Synchronize a previously sent asynchronous get item request.
     *
     * @param response an instance of {@link FutureAPIResponse} returned from
     * {@link #getEventAsFuture}
     *
     * @throws ExecutionException indicates an error in the HTTP backend
     * @throws InterruptedException indicates an interruption during the HTTP operation
     * @throws IOException indicates an error from the API response
     */
    public Event getEvent(FutureAPIResponse response)
            throws ExecutionException, InterruptedException, IOException {
        int status = response.get().getStatus();
        String message = response.get().getMessage();

        if (status == BaseClient.HTTP_OK) {
            // handle DateTime separately
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
            Gson gson = gsonBuilder.create();

            return gson.fromJson(message, Event.class);
        } else {
            throw new IOException(message);
        }
    }

    //////////////////////////////////////////////////
    //
    //  helper methods for use with built-in engines
    //
    //////////////////////////////////////////////////

    /**
     * Sends a set user properties request. Implicitly creates the user if it's not already there.
     * Properties could be empty.
     *
     * @param uid ID of the user
     * @param properties a map of all the properties to be associated with the user, could be empty
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public FutureAPIResponse setUserAsFuture(String uid, Map<String, Object> properties,
            DateTime eventTime) throws IOException {
        return createEventAsFuture(new Event()
            .appId(appId)
            .event("$set")
            .entityType("pio_user")
            .entityId(uid)
            .eventTime(eventTime)
            .properties(properties));
    }

    /**
     * Sends a set user properties request. Same as
     * {@link #setUserAsFuture(String, Map, DateTime)
     * setUserAsFuture(String, Map&lt;String, Object&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public FutureAPIResponse setUserAsFuture(String uid, Map<String, Object> properties)
            throws IOException {
        return setUserAsFuture(uid, properties, new DateTime());
    }

    /**
     * Sets properties of a user. Implicitly creates the user if it's not already there.
     * Properties could be empty.
     *
     * @param uid ID of the user
     * @param properties a map of all the properties to be associated with the user, could be empty
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String setUser(String uid, Map<String, Object> properties, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(setUserAsFuture(uid, properties, eventTime));
    }

    /**
     * Sets properties of a user. Same as {@link #setUser(String, Map, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public String setUser(String uid, Map<String, Object> properties)
            throws ExecutionException, InterruptedException, IOException {
        return setUser(uid, properties, new DateTime());
    }

    /**
     * Sends an unset user properties request. The list must not be empty.
     *
     * @param uid ID of the user
     * @param properties a list of all the properties to unset
     * @param eventTime timestamp of the event
     */
    public FutureAPIResponse unsetUserAsFuture(String uid, List<String> properties,
            DateTime eventTime) throws IOException {
        if (properties.isEmpty()) {
            throw new IllegalStateException("property list cannot be empty");
        }
        // converts the list into a map (to empty string) before creating the event object
        Map<String, Object> propertiesMap = Maps.newHashMap();
        for (String property : properties) {
            propertiesMap.put(property, "");
        }
        return createEventAsFuture(new Event()
            .appId(appId)
            .event("$unset")
            .entityType("pio_user")
            .entityId(uid)
            .eventTime(eventTime)
            .properties(propertiesMap));
    }

    /**
     * Sends an unset user properties request. Same as
     * {@link #unsetUserAsFuture(String, List, DateTime)
     * unsetUserAsFuture(String, List&lt;String&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public FutureAPIResponse unsetUserAsFuture(String uid, List<String> properties)
            throws IOException {
        return unsetUserAsFuture(uid, properties, new DateTime());
    }

    /**
     * Unsets properties of a user. The list must not be empty.
     *
     * @param uid ID of the user
     * @param properties a list of all the properties to unset
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String unsetUser(String uid, List<String> properties, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(unsetUserAsFuture(uid, properties, eventTime));
    }

    /**
     * Unsets properties of a user. Same as {@link #unsetUser(String, List, DateTime)
     * unsetUser(String, List&lt;String&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public String unsetUser(String uid, List<String> properties)
            throws ExecutionException, InterruptedException, IOException {
        return unsetUser(uid, properties, new DateTime());
    }

    /**
     * Sends a delete user request.
     *
     * @param uid ID of the user
     * @param eventTime timestamp of the event
     */
    public FutureAPIResponse deleteUserAsFuture(String uid, DateTime eventTime)
            throws IOException {
        return createEventAsFuture(new Event()
            .appId(appId)
            .event("$delete")
            .entityType("pio_user")
            .entityId(uid)
            .eventTime(eventTime));
    }

    /**
     * Sends a delete user request. Event time is recorded as the time when the function is called.
     *
     * @param uid ID of the user
     */
    public FutureAPIResponse deleteUserAsFuture(String uid)
            throws IOException {
        return deleteUserAsFuture(uid, new DateTime());
    }

    /**
     * Deletes a user.
     *
     * @param uid ID of the user
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String deleteUser(String uid, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(deleteUserAsFuture(uid, eventTime));
    }

    /**
     * Deletes a user. Event time is recorded as the time when the function is called.
     *
     * @param uid ID of the user
     * @return ID of this event
     */
    public String deleteUser(String uid)
            throws ExecutionException, InterruptedException, IOException {
        return deleteUser(uid, new DateTime());
    }


    /**
     * Sends a set item properties request. Implicitly creates the item if it's not already there.
     * Properties could be empty.
     *
     * @param iid ID of the item
     * @param properties a map of all the properties to be associated with the item, could be empty
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public FutureAPIResponse setItemAsFuture(String iid, Map<String, Object> properties,
            DateTime eventTime) throws IOException {
        return createEventAsFuture(new Event()
            .appId(appId)
            .event("$set")
            .entityType("pio_item")
            .entityId(iid)
            .eventTime(eventTime)
            .properties(properties));
    }

    /**
     * Sends a set item properties request. Same as
     * {@link #setItemAsFuture(String, Map, DateTime)
     * setItemAsFuture(String, Map&lt;String, Object&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public FutureAPIResponse setItemAsFuture(String iid, Map<String, Object> properties)
            throws IOException {
        return setItemAsFuture(iid, properties, new DateTime());
    }

    /**
     * Sets properties of a item. Implicitly creates the item if it's not already there.
     * Properties could be empty.
     *
     * @param iid ID of the item
     * @param properties a map of all the properties to be associated with the item, could be empty
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String setItem(String iid, Map<String, Object> properties, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(setItemAsFuture(iid, properties, eventTime));
    }

    /**
     * Sets properties of a item. Same as {@link #setItem(String, Map, DateTime)
     * setItem(String, Map&lt;String, Object&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public String setItem(String iid, Map<String, Object> properties)
            throws ExecutionException, InterruptedException, IOException {
        return setItem(iid, properties, new DateTime());
    }

    /**
     * Sends an unset item properties request. The list must not be empty.
     *
     * @param iid ID of the item
     * @param properties a list of all the properties to unset
     * @param eventTime timestamp of the event
     */
    public FutureAPIResponse unsetItemAsFuture(String iid, List<String> properties,
            DateTime eventTime) throws IOException {
        if (properties.isEmpty()) {
            throw new IllegalStateException("property list cannot be empty");
        }
        // converts the list into a map (to empty string) before creating the event object
        Map<String, Object> propertiesMap = Maps.newHashMap();
        for (String property : properties) {
            propertiesMap.put(property, "");
        }
        return createEventAsFuture(new Event()
            .appId(appId)
            .event("$unset")
            .entityType("pio_item")
            .entityId(iid)
            .eventTime(eventTime)
            .properties(propertiesMap));
    }

    /**
     * Sends an unset item properties request. Same as
     * {@link #unsetItemAsFuture(String, List, DateTime)
     * unsetItemAsFuture(String, List&lt;String&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public FutureAPIResponse unsetItemAsFuture(String iid, List<String> properties)
            throws IOException {
        return unsetItemAsFuture(iid, properties, new DateTime());
    }

    /**
     * Unsets properties of a item. The list must not be empty.
     *
     * @param iid ID of the item
     * @param properties a list of all the properties to unset
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String unsetItem(String iid, List<String> properties, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(unsetItemAsFuture(iid, properties, eventTime));
    }

    /**
     * Unsets properties of a item. Same as {@link #unsetItem(String, List, DateTime)
     * unsetItem(String, List&lt;String&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public String unsetItem(String iid, List<String> properties)
            throws ExecutionException, InterruptedException, IOException {
        return unsetItem(iid, properties, new DateTime());
    }

    /**
     * Sends a delete item request.
     *
     * @param iid ID of the item
     * @param eventTime timestamp of the event
     */
    public FutureAPIResponse deleteItemAsFuture(String iid, DateTime eventTime)
            throws IOException {
        return createEventAsFuture(new Event()
            .appId(appId)
            .event("$delete")
            .entityType("pio_item")
            .entityId(iid)
            .eventTime(eventTime));
    }

    /**
     * Sends a delete item request. Event time is recorded as the time when the function is called.
     *
     * @param iid ID of the item
     */
    public FutureAPIResponse deleteItemAsFuture(String iid)
            throws IOException {
        return deleteItemAsFuture(iid, new DateTime());
    }

    /**
     * Deletes a item.
     *
     * @param iid ID of the item
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String deleteItem(String iid, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(deleteItemAsFuture(iid, eventTime));
    }

    /**
     * Deletes a item. Event time is recorded as the time when the function is called.
     *
     * @param iid ID of the item
     * @return ID of this event
     */
    public String deleteItem(String iid)
            throws ExecutionException, InterruptedException, IOException {
        return deleteItem(iid, new DateTime());
    }

    /**
     * Sends a user-action-on-item request.
     *
     * @param uid ID of the user
     * @param action name of the action performed
     * @param iid ID of the item
     * @param properties a map of properties associated with this action
     * @param eventTime timestamp of the event
     */
    public FutureAPIResponse userActionItemAsFuture(String uid, String action, String iid,
            Map<String, Object> properties, DateTime eventTime) throws IOException {
        return createEventAsFuture(new Event()
            .appId(appId)
            .event(action)
            .entityType("pio_user")
            .entityId(uid)
            .targetEntityType("pio_item")
            .targetEntityId(iid)
            .properties(properties)
            .eventTime(eventTime));
    }

    /**
     * Sends a user-action-on-item request. Similar to
     * {@link #userActionItemAsFuture(String, String, String, Map, DateTime)
     * #userActionItemAsFuture(String, String, String, Map&lt;String, Object\gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public FutureAPIResponse userActionItemAsFuture(String uid, String action, String iid,
            Map<String, Object> properties) throws IOException {
        return userActionItemAsFuture(uid, action, iid, properties, new DateTime());
    }

    /**
     * Records a user-action-on-item event.
     *
     * @param uid ID of the user
     * @param action name of the action performed
     * @param iid ID of the item
     * @param properties a map of properties associated with this action
     * @param eventTime timestamp of the event
     * @return ID of this event
     */
    public String userActionItem(String uid, String action, String iid,
            Map<String, Object> properties, DateTime eventTime)
            throws ExecutionException, InterruptedException, IOException {
        return createEvent(userActionItemAsFuture(uid, action, iid, properties, eventTime));
    }

    /**
     * Records a user-action-on-item event. Similar to
     * {@link #userActionItem(String, String, String, Map, DateTime)
     * userActionItem(String, String, String, Map&lt;String, Object&gt;, DateTime)}
     * except event time is not specified and recorded as the time when the function is called.
     */
    public String userActionItem(String uid, String action, String iid,
            Map<String, Object> properties)
            throws ExecutionException, InterruptedException, IOException {
        return userActionItem(uid, action, iid, properties, new DateTime());
    }

}
