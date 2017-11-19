package cn.edu.gdmec.android.mobileguard.m5virusscan.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import android.os.Environment;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m1home.entity.VersionEntity;

public class VersionUpdateUtils {
    /*本地版本号 */
    private String mVersion;
    private Activity context;
    private VersionEntity versionEntity;

    //回调，需要在声明
    private DownloadedCallBack downloadedCallBack;
    //下一个activity的 class
    private Class<?> nextActivity;

    public  long mTaskid;
    //广播接收者
    private  BroadcastReceiver broadcastReceiver;

    private static final int MESSAGE_NET_ERROR = 101;
    private static final int MESSAGE_IO_ERROR = 102;
    private static final int MESSAGE_JSON_ERROR= 103;
    private static final int MESSAGE_SHOW_ERROR= 104;
    private static final int MESSAGE_ENTERHOME= 105;


    /**用于更新UI */
    private Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case MESSAGE_IO_ERROR:
                    Toast.makeText(context , "IO异常", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_ERROR:
                    Toast.makeText(context , "JSON解析异常", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case MESSAGE_NET_ERROR:
                    Toast.makeText(context,"网络异常",Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case MESSAGE_SHOW_ERROR:
                    showUpdateDialog(versionEntity);
                    break;

                case MESSAGE_ENTERHOME:
                    //Intent intent = new Intent(context,VirusScanActivity.class);//此处回调到下一个activity
                    if(nextActivity != null){
                        Intent intent = new Intent(context,nextActivity);
                        context.startActivity(intent);
                        context.finish();
                    }
                    break;
            };
        };
    };

    /**
     * 获取服务器版本号
     */
    public VersionUpdateUtils(String mVersion,Activity context,DownloadedCallBack downloadedCallBack,Class<?> nextActivity){
        this.mVersion = mVersion;
        this.context = context;
        this.downloadedCallBack = downloadedCallBack;
        this.nextActivity = nextActivity;
    }
    public void getCloudVersion(String url){
        try{
            HttpClient client = new DefaultHttpClient();

            /*连接超时 */
            HttpConnectionParams.setConnectionTimeout(client.getParams(),5000);
            /*请求超时*/
            HttpConnectionParams.setSoTimeout(client.getParams(),5000);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse execute = client.execute(httpGet);
            if(execute.getStatusLine().getStatusCode()==200){
                //请求和响应都成功了
                HttpEntity entity = execute.getEntity();
                String result = EntityUtils.toString(entity,"utf-8");
                //创建jsonObject对象
                JSONObject jsonObject = new JSONObject(result);
                versionEntity = new VersionEntity();
                versionEntity.versioncode =jsonObject.getString("code");
                versionEntity.description =jsonObject.getString("des");
                versionEntity.apkurl = jsonObject.getString("apkurl");
                if(!mVersion.equals(versionEntity.versioncode)){
                    //版本号不一样
                    handler.sendEmptyMessage(MESSAGE_SHOW_ERROR);
                }
            }
        }catch(ProtocolException e){
            handler.sendEmptyMessage(MESSAGE_NET_ERROR);
            e.printStackTrace();
        }catch (IOException e){
            handler.sendEmptyMessage(MESSAGE_IO_ERROR);
            e.printStackTrace();
        }catch (JSONException e){
            handler.sendEmptyMessage(MESSAGE_JSON_ERROR);
            e.printStackTrace();
        }
    }
    /**
     * 弹出更新提示对话框
     * @param versionEntity
     */
    private void showUpdateDialog(final VersionEntity versionEntity){
        //创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检查到新版本："+versionEntity.versioncode+"\n");//设置标题
        builder.setMessage(versionEntity.description);
        //根据服务器返回的描述，设置升级描述信息
        builder.setCancelable(false);//设置不能点击手机返回按钮隐藏对话框
        builder.setIcon(R.drawable.ic_launcher);//设置对话框图标
        //设置立即升级按钮点击事件
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                downloadNewApk(versionEntity.apkurl);
                enterHome();


            }
        });
    //设置不升级按钮点击事件
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                enterHome();

            }
        });
        builder.show();
    }
    /**
     * 下载新版本
     */
    protected  void downloadNewApk(String apkurl){
       // DownLoadUtils downLoadUtils = new DownLoadUtils();

        String filename = "downloadfile";
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc|apk|db";
        Pattern pat = Pattern.compile("[\\w]+[\\.]("+suffixes+")");
        Matcher mc = pat.matcher(apkurl);
        while (mc.find()){
            filename = mc.group();
        }
        // DownLoadUtils downLoadUtils = new DownLoadUtils(context,downloadedCallBack);
        downloadApk(apkurl, filename,context);
    }
    private void enterHome(){
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME,2000);
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
        request.setDestinationInExternalPublicDir("/download/",targetFile);
        //request.setDestinationUri(Uri.parse("/data/data/"+context.getPackageName()+"/files/"));
        //开始下载
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mTaskid = downloadManager.enqueue(request);
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

                    Toast.makeText(context.getApplicationContext(),"下载编号"+Id+"，此"+fileName+"已经下载完成了",Toast.LENGTH_LONG).show();
                }
                context.unregisterReceiver(broadcastReceiver);

                downloadedCallBack.completedDownload(fileName);

            }
        };
        context.registerReceiver(broadcastReceiver,intentFilter);
    }


    public  interface DownloadedCallBack{
        void completedDownload(String filename);
    }
}
