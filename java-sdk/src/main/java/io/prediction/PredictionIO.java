package io.prediction;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.ning.http.client.*;
import com.ning.http.client.extra.ThrottleRequestFilter;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Date;

/**
 * The PredictionIO Java class is a full feature abstraction on top of the RESTful PredictionIO web interface.
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.2
 * @since 0.2
 */
public class PredictionIO {
    private static Logger logger = Logger.getLogger(PredictionIO.class);
    // API base URL constant string
    private final String defaultApiUrl = "http://localhost:8000";
    private final String apiFormat = "json";
    // HTTP status code
    private final int HTTP_OK = 200;
    private final int HTTP_CREATED = 201;
    //API Url
    private String apiUrl;
    // Appkey
    private String appkey;
    // Async HTTP client
    private AsyncHttpClientConfig config;
    private AsyncHttpClient client;

    public PredictionIO(String appkey) {
        this.setAppkey(appkey);
        this.apiUrl = defaultApiUrl;

        // Async HTTP client config
        this.config = (new AsyncHttpClientConfig.Builder())
                .setAllowPoolingConnection(true)
                .setAllowSslConnectionPool(true)
                .addRequestFilter(new ThrottleRequestFilter(100))
                .setMaximumConnectionsPerHost(100)
                .setRequestTimeoutInMs(5000)
                .setIOThreadMultiplier(100)
                .build();
        this.client = new AsyncHttpClient(new NettyAsyncHttpProvider(config));
    }

    public PredictionIO(String appkey, String apiURL) {
        this.setAppkey(appkey);
        if (apiURL != null) {
            this.apiUrl = apiURL;
        } else {
            this.apiUrl = defaultApiUrl;
        }

        // Async HTTP client config
        this.config = (new AsyncHttpClientConfig.Builder())
                .setAllowPoolingConnection(true)
                .setAllowSslConnectionPool(true)
                .addRequestFilter(new ThrottleRequestFilter(100))
                .setMaximumConnectionsPerHost(100)
                .setRequestTimeoutInMs(5000)
                .setIOThreadMultiplier(100)
                .build();
        this.client = new AsyncHttpClient(new NettyAsyncHttpProvider(config));
    }

    public PredictionIO(String appkey, String apiURL, int threadLimit) {
        if (apiURL != null) {
            this.apiUrl = apiURL;
        } else {
            this.apiUrl = defaultApiUrl;
        }
        this.setAppkey(appkey);
        // Async HTTP client config
        this.config = (new AsyncHttpClientConfig.Builder())
                .setAllowPoolingConnection(true)
                .setAllowSslConnectionPool(true)
                .addRequestFilter(new ThrottleRequestFilter(threadLimit))
                .setMaximumConnectionsPerHost(100)
                .setRequestTimeoutInMs(5000)
                .setIOThreadMultiplier(threadLimit)
                .build();
        this.client = new AsyncHttpClient(new NettyAsyncHttpProvider(config), config);
    }

    private AsyncHandler<APIResponse> getHandler() {
        return new AsyncHandler<APIResponse>() {
            private final Response.ResponseBuilder builder = new Response.ResponseBuilder();

            public void onThrowable(Throwable t) {
            }

            public STATE onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
                builder.accumulate(content);
                return STATE.CONTINUE;
            }

            public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
                builder.accumulate(status);
                return STATE.CONTINUE;
            }

