package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

/**
 * Get top n recommendations request builder for item recommendation engine
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.4.1
 * @since 0.4
 */

public class ItemRecGetTopNRequestBuilder {
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String engine;
    private String uid;
    private int n;
    private String[] itypes;
    private Double latitude;
    private Double longitude;
    private Double within;
    private String unit;
    private String[] attributes;

    /**
     * Instantiate a request builder with mandatory arguments.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @param apiUrl the API URL
     * @param apiFormat the return format of the API
     * @param appkey the new app key to be used
     * @param engine engine name
     * @param uid UID
     * @param n number of recommendations to return
     *
     * @see Client#getItemRecGetTopNRequestBuilder
     */
    public ItemRecGetTopNRequestBuilder(String apiUrl, String apiFormat, String appkey, String engine, String uid, int n) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.engine = engine;
        this.uid = uid;
        this.n = n;
    }

    /**
     * Add the "itypes" optional argument to the request.
     *
     * @param itypes array of item types
     */
    public ItemRecGetTopNRequestBuilder itypes(String[] itypes) {
        this.itypes = itypes;
        return this;
    }

    /**
     * Add the "latitude" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param latitude latitude
     */
    public ItemRecGetTopNRequestBuilder latitude(Double latitude) {
        this.latitude = latitude;
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
    public ItemRecGetTopNRequestBuilder longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Add the "within" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param within radius of search from the specified location
     */
    public ItemRecGetTopNRequestBuilder within(Double within) {
        this.within = within;
        return this;
    }

    /**
     * Add the "unit" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param unit unit of "within" (radius)
     */
    public ItemRecGetTopNRequestBuilder unit(String unit) {
        this.unit = unit;
        return this;
    }

    /**
     * Add the "attributes" optional argument to the request.
     *
     * @param attributes array of item attribute names to be returned with the result
     */
    public ItemRecGetTopNRequestBuilder attributes(String[] attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * Build a request.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @see Client#getItemRecTopN(ItemRecGetTopNRequestBuilder)
     * @see Client#getItemRecTopNAsFuture(ItemRecGetTopNRequestBuilder)
     */
    public Request build() {
        RequestBuilder builder = new RequestBuilder("GET");
        builder.setUrl(this.apiUrl + "/engines/itemrec/" + this.engine + "/topn." + this.apiFormat);
        builder.addQueryParameter("pio_appkey", this.appkey);
        builder.addQueryParameter("pio_uid", this.uid);
        builder.addQueryParameter("pio_n", Integer.toString(this.n));
        if (this.itypes != null && this.itypes.length > 0) {
            builder.addQueryParameter("pio_itypes", Utils.arrayToString(this.itypes));
        }
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("pio_latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.within != null) {
            builder.addQueryParameter("pio_within", this.within.toString());
        }
        if (this.unit != null) {
            builder.addQueryParameter("pio_unit", this.unit.toString());
        }
        if (this.attributes != null && this.attributes.length > 0) {
            builder.addQueryParameter("pio_attributes", Utils.arrayToString(this.attributes));
        }
        return builder.build();
    }
}
