package io.prediction;

import org.joda.time.DateTime;

/**
 * User class for PredictionIO User objects
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.4.2
 * @since 0.2
 */

public class User {
    private String uid;
    private Double latitude;
    private Double longitude;

    /**
     * Instantiate a user object with its ID.
     *
     * @param uid the user ID
     */
    public User(String uid) {
        this.uid = uid;
    }

    /**
     * Add the "latitude" optional argument to the user.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param latitude latitude
     */
    public User latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Add the "longitude" optional argument to the user.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     *
     * @param longitude longitude
     */
    public User longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Get the ID of this user.
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * Get the optional latitude attribute of this user.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * Get the optional longitude attribute of this user.
     * <p>
     * Only certain data backend support geospatial indexing.
     * Please refer to the main documentation for more information.
     */
    public Double getLongitude() {
        return this.longitude;
    }

}