            public STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
                builder.accumulate(headers);
                return STATE.CONTINUE;
            }

            public APIResponse onCompleted() throws Exception {
                Response r = builder.build();
                return new APIResponse(r.getStatusCode(), r.getResponseBody());
            }
        };
    }

    private String[] jsonArrayAsStringArray(JsonArray a) throws ClassCastException {
        int l = a.size();
        String[] r = new String[l];
        for (int i = 0; i < l; i++) {
            r[i] = a.get(i).getAsString();
        }
        return r;
    }

    private int[] jsonArrayAsIntArray(JsonArray a) throws ClassCastException {
        int l = a.size();
        int[] r = new int[l];
        for (int i = 0; i < l; i++) {
            r[i] = a.get(i).getAsInt();
        }
        return r;
    }

    private double[] jsonArrayAsDoubleArray(JsonArray a) throws ClassCastException {
        int l = a.size();
        double[] r = new double[l];
        for (int i = 0; i < l; i++) {
            r[i] = a.get(i).getAsDouble();
        }
        return r;
    }

    public void setAppkey(String appkey) {
        // Set current API's appkey
        this.appkey = appkey;
    }

    public FutureAPIResponse getStatus() throws IOException {
        return new FutureAPIResponse(this.client.prepareGet(this.apiUrl + "/status").execute(this.getHandler()));
    }

    public CreateUserRequestBuilder getCreateUserRequestBuilder(String uid) {
        return new CreateUserRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, uid);
    }

    public FutureAPIResponse createUserAsFuture(CreateUserRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean createUser(String uid) throws IOException {
        return this.createUser(this.createUserAsFuture(this.getCreateUserRequestBuilder(uid)));
    }

    public boolean createUser(CreateUserRequestBuilder builder) throws IOException {
        return this.createUser(this.createUserAsFuture(builder));
    }

    public boolean createUser(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    public FutureAPIResponse getUserAsFuture(String uid) throws IOException {
        Request request = (new RequestBuilder("GET")).setUrl(this.apiUrl + "/users/" + uid + "." + this.apiFormat).addQueryParameter("appkey", this.appkey).build();
        return new FutureAPIResponse(this.client.executeRequest(request, this.getHandler()));
    }

    public User getUser(String uid) throws IOException {
        return this.getUser(this.getUserAsFuture(uid));
    }

    public User getUser(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_OK) {
            String message = response.getMessage();
            try {
                JsonParser parser = new JsonParser();
                JsonObject messageAsJson = (JsonObject) parser.parse(message);
                String uid = messageAsJson.get("uid").getAsString();
                User user = new User(uid);
                user.created(new DateTime(messageAsJson.get("ct").getAsLong()));
                //Check values
                try {
                    if (messageAsJson.get("mt") != null) {
                        user.modified(new DateTime(messageAsJson.get("mt").getAsLong()));
                    }
                } catch (ClassCastException cce) {
                    if (logger.isDebugEnabled()) {
                        logger.error("ClassCastException occurred trying to get mt");
                        logger.error(cce.getMessage());
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.error("Exception occurred trying to get mt");
                        logger.error(e.getMessage());
                    }
                }
                try {
                    if (messageAsJson.getAsJsonArray("latlng") != null) {
                        double latlng[] = this.jsonArrayAsDoubleArray(messageAsJson.getAsJsonArray("latlng"));
                        user.latitude(new Double(latlng[0]));
                        user.longitude(new Double(latlng[1]));
                    }
                } catch (ClassCastException cce) {
                    if (logger.isDebugEnabled()) {
                        logger.error("ClassCastException occurred trying to get latlng");
                        logger.error(cce.getMessage());
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.error("Exception occurred trying to get latlng");
                        logger.error(e.getMessage());
                    }
                }
                return user;
            } catch (JsonParseException e) {
                if (logger.isDebugEnabled()) {
                    logger.error("JsonParseException occurred");
                    logger.error(e.getMessage());
                }
                return null;
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.error("General Exception occurred in getUser");
                    logger.error(e.getMessage());
                }
                return null;
            }
        } else {
            return null;
        }
    }

    public FutureAPIResponse deleteUserAsFuture(String uid) throws IOException {
        RequestBuilder builder = new RequestBuilder("DELETE");
        builder.setUrl(this.apiUrl + "/users/" + uid + "." + this.apiFormat);
        builder.addQueryParameter("appkey", this.appkey);
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean deleteUser(String uid) throws IOException {
        return this.deleteUser(this.deleteUserAsFuture(uid));
    }

    public boolean deleteUser(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_OK) {
            return true;
        } else {
            return false;
        }
    }

    public CreateItemRequestBuilder getCreateItemRequestBuilder(String iid, int[] itypes) {
        return new CreateItemRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, iid, itypes);
    }

    public FutureAPIResponse createItemAsFuture(CreateItemRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean createItem(String iid, int[] itypes) throws IOException {
        return this.createItem(this.createItemAsFuture(this.getCreateItemRequestBuilder(iid, itypes)));
    }

    public boolean createItem(CreateItemRequestBuilder builder) throws IOException {
        return this.createItem(this.createItemAsFuture(builder));
    }

    public boolean createItem(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    public FutureAPIResponse getItemAsFuture(String iid) throws IOException {
        Request request = (new RequestBuilder("GET")).setUrl(this.apiUrl + "/items/" + iid + "." + this.apiFormat).addQueryParameter("appkey", this.appkey).build();
        return new FutureAPIResponse(this.client.executeRequest(request, this.getHandler()));
    }

    public Item getItem(String iid) throws IOException {
        return this.getItem(this.getItemAsFuture(iid));
    }

    public Item getItem(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_OK) {
            String message = response.getMessage();
            try {
                JsonParser parser = new JsonParser();
                JsonObject messageAsJson = (JsonObject) parser.parse(message);
                String iid = messageAsJson.get("iid").getAsString();
                int[] itypes = this.jsonArrayAsIntArray(messageAsJson.getAsJsonArray("itypes"));
                Item item = new Item(iid, itypes);
                item.created(new DateTime(messageAsJson.get("ct").getAsLong()));
                try {
                    if (messageAsJson.get("mt") != null) {
                        item.modified(new DateTime(messageAsJson.get("mt").getAsLong()));
                    }
                } catch (ClassCastException cce) {
                    if (logger.isDebugEnabled()) {
                        logger.error("ClassCastException occurred trying to get mt");
                        logger.error(cce.getMessage());
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.error("Exception occurred trying to get mt");
                        logger.error(e.getMessage());
                    }
                }
                try {
                    if (messageAsJson.get("startT") != null) {
                        item.startT(new Date(messageAsJson.get("startT").getAsLong()));
                    }
                } catch (ClassCastException cce) {
                    if (logger.isDebugEnabled()) {
                        logger.error("ClassCastException occurred trying to get startT");
                        logger.error(cce.getMessage());
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.error("Exception occurred trying to get startT");
                        logger.error(e.getMessage());
                    }
                }
                try {
                    if (messageAsJson.get("endT") != null) {
                        item.endT(new Date(messageAsJson.get("endT").getAsLong()));
                    }
                } catch (ClassCastException cce) {
                    if (logger.isDebugEnabled()) {
                        logger.error("ClassCastException occurred trying to get endT");
                        logger.error(cce.getMessage());
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.error("Exception occurred trying to get endT");
                        logger.error(e.getMessage());
                    }
                }
                try {
                    if (messageAsJson.getAsJsonArray("latlng") != null) {
                        double latlng[] = this.jsonArrayAsDoubleArray(messageAsJson.getAsJsonArray("latlng"));
                        item.latitude(new Double(latlng[0]));
                        item.longitude(new Double(latlng[1]));
                    }
                } catch (ClassCastException cce) {
                    if (logger.isDebugEnabled()) {
                        logger.error("ClassCastException occurred trying to get latlng");
                        logger.error(cce.getMessage());
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.error("Exception occurred trying to get latlng");
                        logger.error(e.getMessage());
                    }
                }
                return item;
            } catch (JsonParseException e) {
                if (logger.isDebugEnabled()) {
                    logger.error("JsonParseException occurred");
                    logger.error(e.getMessage());
                }
                return null;
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.error("General exception occurred in getItem");
                    logger.error(e.getMessage());
                }
                return null;
            }
        } else {
            return null;
        }
    }

    public FutureAPIResponse deleteItemAsFuture(String iid) throws IOException {
        RequestBuilder builder = new RequestBuilder("DELETE");
        builder.setUrl(this.apiUrl + "/items/" + iid + "." + this.apiFormat);
        builder.addQueryParameter("appkey", this.appkey);
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean deleteItem(String iid) throws IOException {
        return this.deleteItem(this.deleteItemAsFuture(iid));
    }

    public boolean deleteItem(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_OK) {
            return true;
        } else {
            return false;
        }
    }

    public RecommendationsRequestBuilder getRecommendationsRequestBuilder(String uid, int n) {
        return new RecommendationsRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, uid, n);
    }

    public FutureAPIResponse getRecommendationsAsFuture(RecommendationsRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public String[] getRecommendations(String uid, int n) throws IOException {
        return this.getRecommendations(this.getRecommendationsAsFuture(this.getRecommendationsRequestBuilder(uid, n)));
    }

    public String[] getRecommendations(RecommendationsRequestBuilder builder) throws IOException {
        return this.getRecommendations(this.getRecommendationsAsFuture(builder));
    }

    public String[] getRecommendations(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_OK) {
            String message = response.getMessage();
            try {
                //New implementation using gson
                JsonParser parser = new JsonParser();
                JsonObject messageAsJson = (JsonObject) parser.parse(message);
                JsonArray iidsAsJson = messageAsJson.getAsJsonArray("iids");
                return this.jsonArrayAsStringArray(iidsAsJson);
//                JSONObject messageAsJson = new JSONObject(message);
//                JSONArray iidsAsJson = messageAsJson.getJSONArray("iids");
//                return this.jsonArrayAsStringArray(iidsAsJson);
            } catch (JsonParseException e) {
                if (logger.isDebugEnabled()) {
                    logger.error("JsonParseException occurred in getRecommendations");
                    logger.error(e.getMessage());
                }
                return new String[0];
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.error("General Exception occurred trying to get getRecommendations");
                    logger.error(e.getMessage());
                }
                return new String[0];
            }
        } else {
            return new String[0];
        }
    }

    public SimilarRequestBuilder getSimilarRequestBuilder(String iid, int n) {
        return new SimilarRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, iid, n);
    }

    public FutureAPIResponse getSimilarAsFuture(SimilarRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public String[] getSimilar(String iid, int n) throws IOException {
        return this.getSimilar(this.getSimilarAsFuture(this.getSimilarRequestBuilder(iid, n)));
    }

    public String[] getSimilar(SimilarRequestBuilder builder) throws IOException {
        return this.getSimilar(this.getSimilarAsFuture(builder));
    }

    public String[] getSimilar(FutureAPIResponse response) {
        int status = response.getStatus();

        if (status == this.HTTP_OK) {
            String message = response.getMessage();
            try {
                //New implementation using gson
                JsonParser parser = new JsonParser();
                JsonObject messageAsJson = (JsonObject) parser.parse(message);
                JsonArray iidsAsJson = messageAsJson.getAsJsonArray("iids");
                return this.jsonArrayAsStringArray(iidsAsJson);
            } catch (JsonParseException e) {
                if (logger.isDebugEnabled()) {
                    logger.error("JsonParseException occurred trying to get getSimilar");
                    logger.error(e.getMessage());
                }
                return new String[0];
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.error("General Exception occurred trying to get getSimilar");
                    logger.error(e.getMessage());
                }
                return new String[0];
            }
        } else {
            return new String[0];
        }
    }

    public UserActionItemRequestBuilder getUserRateItemRequestBuilder(String uid, String iid, int rate) {
        UserActionItemRequestBuilder builder = new UserActionItemRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, UserActionItemRequestBuilder.RATE, uid, iid);
        builder.rate(rate);
        return builder;
    }

    public FutureAPIResponse userRateItemAsFuture(UserActionItemRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean userRateItem(String uid, String iid, int rate) throws IOException {
        return this.userRateItem(this.userRateItemAsFuture(this.getUserRateItemRequestBuilder(uid, iid, rate)));
    }

    public boolean userRateItem(UserActionItemRequestBuilder builder) throws IOException {
        return this.userRateItem(this.userRateItemAsFuture(builder));
    }

    public boolean userRateItem(FutureAPIResponse response) {
        if (response.getStatus() == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    public UserActionItemRequestBuilder getUserLikeItemRequestBuilder(String uid, String iid) {
        return new UserActionItemRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, UserActionItemRequestBuilder.LIKE, uid, iid);
    }

    public FutureAPIResponse userLikeItemAsFuture(UserActionItemRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean userLikeItem(String uid, String iid) throws IOException {
        return this.userLikeItem(this.userLikeItemAsFuture(this.getUserLikeItemRequestBuilder(uid, iid)));
    }

    public boolean userLikeItem(UserActionItemRequestBuilder builder) throws IOException {
        return this.userLikeItem(this.userLikeItemAsFuture(builder));
    }

    public boolean userLikeItem(FutureAPIResponse response) {
        if (response.getStatus() == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    public UserActionItemRequestBuilder getUserDislikeItemRequestBuilder(String uid, String iid) {
        return new UserActionItemRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, UserActionItemRequestBuilder.DISLIKE, uid, iid);
    }

    public FutureAPIResponse userDislikeItemAsFuture(UserActionItemRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean userDislikeItem(String uid, String iid) throws IOException {
        return this.userDislikeItem(this.userDislikeItemAsFuture(this.getUserDislikeItemRequestBuilder(uid, iid)));
    }

    public boolean userDislikeItem(UserActionItemRequestBuilder builder) throws IOException {
        return this.userDislikeItem(this.userDislikeItemAsFuture(builder));
    }

    public boolean userDislikeItem(FutureAPIResponse response) {
        if (response.getStatus() == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    public UserActionItemRequestBuilder getUserViewItemRequestBuilder(String uid, String iid) {
        return new UserActionItemRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, UserActionItemRequestBuilder.VIEW, uid, iid);
    }

    public FutureAPIResponse userViewItemAsFuture(UserActionItemRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean userViewItem(String uid, String iid) throws IOException {
        return this.userViewItem(this.userViewItemAsFuture(this.getUserViewItemRequestBuilder(uid, iid)));
    }

    public boolean userViewItem(UserActionItemRequestBuilder builder) throws IOException {
        return this.userViewItem(this.userViewItemAsFuture(builder));
    }

    public boolean userViewItem(FutureAPIResponse response) {
        if (response.getStatus() == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }

    public UserActionItemRequestBuilder getUserConversionItemRequestBuilder(String uid, String iid) {
        return new UserActionItemRequestBuilder(this.apiUrl, this.apiFormat, this.appkey, UserActionItemRequestBuilder.CONVERSION, uid, iid);
    }

    public FutureAPIResponse userConversionItemAsFuture(UserActionItemRequestBuilder builder) throws IOException {
        return new FutureAPIResponse(this.client.executeRequest(builder.build(), this.getHandler()));
    }

    public boolean userConversionItem(String uid, String iid) throws IOException {
        return this.userConversionItem(this.userConversionItemAsFuture(this.getUserConversionItemRequestBuilder(uid, iid)));
    }

    public boolean userConversionItem(UserActionItemRequestBuilder builder) throws IOException {
        return this.userConversionItem(this.userConversionItemAsFuture(builder));
    }

    public boolean userConversionItem(FutureAPIResponse response) {
        if (response.getStatus() == this.HTTP_CREATED) {
            return true;
        } else {
            return false;
        }
    }
}

