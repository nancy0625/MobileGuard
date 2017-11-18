package cn.edu.gdmec.android.mobileguard.m5virusscan.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.m5virusscan.VirusScanActivity;

public class DownLoadUtils {
    /**

     * 下载APK的方法

     * @param url

     * @param targetFile

     */
    public static long mTaskid=0;
    private static BroadcastReceiver broadcastReceiver;
    private Context mContext;

    public  DownLoadUtils(Context context){
        this.mContext = context;
    }


  
    public void downloadApk(String url, String targetFile, Context context) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //漫游是否可以下载
        request.setAllowedOverRoaming(false);
        //设置文件类型，下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mineString = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(url));
        //在通知栏显示，默认是显示的
        request.setMimeType(mineString);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        //下载路径
        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS.toString(), "antivirus.db" );
        //request.setDestinationUri(Uri.parse("/data/data/"+context.getPackageName()+"/files/"));
        //开始下载
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mTaskid = downloadManager.enqueue(request);

        /*Uri downFileUri = downloadManager.getUriForDownloadedFile(mTaskid);
        if (downFileUri != null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(downFileUri, "/data/data/"+context.getPackageName()+"/files/antivirus.db");
            context.startActivity(intent);

        }*/
        listener(mTaskid,targetFile);



    }
    private void listener(final long Id,final String fileName){
        //注册广播接收者
        IntentFilter intentFilter = new IntentFilter((DownloadManager.ACTION_DOWNLOAD_COMPLETE));
         broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
                if(id == Id){
                   Toast.makeText(context.getApplicationContext(),"下载编号"+Id,Toast.LENGTH_LONG).show();
                    /*VirusScanActivity virusScanActivity = new VirusScanActivity();
                    virusScanActivity.update();*/








                }
                context.unregisterReceiver(broadcastReceiver);
            }
        };
        mContext.registerReceiver(broadcastReceiver,intentFilter);
    }


}