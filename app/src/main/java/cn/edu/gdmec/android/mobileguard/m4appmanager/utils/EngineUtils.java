package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;

import android.widget.EditText;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by student on 17/11/6.
 */

public class EngineUtils {
    /*分享应用*/
    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT," 推荐您使用一款软件，名称交："+appInfo.appName+"下载路径：https://play.google.com/store/apps/details?id="+appInfo.packageName);
        context.startActivity(intent);

    }
     /*开启应用程序*/
    public static  void startApplication(Context context,AppInfo appInfo){
        //  打开这个应用程序的入口activity
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent!=null){
            context.startActivity(intent);
        }else{
            Toast.makeText(context,"该应用没有启动页面",Toast.LENGTH_LONG).show();
        }
    }
    /*开启应用的设置页面*/
     public static void SettingAppDetail(Context context,AppInfo appInfo){
         Intent intent = new Intent();
         intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
         intent.addCategory(Intent.CATEGORY_DEFAULT);
         intent.setData(Uri.parse("package:"+appInfo.packageName));
         context.startActivity(intent);
     }
     /*卸载*/
     public static void uninstallApplication(Context context,AppInfo appInfo){
         if (appInfo.isUserApp){
             Intent intent = new Intent();
             intent.setAction(Intent.ACTION_DELETE);
             intent.setData(Uri.parse("package:"+appInfo.packageName));
             context.startActivity(intent);
         }else {
             Toast.makeText(context,"系统应用无法卸载 ",Toast.LENGTH_LONG).show();
         }
     }
    /*获取版本号*/
    public static void About(Context context,AppInfo appInfo) throws PackageManager.NameNotFoundException {
        Intent intent = new Intent();
        intent.setType("text/plain");
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(appInfo.packageName,0);
        try {


            long firstInstallTime = packageInfo.firstInstallTime;
            PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] perssion = pi.requestedPermissions;
            String version = packageInfo.versionName;
            PackageInfo packinfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            String singatures = packinfo.signatures[0].toCharsString();
            intent.putExtra(Intent.EXTRA_TEXT,version+firstInstallTime+singatures+perssion);
            System.out.print("版本号："+version+"时间"+firstInstallTime+singatures+perssion);
            context.startActivity(intent);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }




    }


}