package io.prediction;

import java.util.Date;

/**
 * User class for PredictionIO User objects
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.2
 * @since 0.2
 */

public class User {
    private String uid;
    private String gender;
    private Date bday;
    private Double latitude;
    private Double longitude;
    private Date created;
    private Date modified;

    public User(String uid) {
        this.uid = uid;
    }

    public User gender(String gender) {
        this.gender = gender;
        return this;
    }

    public User bday(Date bday) {
        this.bday = bday;
        return this;
    }

    public User latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public User longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public User created(Date created) {
        this.created = created;
        return this;
    }

    public User modified(Date modified) {
        this.modified = modified;
        return this;
    }

    public String getUid() {
        return this.uid;
    }

    public String getGender() {
        return this.gender;
    }

    public Date getBday() {
        return this.bday;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Date getCreated() {
        return this.created;
    }

    public Date getModified() {
        return this.modified;
    }
}
