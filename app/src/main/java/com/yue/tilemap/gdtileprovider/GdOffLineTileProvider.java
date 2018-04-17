package com.yue.tilemap.gdtileprovider;

import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;

/**
 * 离线地图
 */
public class GdOffLineTileProvider implements TileProvider {

    private final int tileWidth;
    private final int tileHeight;
    private String offlinePath;

    public GdOffLineTileProvider(int tileWidth, int tileHeight, String offlinePath) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.offlinePath = offlinePath;
    }

    @Override
    public Tile getTile(int i, int i1, int i2) {
        return null;
    }

    @Override
    public int getTileWidth() {
        return this.tileWidth;
    }

    @Override
    public int getTileHeight() {
        return this.tileHeight;
    }
}
