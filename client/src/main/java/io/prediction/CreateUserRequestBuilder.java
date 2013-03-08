package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;


/**
 * Class to build User requests
 *
 * @author TappingStone (help@tappingstone.com)
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

    public CreateUserRequestBuilder(String apiUrl, String apiFormat, String appkey, String uid) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.uid = uid;
    }

    public CreateUserRequestBuilder latitude(double latitude) {
        this.latitude = new Double(latitude);
        return this;
    }

    public CreateUserRequestBuilder longitude(double longitude) {
        this.longitude = new Double(longitude);
        return this;
    }

    public Request build() {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(this.apiUrl + "/users." + this.apiFormat);
        builder.addQueryParameter("appkey", this.appkey);
        builder.addQueryParameter("uid", this.uid);
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        return builder.build();
    }
}
