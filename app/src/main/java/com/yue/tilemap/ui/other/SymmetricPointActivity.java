package com.yue.tilemap.ui.other;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivitySymmetricPointBinding;
import com.yue.tilemap.utils.LatlngByAngleDistance;
import com.yue.tilemap.utils.LatlngByAngleDistance2;
import com.yue.tilemap.utils.LatlngByAngleDistance3;
import com.yue.tilemap.utils.MapUtils;

/**
 * 根据一点经纬度、距离、方位角 计算另一点的经纬度
 * 角度计算：计算与当前方向夹角位90度 distance位d米的一个点（经纬度）
 * 假设当前点为x度（与x轴正方向形成的夹角），则与他形成直角90度点的角度（与x轴正方向形成的夹角）为
 * 1.[360+(x+90)]
 * 2.[x+90]
 */
public class SymmetricPointActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySymmetricPointBinding mBinding;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;//定位模式

    /*定位的点 将基于这个点去计算出另外两个对称点*/
    private Location mLocation;

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_symmetric_point);
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
            // 如果要设置定位的默认状态，可以在此处进行设置
            myLocationStyle = new MyLocationStyle();
            // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
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
                if (location.getLatitude() != 0) {
                    // 定位、但不会移动到地图中心点，并且会跟随设备移动。
//                    aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER));
                    mLocation = location;
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
                Toast.makeText(this, "第一个点", Toast.LENGTH_SHORT).show();
                /*正前方测试*/
                addMarkType(mLocation.getLongitude(), mLocation.getLatitude(), 50, firistAngle);
                break;
            case R.id.btn_symmetric_second://第二个点
                double secondAngle = 360 + (mLocation.getBearing() - 90);
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
        mBinding.tvSymmetricLocbear.setText("当前点的方位角："+mLocation.getBearing());
        mBinding.tvSymmetricInitad.setText("");
        mBinding.tvSymmetricInitad.setText("目标方位角：" + (int)angle + "目标距离(米)：" + (int)distance);
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
        mBinding.tvSymmetricEndad.setText("计算后方位角：" + angle + " 计算后距离(米)：" + distance);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mapSymmetricPoint.onDestroy();
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
