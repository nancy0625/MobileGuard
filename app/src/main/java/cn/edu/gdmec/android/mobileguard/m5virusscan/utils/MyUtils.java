package cn.edu.gdmec.android.mobileguard.m5virusscan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.edu.gdmec.android.mobileguard.m5virusscan.dao.AntiVirusDao;

/**
 * Created by asus on 2017/9/9.
 */

public class MyUtils  {
    /**
     * 获取版本号
     * @param context
     * @return 返回版本号
     */
    public static String getVersion(Context context) {
        AntiVirusDao antiVirusDao = new AntiVirusDao(context);
        String result = antiVirusDao.checkVersion();
        return result;
    }


    /**
     * 安装新版本
     * @param activity
     */
    public static void installApk(Activity activity){
        Intent intent = new Intent("android.intent.action.VIEW");
        //添加默认分类
        intent.addCategory("android.intent.category.DEFAULT");
        //设置数据和类型
        //intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/mobilesafe2.0apk")),"application/vnd.android.package-archive");
        //如果开启的Activity退出时会回调当前Activity的onActivityResult
        activity.startActivityForResult(intent,0);
    }

}
