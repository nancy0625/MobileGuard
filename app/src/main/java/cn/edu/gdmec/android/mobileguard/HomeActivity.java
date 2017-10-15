package cn.edu.gdmec.android.mobileguard;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.m1home.adapter.HomeAdapter;
import cn.edu.gdmec.android.mobileguard.m2home.LostFindActivity;
import cn.edu.gdmec.android.mobileguard.m2home.SetUp1Activity;
import cn.edu.gdmec.android.mobileguard.m2home.dialog.InterPasswordDialog;
import cn.edu.gdmec.android.mobileguard.m2home.dialog.SetUpPasswordDialog;
import cn.edu.gdmec.android.mobileguard.m2home.utils.MD5Utils;

/**
 * Created by asus on 2017/9/11.
 */

public class HomeActivity extends Activity {
    private long mExitTime;
    //声明GridView该控件类似于ListView 列表
    private GridView gv_home;
//测试
    /** 存储手机防盗密码的sp     */
     private SharedPreferences msharedPtrferences;
    /** 设备管理员*/
    private DevicePolicyManager policyManager;
    /** 申请权限*/
    private ComponentName componentName;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        msharedPtrferences = getSharedPreferences("config",MODE_PRIVATE);

        //初始化GridView
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));

        //设置条目的点击事件
     gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         //parent代表gridView,view代表每一个条目的view对象，position代表每个条目的位置
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             System.out.print(position);
             switch (position){
                 case 0://手机防盗
                     if(isSetUpPassword()){
                         //弹出输入密码提示框
                         showInterPswdDialog();
                     }else{
                         //弹出设置密码对话框
                         showSetUpPswdDialog();
                     }
                     break;
                /* case 1://通讯卫士
                     startActivity(SecurityPhoneActivity.class);
                     break;
                 case 2://软件管家
                     startActivity(AppManagerActivity.class);
                     break;
                 case 3://病毒查杀
                     startActivity(VirusScanActivity.class);
                     break;
                 case 4://缓存清理
                     startActivity(CacheClearListActivity.class);
                     break;
                 case 5://进程管理
                     startActivity(ProcessManagerActivity.class);
                     break;
                 case 6://流量统计
                     startActivity(TrafficMonitoringActivity.class);
                     break;
                 case 7://高级工具
                     startActivity(AdvancedToolsActivity.class);
                     break;
                 case 8://设置中心
                     startActivity(SettingsActivity.class);
                     break;*/


             }
         }
     });


    }


    //设置密码输入框
    private void showSetUpPswdDialog(){
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(HomeActivity.this);
       setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack() {
           @Override
           public void ok() {
               String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();
               String affirmPwsd = setUpPasswordDialog.mAffirmET.getText().toString().trim();
               if(!TextUtils.isEmpty(firstPwsd)&&!TextUtils.isEmpty(affirmPwsd)){
                   if(firstPwsd.equals(affirmPwsd)){
                       //两次密码不一致
                       savePswd(affirmPwsd);
                      setUpPasswordDialog.dismiss();
                       showInterPswdDialog();
                   }else{
                      Toast.makeText(HomeActivity.this,"两次密码不一致",Toast.LENGTH_LONG).show();
                   }
               }else{
                   Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void cancel() {
                setUpPasswordDialog.dismiss();
           }
       });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }
    private void showInterPswdDialog(){
        final String password =getPassword();
        final InterPasswordDialog mInPswdDialog = new InterPasswordDialog(HomeActivity.this);
        mInPswdDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if (TextUtils.isEmpty(mInPswdDialog.getPassword())){
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();

                }else if (password.equals(MD5Utils.encode(mInPswdDialog.getPassword()))){
                    //进入防盗页面
                    mInPswdDialog.dismiss();
                    startActivity(LostFindActivity.class);
                    //Toast.makeText(HomeActivity.this,"可以进入手机防盗模块",Toast.LENGTH_LONG).show();

                }else {
                    //对话框消失
                    mInPswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this,"密码错误，请重新输入",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void cancel() {
                mInPswdDialog.dismiss();
            }
        });
        mInPswdDialog.setCancelable(true);
        //让对话框显示
        mInPswdDialog.show();
    }

    /**
     * 保存密码
     * @param affirmPwsd
     */
    private void savePswd(String affirmPwsd){
        SharedPreferences.Editor edit = msharedPtrferences.edit();
        edit.putString("PhoneAntiTheftPWD",MD5Utils.encode(affirmPwsd));
        edit.commit();
    }

    /**
     * 获取密码
     * @return sp 存储的密码
     */
    private String getPassword(){
        String password = msharedPtrferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)){
            return "";
        }
        return password;
    }

    /**
     * 判断用户是否设置过手机防盗密码
     *
     */
    private boolean isSetUpPassword(){
        String password = msharedPtrferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)){
            return false;
        }
        return true;
    }
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);

    }

    /**
     * 开启新的Activity不关闭自己
     * @param //cls,新的Activity的字节码
     */
   /* public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);
    }
    /**
     * 按两次返回退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-mExitTime)<2000){
                System.exit(0);
            }else{
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return  super.onKeyDown(keyCode,event);
    }


}
