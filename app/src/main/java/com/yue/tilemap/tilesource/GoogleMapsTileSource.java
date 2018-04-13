package com.yue.tilemap.tilesource;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.MapTileIndex;

/**
 * google 服务器图源
 */
public class GoogleMapsTileSource extends OnlineTileSourceBase {


//        String str1 = "http://mt0.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=2";
//        String str2 = "http://mt1.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=2";
//        String str3 = "http://mt2.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=2";
//        String str4 = "http://mt3.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=2";

    public GoogleMapsTileSource(String aName, int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding, String[] aBaseUrl) {
        super(aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding, aBaseUrl);
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {

        return getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" +
                MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
    }
}
