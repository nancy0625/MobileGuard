package cn.edu.gdmec.android.myguard.m1home;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.edu.gdmec.android.myguard.R;
import cn.edu.gdmec.android.myguard.m1home.adapter.HomeAdapter;

/**
 * Created by asus on 2017/9/11.
 */

public class HomeActivity extends Activity {
    private long mExitTime;
    private GridView gv_home;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        //初始化GridView
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));

        //设置条目的点击事件
     gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         //parent代表gridView,view代表每一个条目的view对象，position代表每个条目的位置
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             System.out.print(position);
             /*switch (position){
                 case 0://手机防盗
                     if(isSetUpPassword()){
                         //弹出输入密码提示框
                         showInterPswdDialog();
                     }else{
                         //弹出设置密码对话框
                         showSetUpPswdDialog();
                     }
                     break;
                 case 1://通讯卫士
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
                     break;


             }*/
         }
     });
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
