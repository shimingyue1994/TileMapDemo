package com.yue.tilemap.ui.other;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yue.tilemap.R;
import com.yue.tilemap.bean.NetBean;
import com.yue.tilemap.databinding.ActivitySymmetricPointBinding;
import com.yue.tilemap.service.GdLocationService;
import com.yue.tilemap.ui.TSimpleMapActivity;
import com.yue.tilemap.utils.LatlngByAngleDistance;
import com.yue.tilemap.utils.LatlngByAngleDistance2;
import com.yue.tilemap.utils.LatlngByAngleDistance3;
import com.yue.tilemap.utils.MapUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * *****************
 * *               * 假设50米
 * ***************** 直线轨迹
 * *               *
 * *****************
 * <p>
 * <p>
 * 实现区域划分（可以先使用百度鹰眼记录轨迹[将角度存储下来]，然后再根据已知轨迹的点集合和角度算法求出区域点集合）
 * 根据一点经纬度、距离、方位角 计算另一点的经纬度
 * 角度计算：计算与当前方向夹角位90度 distance位d米的一个点（经纬度）
 * 假设当前点为x度（与x轴正方向形成的夹角），则与他形成直角90度点的角度（与x轴正方向形成的夹角）为
 * 1.[360+(x+90)]
 * 2.[x+90]
 */
