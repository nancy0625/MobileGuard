package cn.edu.gdmec.android.mobileguard.m2home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**，
 * Created by acer on 2017/10/14.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        currectSIM();
    }

    public void currectSIM() {
        //检查SIM卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取防盗保护状态
        boolean protecting=sp.getBoolean("protecting",true);
        if(protecting){
            //得到绑定的SIM卡串号
            String bindsim = sp.getString("sim","");

            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //为了
            String realsim=tm.getSimSerialNumber();
            if (bindsim.equals(realsim)){
                Log.i("","sim卡未发生变化，还是您的手机");

            }else{
                Log.i("","SIM卡变化了");
                //
                String safenumber=sp.getString("safephone","");
                if(!TextUtils.isEmpty(safenumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber,null,"你的亲友手机的sim卡已经被更换！",null,null);
                }
            }

        }
    }
}
