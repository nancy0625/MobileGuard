package cn.edu.gdmec.android.mobileguard.m2home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.edu.gdmec.android.mobileguard.App;

/**
 * Created by acer on 2017/10/16.
 */
/** 监听开机的广播该类，主要用于检查SIM卡是否被更换，如果被更换则发送短信给安全号码*/
public class BootCompleteReceiver extends BroadcastReceiver{
    private static final String TAG = BootCompleteReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ((App)(context.getApplicationContext())).correctSIM();//初始化
    }
}
