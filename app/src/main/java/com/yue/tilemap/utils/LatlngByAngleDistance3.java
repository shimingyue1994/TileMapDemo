package com.yue.tilemap.utils;

import com.amap.api.maps.model.LatLng;

/**
 * 地图工具类
 */
public class LatlngByAngleDistance3 {


    /**
     * 根据一个点的经纬度、方位角、距离得到另外一个点的经纬度 有误差
     *
     * @param distance 单位 km
     * @param latlngA
     * @param angle：角度
     * @return
     */
    public static LatLng getLatlng(float distance, LatLng latlngA, double angle) {
        return new LatLng(latlngA.latitude + (distance * Math.cos(angle * Math.PI / 180)) / 111,
                latlngA.longitude + (distance * Math.sin(angle * Math.PI / 180)) / (111 * Math.cos(latlngA.latitude * Math.PI / 180))
        );
    }


    /**
     * @param lon      经度
     * @param lat      纬度
     * @param distance 距离 单位km
     * @param angle    方位角
     * @return
     */
    public static String getLatlng(double lon, double lat, double distance, double angle) {
        double turnLat = lat + (distance * Math.cos(angle * Math.PI / 180)) / 111;
        double turnLon = lon + (distance * Math.sin(angle * Math.PI / 180)) / (111 * Math.cos(lat * Math.PI / 180));
        return turnLon + "," + turnLat;
    }
}
