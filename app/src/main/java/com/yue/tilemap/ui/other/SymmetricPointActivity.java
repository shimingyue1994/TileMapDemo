package com.yue.tilemap.ui.other;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivitySymmetricPointBinding;

/**
 * 根据一点经纬度、距离、方位角 计算另一点的经纬度
 */
public class SymmetricPointActivity extends AppCompatActivity {

    private ActivitySymmetricPointBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_symmetric_point);
    }

    private void initView(){


    }
}
