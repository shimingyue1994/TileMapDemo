package com.yue.tilemap.utils;

public class LatLngByAngleDistanceChange {


    /**
     * 求B点经纬度
     *
     * @param A 已知点的经纬度，
     * @param distance   AB两地的距离  单位km
     * @param angle  AB连线与正北方向的夹角（0~360）
     * @return B点的经纬度
     */
    final static double Rc = 6378137;
    final static double Rj = 6356725;

    /**
     * @param latitude  纬度
     * @param longitude 经度
     * @param distance  距离
     * @param angle     方位角
     * @return 返回格式 经度,纬度  示例 121.123124,35.132123
     */
    public static String getMyLatLng(double longitude, double latitude, double distance, double angle) {//方法


        double m_LoDeg, m_LoMin, m_LoSec;
        double m_LaDeg, m_LaMin, m_LaSec;
        double m_Longitude, m_Latitude;
        double m_RadLo, m_RadLa;
        double Ec;
        double Ed;

        m_LoDeg = (int) longitude;
        m_LoMin = (int) ((longitude - m_LoDeg) * 60);
        m_LoSec = (longitude - m_LoDeg - m_LoMin / 60.) * 3600;

        m_LaDeg = (int) latitude;
        m_LaMin = (int) ((latitude - m_LaDeg) * 60);
        m_LaSec = (latitude - m_LaDeg - m_LaMin / 60.) * 3600;

        m_Longitude = longitude;
        m_Latitude = latitude;
        m_RadLo = longitude * Math.PI / 180.;
        m_RadLa = latitude * Math.PI / 180.;
        Ec = Rj + (Rc - Rj) * (90. - m_Latitude) / 90.;
        Ed = Ec * Math.cos(m_RadLa);

        double dx = distance * 1000 * Math.sin(Math.toRadians(angle));
        double dy = distance * 1000 * Math.cos(Math.toRadians(angle));
        double bjd = (dx / Ed + m_RadLo) * 180. / Math.PI;
        double bwd = (dy / Ec + m_RadLa) * 180. / Math.PI;
        String lnglat = bjd + "," + bwd;
        return lnglat;
    }
}
