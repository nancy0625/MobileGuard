package cn.edu.gdmec.android.mobileguard.m2home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lidroid.xutils.cache.LruDiskCache;

import cn.edu.gdmec.android.mobileguard.R;

/**
 * Created by asus on 2017/10/13.
 */

public class SetUp2Activity extends BaseSetUpActivity implements View.OnClickListener{
    private TelephonyManager mTelephonyManager;
    private Button mBindSIMBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
       // mTelephonyManager = (TelephonyManager)getSystemService(TELECOM_SERVICE);
        initView();
    }
    private void initView(){
        //设置第二个小圆点的颜色l
        ((RadioButton)findViewById(R.id.rb_second)).setChecked(true);
       /* mBindSIMBtn = (Button)findViewById(R.id.btn_bind_sim);
        mBindSIMBtn.setOnClickListener(this);
        if (isBind()){
            mBindSIMBtn.setEnabled(false);
        }else {
            mBindSIMBtn.setEnabled(true);
        }  */
    }
    private boolean isBind(){
        String sinString = sp.getString("sim",null);
        if (TextUtils.isEmpty(sinString)){
            return false;
        }
        return false;
    }
    @Override
    public void showNext() {
      /* if (!isBind()){
            Toast.makeText(this,"您还没有绑定SIM卡",Toast.LENGTH_SHORT).show();
            return;
        }*/
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bind_sim:
                //绑定SIM卡
                bindSIM();
                break;
        }
    }
    /*
    绑定Sim卡
     */
    private void bindSIM(){
        if(!isBind()){
            String simSerialNumber = mTelephonyManager.getSimSerialNumber();
           SharedPreferences.Editor edit = sp.edit();
            edit.putString("sim",simSerialNumber);
            edit.commit();
            Toast.makeText(this,"SIM卡绑定成功",Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }else{
            //已经绑定的用户
            Toast.makeText(this,"SIM卡已经绑定",Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }
    }
}
