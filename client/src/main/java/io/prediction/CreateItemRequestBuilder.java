package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class to build Item requests
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.4.2
 * @since 0.2
 */

public class CreateItemRequestBuilder {
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String iid;
    private String[] itypes;
    private Double latitude;
    private Double longitude;
    private DateTime startT;
    private DateTime endT;
    private Map<String, String> attributes;

    /**
     * Instantiate a request builder with mandatory arguments.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @param apiUrl the API URL
     * @param apiFormat the return format of the API
     * @param appkey the new app key to be used
     * @param iid the item ID
     * @param itypes item types
     *
     * @see Client#getCreateItemRequestBuilder
     */
    public CreateItemRequestBuilder(String apiUrl, String apiFormat, String appkey, String iid, String[] itypes) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.iid = iid;
        this.itypes = itypes;
        this.attributes = new HashMap<String, String>();
    }

    /**
     * Add the "latitude" optional argument to the request.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param latitude latitude
     */
    public CreateItemRequestBuilder latitude(double latitude) {
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
    public CreateItemRequestBuilder longitude(double longitude) {
        this.longitude = new Double(longitude);
        return this;
    }

    /**
     * Add the "startT" optional argument to the request.
     *
     * @param startT the time when this item becomes valid
     */
    public CreateItemRequestBuilder startT(DateTime startT) {
        this.startT = startT;
        return this;
    }

    /**
     * Add the "endT" optional argument to the request.
     *
     * @param endT the time when this item becomes invalid
     */
    public CreateItemRequestBuilder endT(DateTime endT) {
        this.endT = endT;
        return this;
    }

    /**
     * Add optional custom item attributes argument to the request.
     * <p>
     * Notice that adding custom attributes with following names will be silently ignored as they collide with system attributes at the REST API:
     * <ul>
     * <li>appkey
     * <li>iid
     * <li>itypes
     * <li>latlng
     * <li>startT
     * <li>endT
     * </ul>
     *
     * @param name name of the custom item attribute
     * @param value value of the custom item attribute
     */
    public CreateItemRequestBuilder attribute(String name, String value) {
        if (!name.startsWith("pio_")) {
            this.attributes.put(name, value);
        }
        return this;
    }

    /**
     * Build a request.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @see Client#createItem(CreateItemRequestBuilder)
     * @see Client#createItemAsFuture(CreateItemRequestBuilder)
     */
    public Request build() {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(this.apiUrl + "/items." + this.apiFormat);
        builder.addQueryParameter("pio_appkey", this.appkey);
        builder.addQueryParameter("pio_iid", this.iid);
        builder.addQueryParameter("pio_itypes", Utils.arrayToString(this.itypes));
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("pio_latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.startT != null) {
            builder.addQueryParameter("pio_startT", startT.toString());
        }
        if (this.endT != null) {
            builder.addQueryParameter("pio_endT", endT.toString());
        }
        for (Map.Entry<String, String> attribute : this.attributes.entrySet()) {
            if (attribute.getValue() != null) {
                builder.addQueryParameter(attribute.getKey(), attribute.getValue());
            }
        }
        return builder.build();
    }
}
