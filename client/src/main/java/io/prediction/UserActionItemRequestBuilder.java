package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import org.joda.time.DateTime;

/**
 * UserActionItem request builder
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.2
 * @since 0.2
 */

public class UserActionItemRequestBuilder {
    // Mandatory fields
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private int action;
    private String uid;
    private String iid;
    private DateTime t;

    // Optional fields
    private Double latitude;
    private Double longitude;
    private int rate; // mandatory for u2i rate action

    /**
     * Action code for a user-rate-item action
     */
    public static final int RATE = 0;

    /**
     * Action code for a user-like-item action
     */
    public static final int LIKE = 1;

    /**
     * Action code for a user-dislike-item action
     */
    public static final int DISLIKE = 2;

    /**
     * Action code for a user-view-item action
     */
    public static final int VIEW = 3;

    /**
     * Action code for a user-view-details-item action
     *
     * @deprecated As of 0.2
     */
    public static final int VIEWDETAILS = 4;

    /**
     * Action code for a user-conversion-item action
     */
    public static final int CONVERSION = 5;

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
    public UserActionItemRequestBuilder(String apiUrl, String apiFormat, String appkey, int action, String uid, String iid) {
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
        builder.addQueryParameter("appkey", this.appkey);
        builder.addQueryParameter("uid", this.uid);
        builder.addQueryParameter("iid", this.iid);
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.t != null) {
            builder.addQueryParameter("t", t.toString());
        }

        String actionUrl = "";
        switch (this.action) {
            case RATE:
                actionUrl = "/actions/u2i/rate.";
                builder.addQueryParameter("rate", Integer.toString(this.rate));
                break;
            case LIKE:
                actionUrl = "/actions/u2i/like.";
                break;
            case DISLIKE:
                actionUrl = "/actions/u2i/dislike.";
                break;
            case VIEW:
                actionUrl = "/actions/u2i/view.";
                break;
            case VIEWDETAILS:
                actionUrl = "/actions/u2i/viewDetails.";
                break;
            case CONVERSION:
                actionUrl = "/actions/u2i/conversion.";
                break;
        }
        builder.setUrl(this.apiUrl + actionUrl + this.apiFormat);
        return builder.build();
    }
}
