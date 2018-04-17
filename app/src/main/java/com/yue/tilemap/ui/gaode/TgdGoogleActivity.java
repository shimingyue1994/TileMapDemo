package com.yue.tilemap.ui.gaode;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;
import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivityTgdGoogleBinding;

import java.net.URL;

/**
 * google地图贴图贴在高德地图上
 */
public class TgdGoogleActivity extends AppCompatActivity {

    private AMap aMap;
    final String url = "http://a.tile.openstreetmap.org/%d/%d/%d.png";
    String googleUrl01 = "http://mt0.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
    String googleUrl02 = "http://mt1.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
    String googleUrl03 = "http://mt2.google.cn/vt/lyrs=y&hl=zh-CN&gl=cn";
    String googleUrl04 = "http://mt3.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
    private ActivityTgdGoogleBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tgd_google);
        initGdMap(savedInstanceState);
    }

    /**
     * 高德地图初始化
     *
     * @param savedInstanceState
     */
    private void initGdMap(Bundle savedInstanceState) {
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//		   MapsInitializer.sdcardDir = OffLineMapUtils.getSdCacheDir(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mapTgdGoogle.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mapTgdGoogle.getMap();
            initTile(savedInstanceState);
        }
    }

    /**
     * 初始化贴片设置
     */
    private void initTile(Bundle savedInstanceState) {
        aMap.addTileOverlay(new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {

            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    Log.i("TileOverlayActivity", "url:" + String.format(googleUrl03, zoom, x, y));
//					return new URL(String.format(url, zoom, x, y));
                    return new URL(googleUrl03 + "&x=" + x + "&y=" +
                            y + "&z=" + zoom);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        })
                .diskCacheEnabled(true)
                .diskCacheDir("/storage/emulated/0/amap/cache")
                .diskCacheSize(100000)
                .memoryCacheEnabled(true)
                .memCacheSize(100000));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mapTgdGoogle.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mapTgdGoogle.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mapTgdGoogle.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mapTgdGoogle.onSaveInstanceState(outState);
    }

}
