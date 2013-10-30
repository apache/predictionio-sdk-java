package io.prediction;

import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

/**
 * UserActionItem request builder
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.6.1
 * @since 0.2
 */

public class UserActionItemRequestBuilder {
    // Mandatory fields
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String action;
    private String uid;
    private String iid;
    private DateTime t;

    // Optional fields
    private Double latitude;
    private Double longitude;
    private int rate; // mandatory for u2i rate action

    /**
     * Action name for a user-rate-item action
     */
    public static final String RATE = "rate";

    /**
     * Action name for a user-like-item action
     */
    public static final String LIKE = "like";

    /**
     * Action name for a user-dislike-item action
     */
    public static final String DISLIKE = "dislike";

    /**
     * Action name for a user-view-item action
     */
    public static final String VIEW = "view";

    /**
     * Action name for a user-conversion-item action
     */
    public static final String CONVERSION = "conversion";

    /**
     * Instantiate a request builder with mandatory arguments.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @param apiUrl the API URL
     * @param apiFormat the return format of the API
     * @param appkey the new app key to be used
     * @param action the action code
     * @param uid the user ID
     * @param iid the item ID
     *
     * @see Client#getUserRateItemRequestBuilder
     * @see Client#getUserLikeItemRequestBuilder
     * @see Client#getUserDislikeItemRequestBuilder
     * @see Client#getUserViewItemRequestBuilder
     * @see Client#getUserConversionItemRequestBuilder
     */
    public UserActionItemRequestBuilder(String apiUrl, String apiFormat, String appkey, String action, String uid, String iid) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.action = action;
        this.uid = uid;
        this.iid = iid;
    }

    /**
     * Add the "latitude" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param latitude latitude
     */
    public UserActionItemRequestBuilder latitude(double latitude) {
        this.latitude = new Double(latitude);
        return this;
    }

    /**
     * Add the "longitude" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param longitude longitude
     */
    public UserActionItemRequestBuilder longitude(double longitude) {
        this.longitude = new Double(longitude);
        return this;
    }

    /**
     * Add the "rate" argument (mandatory for user-rate-item actions) to the request.
     *
     * @param rate user's rating on item
     */
    public UserActionItemRequestBuilder rate(int rate) {
        this.rate = rate;
        return this;
    }

    /**
     * Add the "t" optional argument to the request.
     *
     * @param t time of action
     */
    public UserActionItemRequestBuilder t(DateTime t) {
        this.t = t;
        return this;
    }

    /**
     * Build a request.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @see Client#userConversionItem(UserActionItemRequestBuilder)
     * @see Client#userLikeItem(UserActionItemRequestBuilder)
     * @see Client#userDislikeItem(UserActionItemRequestBuilder)
     * @see Client#userRateItem(UserActionItemRequestBuilder)
     * @see Client#userViewItem(UserActionItemRequestBuilder)
     */
    public Request build() {
        RequestBuilder builder = new RequestBuilder("POST");

        JsonObject requestJson = new JsonObject();

        requestJson.addProperty("pio_appkey", this.appkey);
        requestJson.addProperty("pio_uid", this.uid);
        requestJson.addProperty("pio_iid", this.iid);
        if (this.latitude != null && this.longitude != null) {
            requestJson.addProperty("pio_latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.t != null) {
            requestJson.addProperty("pio_t", t.toString());
        }

        String actionUrl = "/actions/u2i.";
        requestJson.addProperty("pio_action", this.action);
        if (this.action == RATE) {
            requestJson.addProperty("pio_rate", Integer.toString(this.rate));
        }

        builder.setUrl(this.apiUrl + actionUrl + this.apiFormat);

        String requestJsonString = requestJson.toString();

        builder.setBody(requestJsonString);
        builder.setHeader("Content-Type","application/json");
        builder.setHeader("Content-Length", ""+requestJsonString.length());

        return builder.build();
    }
}
