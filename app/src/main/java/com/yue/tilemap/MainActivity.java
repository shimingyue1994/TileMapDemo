package com.yue.tilemap;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yue.tilemap.bean.MainItem;
import com.yue.tilemap.bean.ViewHolder;
import com.yue.tilemap.databinding.ActivityMainBinding;
import com.yue.tilemap.ui.TGoogleTileActivity;
import com.yue.tilemap.ui.TMapShowActivity;
import com.yue.tilemap.ui.TSimpleMapActivity;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 瓦片地图 显示google瓦片  两种方案 OpenStreetMap 或高德地图
 * 还差缓存 和范围计算
 * <p>
 * WMS：坐标系转换问题 如果是火星坐标系统，直接贴在高德地图上，如果是google 贴在openstreetmap上
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private List<MainItem> mList;

    private MainAdapter mAdapter;

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mList = new ArrayList<>();
        mAdapter = new MainAdapter(this, mList);
        mBinding.lvMain.setAdapter(mAdapter);
        mBinding.lvMain.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        mList.add(new MainItem("简单地图显示openstreetmap", "简单地图显示openstreetmap" + TSimpleMapActivity.class.getSimpleName(), TSimpleMapActivity.class));
        mList.add(new MainItem("测试图源显示", "测试TestActivity01" + TMapShowActivity.class.getSimpleName(), TMapShowActivity.class));
        mList.add(new MainItem("测试google图源openstreetmap", "测试google图源openstreetmap" + TGoogleTileActivity.class.getSimpleName(), TGoogleTileActivity.class));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, mList.get(position).getaClass()));
    }

    class MainAdapter extends BaseAdapter {
        private Context context;
        private List<MainItem> list;

        public MainAdapter(Context context, List<MainItem> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
            }
            TextView tvText = ViewHolder.get(convertView, R.id.tv_main_text);

            MainItem item = list.get(position);
            tvText.setText(item.getName() + "\n" + item.getDesc());
            return convertView;
        }
    }
}
