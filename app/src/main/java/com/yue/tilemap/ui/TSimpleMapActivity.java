package com.yue.tilemap.ui;

import android.Manifest;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivityTsimpleMapBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 简单地图展示 openstreetmap
 */
public class TSimpleMapActivity extends AppCompatActivity {

    private String TAG = "TSimpleMapActivity";

    private ActivityTsimpleMapBinding mBinding;

    //指南针
    private CompassOverlay mCompassOverlay;

    private MyLocationNewOverlay mLocationOverlay;

    //旋转手势
    private RotationGestureOverlay mRotationGestureOverlay;

    //显示比例尺
    private ScaleBarOverlay mScaleBarOverlay;

    //添加内置迷你地图
    private MinimapOverlay mMinimapOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*初始化osmandroid 配置*/
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tsimple_map);
        setTitle("opensimplemap简单显示");
        requestPermissions();
        //设置瓦片图片图源
        mBinding.mapTsimple.setTileSource(TileSourceFactory.MAPNIK);
        //设置缩放按钮 此时可以通过手指进行缩放
        mBinding.mapTsimple.setBuiltInZoomControls(true);
        mBinding.mapTsimple.setMultiTouchControls(true);

        //获取地图控制器
        IMapController mapController = mBinding.mapTsimple.getController();
        //设置缩放级别
        mapController.setZoom(14d);
        //设置并移动到中心点120.169262,35.976249
        GeoPoint startPoint = new GeoPoint(35.976249, 120.169262);
        mapController.setCenter(startPoint);

        /**
         * 覆盖物的添加
         */
        /*显示指南针*/
        this.mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mBinding.mapTsimple);
        this.mCompassOverlay.enableCompass();
        mBinding.mapTsimple.getOverlays().add(this.mCompassOverlay);
        /*显示当前位置 好像没啥用*/
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mBinding.mapTsimple);
        this.mLocationOverlay.enableMyLocation();
        mBinding.mapTsimple.getOverlays().add(this.mLocationOverlay);

        /*添加网格线覆盖 网格显示经纬度*/
//        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
//        mBinding.mapTsimple.getOverlays().add(overlay);

        /*添加旋转手势 缩放时地图会旋转*/
        mRotationGestureOverlay = new RotationGestureOverlay(mBinding.mapTsimple);
        mRotationGestureOverlay.setEnabled(true);
        mBinding.mapTsimple.setMultiTouchControls(true);
        mBinding.mapTsimple.getOverlays().add(this.mRotationGestureOverlay);

        /*比例尺覆盖物显示*/
        final DisplayMetrics dm = this.getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(mBinding.mapTsimple);
        mScaleBarOverlay.setCentred(true);
        //play around with these values to get the location on screen in the right place for your applicatio
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mBinding.mapTsimple.getOverlays().add(this.mScaleBarOverlay);


        /*添加内置迷你地图*/
        mMinimapOverlay = new MinimapOverlay(this, mBinding.mapTsimple.getTileRequestCompleteHandler());
        mMinimapOverlay.setWidth(dm.widthPixels / 3);
        mMinimapOverlay.setHeight(dm.heightPixels / 2);
        //optionally, you can set the minimap to a different tile source
        //mMinimapOverlay.setTileSource(....);
        mBinding.mapTsimple.getOverlays().add(this.mMinimapOverlay);

        /*添加标记点*/
        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Title", "Description", new GeoPoint(35.976249, 120.169262))); // Lat/Lon decimal degrees
        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(TSimpleMapActivity.this, "点击了" + item.getPoint().getLatitude(), Toast.LENGTH_SHORT).show();
                        //do something
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        mOverlay.setFocusItemsOnTap(false);
//        mOverlay.setFocusedItem(0);
        mBinding.mapTsimple.getOverlays().add(mOverlay);

    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mBinding.mapTsimple != null)
            mBinding.mapTsimple.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mBinding.mapTsimple != null)
            mBinding.mapTsimple.onPause();
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                               @Override
                               public void accept(Permission permission) throws Exception {
                                   if (permission.granted) {
                                       // 所有权限都已授权
//                                       Toast.makeText(TSimpleMapActivity.this, "权限请求成功", Toast.LENGTH_SHORT).show();
                                   } else if (permission.shouldShowRequestPermissionRationale) {
                                       // 至少有一个权限未被授予 没有选中 [不在询问按钮]
                                       Toast.makeText(TSimpleMapActivity.this, "有权限未申请成功" + permission.name, Toast.LENGTH_SHORT).show();
                                       if (permission.name.equals(Manifest.permission.ACCESS_COARSE_LOCATION) || permission.name.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                           Toast.makeText(TSimpleMapActivity.this, "定位权限未被授予", Toast.LENGTH_SHORT).show();
                                       } else if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                           Toast.makeText(TSimpleMapActivity.this, "存储权限未被授予", Toast.LENGTH_SHORT).show();
                                       }
                                   } else {
                                       // 至少有一个权限未被授予 选中了[不在询问按钮]
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable t) {
                                   Log.e(TAG, "onError", t);
                                   Toast.makeText(TSimpleMapActivity.this, "权限请求错误", Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() {
                                Log.i(TAG, "OnComplete");
//                                Toast.makeText(TSimpleMapActivity.this, "权限请求完成", Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}
