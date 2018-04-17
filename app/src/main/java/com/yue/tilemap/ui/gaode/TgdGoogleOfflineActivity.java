package com.yue.tilemap.ui.gaode;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;
import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivityTgdGoogleOfflineBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URL;
import java.util.HashMap;

/**
 * 离线加载地图
 * 注意缩放级别的控制
 */
public class TgdGoogleOfflineActivity extends AppCompatActivity {

    private ActivityTgdGoogleOfflineBinding mBinding;

    private AMap aMap;
    //lyrs 表示图层
    final String url = "http://a.tile.openstreetmap.org/%d/%d/%d.png";
    String googleUrl01 = "http://mt0.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
    String googleUrl02 = "http://mt1.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";
    String googleUrl03 = "http://mt2.google.cn/vt/lyrs=r&hl=zh-CN&gl=cn";
    String googleUrl04 = "http://mt3.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tgd_google_offline);
        setTitle("高德地图google离线地图");
        initGdMap(savedInstanceState);
    }

    /**
     * 初始化地图基本设置
     *
     * @param savedInstanceState
     */
    private void initGdMap(Bundle savedInstanceState) {
        mBinding.mapTgdgoogleOffline.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mapTgdgoogleOffline.getMap();
            initTile(savedInstanceState);
        }
    }

    /**
     * 初始化瓦片
     */
    private void initTile(Bundle savedInstanceState) {
        showOnlineTile();

    }


    /**
     * 在线瓦片 显示
     */
    private void showOnlineTile() {
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


    /**
     * 离线瓦片 显示
     */
    private void showOffLineTile() {

    }

    //EventBus消息接收处
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshView(HashMap<String, Object> map) {
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mapTgdgoogleOffline.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mapTgdgoogleOffline.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mapTgdgoogleOffline.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mapTgdgoogleOffline.onSaveInstanceState(outState);
    }

}
