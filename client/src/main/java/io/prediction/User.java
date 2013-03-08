package io.prediction;

import org.joda.time.DateTime;

/**
 * User class for PredictionIO User objects
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.3
 * @since 0.2
 */

public class User {
    private String uid;
    private Double latitude;
    private Double longitude;
    private DateTime created;

    public User(String uid) {
        this.uid = uid;
    }

    public User latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public User longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public User created(DateTime created) {
        this.created = created;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public DateTime getCreated() {
        return this.created;
    }
}
