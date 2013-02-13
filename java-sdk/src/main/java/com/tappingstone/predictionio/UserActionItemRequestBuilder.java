package com.tappingstone.predictionio;

import java.util.Date;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

/**
 * UserActionItem request builder
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 1.0
 * @since 1.0
 */

public class UserActionItemRequestBuilder {
    // Mandatory fields
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private int action;
    private String uid;
    private String iid;
    private Date t;

    // Optional fields
    private Double latitude;
    private Double longitude;
    private int rate; // mandatory for u2i rate action

    public static final int RATE = 0;
    public static final int LIKE = 1;
    public static final int DISLIKE = 2;
    public static final int VIEW = 3;
    public static final int VIEWDETAILS = 4;
    public static final int CONVERSION = 5;

    public UserActionItemRequestBuilder(String apiUrl, String apiFormat, String appkey, int action, String uid, String iid) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.action = action;
        this.uid = uid;
        this.iid = iid;
    }

    public UserActionItemRequestBuilder latitude(double latitude) {
        this.latitude = new Double(latitude);
        return this;
    }

    public UserActionItemRequestBuilder longitude(double longitude) {
        this.longitude = new Double(longitude);
        return this;
    }

    public UserActionItemRequestBuilder rate(int rate) {
        this.rate = rate;
        return this;
    }

    public UserActionItemRequestBuilder t(Date t) {
        this.t = t;
        return this;
    }

    public Request build() {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.addQueryParameter("appkey", this.appkey);
        builder.addQueryParameter("uid", this.uid);
        builder.addQueryParameter("iid", this.iid);
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.t != null) {
            builder.addQueryParameter("t", Long.toString(this.t.getTime()));
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
