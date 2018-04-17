package com.yue.tilemap.ui.gaode;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yue.tilemap.R;
import com.yue.tilemap.databinding.ActivityTgdGoogleOfflineBinding;

/**
 * 离线加载地图
 */
public class TgdGoogleOfflineActivity extends AppCompatActivity {

    private ActivityTgdGoogleOfflineBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tgd_google_offline);
    }

    /**
     * 初始化地图基本设置
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {

    }

    /**
     * 初始化瓦片
     */
    private void initTile() {

    }


    /**
     * 在线瓦片 显示
     */
    private void showOnlineTile() {

    }


    /**
     * 离线瓦片 显示
     */
    private void showOffLineTile() {

    }
}
