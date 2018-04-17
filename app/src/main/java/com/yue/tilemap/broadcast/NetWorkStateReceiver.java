package com.yue.tilemap.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yue.tilemap.bean.NetBean;
import com.yue.tilemap.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:NetWorkStateReceiver
 * @author: shimy
 * @date: 2017/10/20 0020 下午 3:02
 * @description: 监听网络状态的变化
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (CommonUtils.isNetworkAvailable(context)) {
//            Toast.makeText(context, "有网啦", Toast.LENGTH_SHORT).show();
            NetBean netBean = new NetBean();
            netBean.setConn(true);
            EventBus.getDefault().postSticky(netBean);
        } else {
//            Toast.makeText(context, "没有网", Toast.LENGTH_SHORT).show();
            NetBean netBean = new NetBean();
            netBean.setConn(false);
            EventBus.getDefault().postSticky(netBean);
        }
    }
}
