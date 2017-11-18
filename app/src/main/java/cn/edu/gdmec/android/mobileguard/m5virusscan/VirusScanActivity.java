package cn.edu.gdmec.android.mobileguard.m5virusscan;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import junit.runner.Version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m5virusscan.dao.AntiVirusDao;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.DownLoadUtils;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.MyUtils;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.VersionUpdateUtils;
public class VirusScanActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mLastTimeTV;
    private TextView mtvversion;
    private SharedPreferences mSP;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_virus_scan);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        copyDB("antivirus.db","");
        initView();

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String dbVersion = MyUtils.getVersion(getApplicationContext());  //是比较版本后显示
            mtvversion = (TextView)findViewById(R.id.tv_version);
            mtvversion.setText("病毒数据库版本："+dbVersion);
            UpdateDB(dbVersion); //以下是根据版本号，进行更新数据库与页面显示
           super.handleMessage(msg);
        }
    };
    //下载成功后再次复制数据库，实现更新数据库,  封装个接口，实现更新版本库完成同时复制

    VersionUpdateUtils.DownloadedCallBack downloadedCallBack = new VersionUpdateUtils.DownloadedCallBack(){
        @Override
        public void completedDownload(String filename) {
            copyDB("antivirus.db", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        }
    };
    /*
    确保复制数据完成后，更新数据库,再获取本地版本号与云端版本号匹配    本地版本号   localVersion
    * */
    final private void  UpdateDB (String localVersion){
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(localVersion,VirusScanActivity.this,downloadedCallBack,VirusScanActivity.class);
        new Thread(){
            public void run(){
                //获取服务器版本号
                updateUtils.getCloudVersion();
            }
        }.start();
    }
    @Override
    protected void onResume() {
        String string = mSP.getString("lastVirusScan","您还没有查杀病毒");
        mLastTimeTV.setText(string);
        super.onResume();
    }
    /**
     * 复制病毒库
     * @param
     */
    private void  copyDB(final String dbname,final String path){
        new Thread(){
            public void run(){
                try {
                   File file = new File(getFilesDir(),dbname);
                    if (file.exists() && file.length()>0&&path.equals("")){
                        Log.i("VirusScanActivity","数据库已存在");
                        handler.sendEmptyMessage(0);
                        return;
                    }
                    InputStream is = getAssets().open(dbname);
                    FileOutputStream fos = openFileOutput(dbname,MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = is.read(buffer)) != -1){
                        fos.write(buffer,0,len);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
    //初始化控件
    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mLastTimeTV = (TextView)findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
    }
    public  void update(){
       onCreate(null);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this,VirusScanSpeedActivity.class));
                break;
        }
    }
}