public class SymmetricPointActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SymmetricPointActivity";

    private ActivitySymmetricPointBinding mBinding;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;//定位模式

    /*定位的点 将基于这个点去计算出另外两个对称点*/
    private AMapLocation mLocation;

    private int type = 0;
    private boolean isLocation = false;
    private boolean isCenter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_symmetric_point);
        requestPermissions();
        EventBus.getDefault().register(this);
        startService(new Intent(this, GdLocationService.class));
        mBinding.btnSymmetricFirst.setOnClickListener(this);
        mBinding.btnSymmetricSecond.setOnClickListener(this);
        mBinding.btnSymmetricClear.setOnClickListener(this);
        mBinding.spinnerSymmetricMethod.setOnItemSelectedListener(onItemSelectedListener);
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        mBinding.mapSymmetricPoint.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mapSymmetricPoint.getMap();
            //设置缩放级别
            aMap.animateCamera(CameraUpdateFactory.zoomTo(16f));
            // 如果要设置定位的默认状态，可以在此处进行设置
            myLocationStyle = new MyLocationStyle();
            // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationStyle(myLocationStyle);
        }

        /**
         * 设置定位监听
         */
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!isCenter && location.getLatitude() != 0) {
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    isCenter = true;
                }
            }
        });

    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String[] method = getResources().getStringArray(R.array.latlngad_method);
            Toast.makeText(SymmetricPointActivity.this, "position" + position + " " + method[position], Toast.LENGTH_SHORT).show();
            type = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_symmetric_first://第一个点
                //角度
                double firistAngle = mLocation.getBearing() + 90;
                if (firistAngle >= 360) {//角度超过360的问题
                    firistAngle = firistAngle - 360;
                }
                Toast.makeText(this, "第一个点", Toast.LENGTH_SHORT).show();
                /*正前方测试*/
                addMarkType(mLocation.getLongitude(), mLocation.getLatitude(), 50, firistAngle);
                break;
            case R.id.btn_symmetric_second://第二个点
                double secondAngle = 360 + (mLocation.getBearing() - 90);
                if (secondAngle >= 360) {//角度超过360的问题
                    secondAngle = secondAngle - 360;
                }
                Toast.makeText(this, "第二个点", Toast.LENGTH_SHORT).show();
                /*正前方测试*/
                addMarkType(mLocation.getLongitude(), mLocation.getLatitude(), 50, secondAngle);
                break;
            case R.id.btn_symmetric_clear://清除所有marker
                aMap.clear();
                break;
        }
    }

    /**
     * 选择使用那种算法
     *
     * @param lon      经度 （china-长）
     * @param lat      纬度 （china-短）
     * @param distance 距离 单位（米）
     * @param angle    方位角
     */
    private void addMarkType(double lon, double lat, double distance, double angle) {
        String latlng = "";
        switch (type) {
            case 0:
                LatlngByAngleDistance latlngByAngleDistance = new LatlngByAngleDistance(lon, lat);
                double distance0 = distance / 1000;
                latlng = LatlngByAngleDistance.getMyLatLng(latlngByAngleDistance, distance0, angle);
                break;
            case 1:
                LatlngByAngleDistance2 latlngByAngleDistance2 = new LatlngByAngleDistance2();
                latlng = latlngByAngleDistance2.computerThatLonLat(lon, lat, angle, distance);
                break;
            case 2:
                double distance3 = distance / 1000;
                latlng = LatlngByAngleDistance3.getLatlng(lon, lat, distance3, angle);
                break;
        }
        Log.i("SymmetricPointActivity", "目标方位角：" + angle + " 目标距离(米)：" + distance);
        mBinding.tvSymmetricLocbear.setText("当前点的方位角：" + mLocation.getBearing());
        mBinding.tvSymmetricInitad.setText("");
        mBinding.tvSymmetricInitad.setText("目标方位角：" + (int) angle + "目标距离(米)：" + (int) distance);
        if (!TextUtils.isEmpty(latlng)) {
            String lonNewString = latlng.split(",")[0];
            String latNewString = latlng.split(",")[1];
            double lonNew = Double.parseDouble(lonNewString);
            double latNew = Double.parseDouble(latNewString);
            addMark(lonNew, latNew);
        } else {
            Toast.makeText(this, "经纬度生成失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加经纬度
     *
     * @param lon 经度
     * @param lat 纬度
     */
    private void addMark(double lon, double lat) {
        LatLng latLng = new LatLng(lat, lon);
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng)
                .draggable(false);
        Marker marker = aMap.addMarker(markerOption);
        float distance = AMapUtils.calculateLineDistance(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), latLng);
        double angle = MapUtils.getAngle1(mLocation.getLatitude(), mLocation.getLongitude(), latLng.latitude, latLng.longitude);
        mBinding.tvSymmetricEndad.setText("");
        mBinding.tvSymmetricEndad.setText("计算后方位角：" + (int) angle + " 计算后距离(米)：" + (int) distance);
    }


    /**
     * EventBus消息接收处
     * 实时定位 方位角已开启
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLocation(AMapLocation aMapLocation) {
//        Toast.makeText(this, "得到经纬度" +
//                        aMapLocation.getLatitude() + " 方位角：" +
//                        aMapLocation.getBearing() + " 错误码：" +
//                        aMapLocation.getErrorCode(),
//                Toast.LENGTH_SHORT).show();
        mLocation = aMapLocation;
        if (mLocation == null || mLocation.getLatitude() == 0.0) {
            Toast.makeText(this, "定位失败" + mLocation.getErrorCode() + mLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
        }
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
                                       Toast.makeText(SymmetricPointActivity.this, "有权限未申请成功" + permission.name, Toast.LENGTH_SHORT).show();
                                       if (permission.name.equals(Manifest.permission.ACCESS_COARSE_LOCATION) || permission.name.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                           Toast.makeText(SymmetricPointActivity.this, "定位权限未被授予", Toast.LENGTH_SHORT).show();
                                       } else if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                           Toast.makeText(SymmetricPointActivity.this, "存储权限未被授予", Toast.LENGTH_SHORT).show();
                                       }
                                   } else {
                                       // 至少有一个权限未被授予 选中了[不在询问按钮]
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable t) {
                                   Log.e(TAG, "onError", t);
                                   Toast.makeText(SymmetricPointActivity.this, "权限请求错误", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mapSymmetricPoint.onDestroy();
        stopService(new Intent(this, GdLocationService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mapSymmetricPoint.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mapSymmetricPoint.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mapSymmetricPoint.onSaveInstanceState(outState);
    }

}
