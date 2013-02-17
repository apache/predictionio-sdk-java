package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

/**
 * Class to build Similar Requests
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.2
 * @since 0.2
 */

public class SimilarRequestBuilder {
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String iid;
    private int n;
    private Double latitude;
    private Double longitude;
    private Double within;
    private String unit;

    public SimilarRequestBuilder(String apiUrl, String apiFormat, String appkey, String iid, int n) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.iid = iid;
        this.n = n;
    }

    public SimilarRequestBuilder latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public SimilarRequestBuilder longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public SimilarRequestBuilder within(Double within) {
        this.within = within;
        return this;
    }

    public SimilarRequestBuilder unit(String unit) {
        this.unit = unit;
        return this;
    }

    public Request build() {
        RequestBuilder builder = new RequestBuilder("GET");
        builder.setUrl(this.apiUrl + "/items/" + this.iid + "/similar." + this.apiFormat);
        builder.addQueryParameter("appkey", this.appkey);
        builder.addQueryParameter("n", Integer.toString(this.n));
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.within != null) {
            builder.addQueryParameter("within", this.within.toString());
        }
        if (this.unit != null) {
            builder.addQueryParameter("unit", this.unit.toString());
        }
        return builder.build();
    }
}