package com.moliying.jzc.bookstore;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.moliying.jzc.bookstore.ui.BaseActivity;

import java.util.LinkedList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by Jzc on 2016/8/15.
 */
public class App extends Application{
    public static Context mContext;
    public static LinkedList<BaseActivity> mActivities = new LinkedList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Fresco.initialize(mContext);
        Bmob.initialize(mContext, "303a075281df5463c49a5ab99fe18ca2");
//        第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId("303a075281df5463c49a5ab99fe18ca2")
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }
    public static void release(){
        mActivities = null;
        mContext = null;
    }
}
