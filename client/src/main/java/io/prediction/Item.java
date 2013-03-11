package io.prediction;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Item class for PredictionIO item objects
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.3
 * @since 0.2
 */

public class Item {
    private String iid;
    private String[] itypes;
    private Date startT;
    private Date endT;
    private Double latitude;
    private Double longitude;
    private DateTime created;

    public Item(String iid, String[] itypes) {
        this.iid = iid;
        this.itypes = itypes;
    }

    public Item startT(Date startT) {
        this.startT = startT;
        return this;
    }

    public Item endT(Date endT) {
        this.endT = endT;
        return this;
    }

    public Item latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Item longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Item created(DateTime created) {
        this.created = created;
        return this;
    }

    public String getIid() {
        return this.iid;
    }

    public String[] getItypes() {
        return this.itypes;
    }

    public Date getStartT() {
        return this.startT;
    }

    public Date getEndT() {
        return this.endT;
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
