package com.yue.tilemap.ui.other;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.MyLocationStyle;
import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivitySymmetricPointBinding;

/**
 * 根据一点经纬度、距离、方位角 计算另一点的经纬度
 */
public class SymmetricPointActivity extends AppCompatActivity {

    private ActivitySymmetricPointBinding mBinding;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;//定位模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_symmetric_point);
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
//                Toast.makeText(SymmetricPointActivity.this, "定位" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                if (location.getLatitude() != 0) {
                    // 定位、但不会移动到地图中心点，并且会跟随设备移动。
                    aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER));
                }
            }
        });

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
