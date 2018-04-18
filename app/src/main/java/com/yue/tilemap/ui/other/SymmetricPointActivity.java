package com.yue.tilemap.ui.other;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivitySymmetricPointBinding;

/**
 * 根据一点经纬度、距离、方位角 计算另一点的经纬度
 */
public class SymmetricPointActivity extends AppCompatActivity {

    private ActivitySymmetricPointBinding mBinding;
    private AMap aMap;

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
        }

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
