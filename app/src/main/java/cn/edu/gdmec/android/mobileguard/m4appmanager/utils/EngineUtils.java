package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;

import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

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
        intent.putExtra(Intent.EXTRA_TEXT," 推荐您使用一款软件，名称叫："+appInfo.appName+"下载路径：https://play.google.com/store/apps/details?id="+appInfo.packageName);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PackageInfo packageInfo = packageManager.getPackageInfo(appInfo.packageName,0);
        try {


            long firstInstallTime = packageInfo.firstInstallTime;
            PackageInfo pi = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] perssion = pi.requestedPermissions;
            String version = packageInfo.versionName;
            PackageInfo packinfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            String singatures = packinfo.signatures[0].toCharsString();

            try
            {
                CertificateFactory
                        certFactory =
                        CertificateFactory.getInstance("X.509");

                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new
                        ByteArrayInputStream(singatures.getBytes()));

                String pubKey =
                        cert.getPublicKey().toString();   //公钥

                String signNumber =
                        cert.getSerialNumber().toString();

                System.out.println("signName:" +
                        cert.getSigAlgName());//算法名
                System.out.println("pubKey:" +
                        pubKey);

                System.out.println("signNumber:" +
                        signNumber);//证书序列编号

                System.out.println("subjectDN:"+cert.getSubjectDN().toString());

            } catch (CertificateException e)
            {

                e.printStackTrace();
            }
            intent.putExtra(Intent.EXTRA_TEXT,version+firstInstallTime+singatures+perssion);
            builder.setTitle("MobileGuard");

            SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 a hh:mm:ss");
            builder.setMessage("Version："+version+"\n"+"install time:"+format.format(firstInstallTime)+"\n"+"Certificate issuer:"+singatures+"\n"+"Perssions:\n"+perssion);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

    }


}
