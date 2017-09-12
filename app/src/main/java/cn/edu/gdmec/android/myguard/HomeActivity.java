package cn.edu.gdmec.android.myguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

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
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗

                }
            }
        });
    }

}
