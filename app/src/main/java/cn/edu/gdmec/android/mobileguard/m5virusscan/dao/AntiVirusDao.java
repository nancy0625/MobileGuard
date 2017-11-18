package cn.edu.gdmec.android.mobileguard.m5virusscan.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by asus on 2017/11/15.
 */

public class AntiVirusDao {
    /*
   检查某个MD5是否是病毒，
   * */
    private static Context context;
    private static String dbname;
    public AntiVirusDao(Context context) {
        this.context=context;
        dbname = "/data/data/"+context.getPackageName()+"/files/antivirus.db";

    }


    //使用apk文件的MD5值匹配病毒的数据库
    public static String checkVirus(String md5){
        String desc = null;
        //打开病毒的数据库
       Log.i(context.getPackageName(),"地址错误");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbname,null,
                SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select desc from datable where md5=?",new String[]{ md5 });



        if (cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }
    public static String checkVersion(){

        String major = null;
        String minor = null;
        String build = null;

        //打开病毒的数据库
        //Log.i(context.getPackageName(),"地址错误");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbname,null,
                SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select * from version ",null);

        if (cursor.moveToNext()){
            major = cursor.getString(0);
            minor = cursor.getString(1);
            build = cursor.getString(2);
        }
        cursor.close();
        db.close();
       String  version = major+"." + minor +"."+ build;
        return version;
    }
}
