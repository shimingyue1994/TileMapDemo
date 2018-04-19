package com.yue.tilemap.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class TrackDataBean {

    @Id
    private long time;
    private double lat;
    private double lng;
    private double bearing;


    @Generated(hash = 605365247)
    public TrackDataBean(long time, double lat, double lng, double bearing) {
        this.time = time;
        this.lat = lat;
        this.lng = lng;
        this.bearing = bearing;
    }

    @Generated(hash = 1899149185)
    public TrackDataBean() {
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
}
