package io.prediction;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

/**
 * Get top n recommendations request builder for item recommendation engine
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.7.0
 * @since 0.7.0
 */

public class ItemRankGetRankedRequestBuilder {
    private String apiUrl;
    private String apiFormat;
    private String appkey;
    private String engine;
    private String uid;
    private String[] iids;
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
     * @see Client#getItemRankGetRankedRequestBuilder
     */
    public ItemRankGetRankedRequestBuilder(String apiUrl, String apiFormat, String appkey, String engine, String uid, String[] iids) {
        this.apiUrl = apiUrl;
        this.apiFormat = apiFormat;
        this.appkey = appkey;
        this.engine = engine;
        this.uid = uid;
        this.iids = iids;
    }

    /**
     * Add the "attributes" optional argument to the request.
     *
     * @param attributes array of item attribute names to be returned with the result
     */
    public ItemRankGetRankedRequestBuilder attributes(String[] attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * Build a request.
     * <p>
     * Do not use this directly. Please refer to "See Also".
     *
     * @see Client#getItemRankRanked(ItemRankGetRankedRequestBuilder)
     * @see Client#getItemRankRankedAsFuture(ItemRankGetRankedRequestBuilder)
     */
    public Request build() {
        RequestBuilder builder = new RequestBuilder("GET");
        builder.setUrl(this.apiUrl + "/engines/itemrank/" + this.engine + "/ranked." + this.apiFormat);
        builder.addQueryParameter("pio_appkey", this.appkey);
        builder.addQueryParameter("pio_uid", this.uid);
        builder.addQueryParameter("pio_iids", Utils.arrayToString(this.iids));
        if (this.attributes != null && this.attributes.length > 0) {
            builder.addQueryParameter("pio_attributes", Utils.arrayToString(this.attributes));
        }
        return builder.build();
    }
}
