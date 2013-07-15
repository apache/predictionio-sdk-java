package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;


/**
 * Class to build User requests
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.3
 * @since 0.2
 */

public class CreateUserRequestBuilder {
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String uid;
    private Double latitude;
    private Double longitude;

    /**
     * Instantiate a request builder with mandatory arguments.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @param apiUrl the API URL
     * @param apiFormat the return format of the API
     * @param appkey the new app key to be used
     * @param uid the user ID
     *
     * @see Client#getCreateUserRequestBuilder
     */
    public CreateUserRequestBuilder(String apiUrl, String apiFormat, String appkey, String uid) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.uid = uid;
    }

    /**
     * Add the "latitude" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param latitude latitude
     */
    public CreateUserRequestBuilder latitude(double latitude) {
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
    public CreateUserRequestBuilder longitude(double longitude) {
        this.longitude = new Double(longitude);
        return this;
    }

    /**
     * Build a request.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @see Client#createUser(CreateUserRequestBuilder)
     * @see Client#createUserAsFuture(CreateUserRequestBuilder)
     */
    public Request build() {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(this.apiUrl + "/users." + this.apiFormat);
        builder.addQueryParameter("pio_appkey", this.appkey);
        builder.addQueryParameter("pio_uid", this.uid);
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("pio_latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        return builder.build();
    }
}
