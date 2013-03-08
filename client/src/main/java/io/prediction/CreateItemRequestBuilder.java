package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import org.joda.time.DateTime;

/**
 * Class to build Item requests
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.3
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

    public CreateItemRequestBuilder(String apiUrl, String apiFormat, String appkey, String iid, String[] itypes) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.iid = iid;
        this.itypes = itypes;
    }

    public CreateItemRequestBuilder latitude(double latitude) {
        this.latitude = new Double(latitude);
        return this;
    }

    public CreateItemRequestBuilder longitude(double longitude) {
        this.longitude = new Double(longitude);
        return this;
    }

    public CreateItemRequestBuilder startT(DateTime startT) {
        this.startT = startT;
        return this;
    }

    public CreateItemRequestBuilder endT(DateTime endT) {
        this.endT = endT;
        return this;
    }

    public Request build() {
        RequestBuilder builder = new RequestBuilder("POST");
        builder.setUrl(this.apiUrl + "/items." + this.apiFormat);
        builder.addQueryParameter("appkey", this.appkey);
        builder.addQueryParameter("iid", this.iid);
        builder.addQueryParameter("itypes", Utils.itypesAsString(this.itypes));
        if (this.latitude != null && this.longitude != null) {
            builder.addQueryParameter("latlng", this.latitude.toString() + "," + this.longitude.toString());
        }
        if (this.startT != null) {
            builder.addQueryParameter("startT", startT.toString());
        }
        if (this.endT != null) {
            builder.addQueryParameter("endT", endT.toString());
        }
        return builder.build();
    }
}
