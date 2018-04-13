package com.yue.tilemap.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivityTgoogleTileBinding;
import com.yue.tilemap.tilesource.GoogleMapsTileSource;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;

/**
 * 展示google图源
 */
public class TGoogleTileActivity extends AppCompatActivity {
    private ActivityTgoogleTileBinding mBinging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinging = DataBindingUtil.setContentView(this, R.layout.activity_tgoogle_tile);
        setTitle("测试google图源openstreetmap");
        initGoogle();
        //设置缩放按钮 此时可以通过手指进行缩放
        mBinging.mapTgoogle.setBuiltInZoomControls(true);
        mBinging.mapTgoogle.setMultiTouchControls(true);
//        mBinging.mapTgoogle.setTilesScaledToDpi(true);

        //获取地图控制器
        IMapController mapController = mBinging.mapTgoogle.getController();
        //设置缩放级别
        mapController.setZoom(3d);
        //设置并移动到中心点120.169262,35.976249
        GeoPoint startPoint = new GeoPoint(35.976249, 120.169262);
        mapController.setCenter(startPoint);
    }

    private void initGoogle() {
        //&scale=2
        String str1 = "http://mt0.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
        String str2 = "http://mt1.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
        String str3 = "http://mt2.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
        String str4 = "http://mt3.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";

        GoogleMapsTileSource googleMapsTileSource = new GoogleMapsTileSource("GoogleNormal",
                0,
                19,
                256,
                ".png",
                new String[]{str1, str2, str3, str4});
        mBinging.mapTgoogle.setTileSource(googleMapsTileSource);
    }
}
