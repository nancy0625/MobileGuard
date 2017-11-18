package cn.edu.gdmec.android.mobileguard.m5virusscan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.MyUtils;
import cn.edu.gdmec.android.mobileguard.m5virusscan.utils.VersionUpdateUtils;


public class VirusScanActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mLastTimeTV;
    private TextView mtvversion;
    private SharedPreferences mSP;
    private String mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_virus_scan);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        copyDB("antivirus.db");
        initView();
        mVersion = MyUtils.getVersion(getApplicationContext());
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(mVersion,VirusScanActivity.this);

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
        mtvversion.setText("病毒数据库版本："+mVersion);
        super.onResume();
    }

    /**
     * 复制病毒库
     * @param
     */
    private void  copyDB(final String dbname){
        new Thread(){
            public void run(){

                try {
                   File file = new File(getFilesDir(),dbname);
                    if (file.exists() && file.length()>0){
                        Log.i("VirusScanActivity","数据库已存在");
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

    /**
     * 初始化控件
     *
     */
    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mLastTimeTV = (TextView)findViewById(R.id.tv_lastscantime);
        mtvversion = (TextView)findViewById(R.id.tv_version);
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

