package com.tappingstone.predictionio;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

import java.util.Date;

/**
 * Class to build User requests
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 1.0
 * @since 1.0
 */

public class CreateUserRequestBuilder {
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String uid;
    private String gender;
    private Date bday;
    private Double latitude;
    private Double longitude;

    public CreateUserRequestBuilder(String apiUrl, String apiFormat, String appkey, String uid) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.uid = uid;
    }

    public CreateUserRequestBuilder gender(String gender) {
        this.gender = gender;
        return this;
    }

    public CreateUserRequestBuilder bday(Date bday) {
        this.bday = bday;
        return this;
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
        if (this.gender != null) {
            builder.addQueryParameter("gender", this.gender);
        }
        if (this.bday != null) {
            builder.addQueryParameter("bday", Long.toString(this.bday.getTime()));
        }
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        return builder.build();
    }
}
