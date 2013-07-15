package io.prediction;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Item class for PredictionIO item objects
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.4.2
 * @since 0.2
 */

public class Item {
    private String iid;
    private String[] itypes;
    private Date startT;
    private Date endT;
    private Double latitude;
    private Double longitude;

    /**
     * Instantiate an item object with its ID and types.
     *
     * @param iid the item ID
     * @param itypes item types
     */
    public Item(String iid, String[] itypes) {
        this.iid = iid;
        this.itypes = itypes;
    }

    /**
     * Add the "startT" optional argument to the item.
     *
     * @param startT the time when this item becomes valid
     */
    public Item startT(Date startT) {
        this.startT = startT;
        return this;
    }

    /**
     * Add the "endT" optional argument to the item.
     *
     * @param endT the time when this item becomes invalid
     */
    public Item endT(Date endT) {
        this.endT = endT;
        return this;
    }

    /**
     * Add the "latitude" optional argument to the item.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param latitude latitude
     */
    public Item latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Add the "longitude" optional argument to the item.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param longitude longitude
     */
    public Item longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Get the ID of this item.
     */
    public String getIid() {
        return this.iid;
    }

    /**
     * Get item types of this item.
     */
    public String[] getItypes() {
        return this.itypes;
    }

    /**
     * Get the start time of validity of this item.
     */
    public Date getStartT() {
        return this.startT;
    }

    /**
     * Get the end time of validity of this item.
     */
    public Date getEndT() {
        return this.endT;
    }

    /**
     * Get the optional latitude attribute of this item.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * Get the optional longitude attribute of this item.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     */
    public Double getLongitude() {
        return this.longitude;
    }

}
