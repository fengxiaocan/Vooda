package com.evil.check;


import android.content.Context;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * @项目名： xiaofang
 * @包名： com.xiaofang.controlsystem.utils
 * @创建者: Noah.冯
 * @时间: 17:19
 * @描述： 检测是否超时
 */

public class CheckOutTime{

    public static final long   DAY_TIME = 1000 * 60 * 60 * 24;
    public static final String webUrl1  = "http://www.bjtime.cn";//bjTime
    public static final String webUrl2  = "http://www.baidu.com";//百度
    public static final String webUrl3  = "http://www.taobao.com";//淘宝
    public static final String webUrl4  = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
    public static final String webUrl5  = "http://www.360.cn";//360
    public static final String webUrl6  = "http://www.beijing-time.org";
    //beijing-time

    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     *
     * @return
     *
     * @author SHANHY
     * @date 2015年11月27日
     */
    private static long getWebsiteDatetime(String webUrl){
        try{
            URL url = new URL(webUrl);// 取得资源对象
            HttpURLConnection uc = (HttpURLConnection) url
                    .openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            long time = date.getTime();
            uc.disconnect();
            return time;
        }catch(Exception e){
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * 检测时间是否过期
     *
     * @param outTime
     *         时间,以毫秒为单位,1000L为去1秒;
     * @param callback
     *         结果回调,如果为true,则finish所有Activity
     */
    public static void checkTime(final long outTime,
                                 final TimeOutCallback callback){
        new Thread(new Runnable(){
            @Override
            public void run(){
                //按照版本构建的时间为测试版起始点
                String buildStr = ManifestUtils
                        .getManifestString(callback.getContext(),"BUILD_TIME");
                String time = buildStr.substring(4);
                long currentTime = getWebsiteDatetime(webUrl4);
                Long buildTime = Long.valueOf(time);
                if(currentTime - buildTime > outTime || currentTime <
                        buildTime - (1000 * 60 * 60)){
                    //超过有效期
                    callback.method(true);
                }else{
                    callback.method(false);
                }
            }
        }).start();
    }

    /**
     * 检测时间是否过期
     *
     * @param outDay
     *         时间,以天为单位,1为去1天;
     * @param callback
     *         结果回调,如果为true,则finish所有Activity
     */
    public static void checkTime(final int outDay,
                                 final TimeOutCallback callback){
        new Thread(new Runnable(){
            @Override
            public void run(){
                //按照版本构建的时间为测试版起始点
                String buildStr = ManifestUtils
                        .getManifestString(callback.getContext(),"BUILD_TIME");
                String time = buildStr.substring(4);
                long currentTime = getWebsiteDatetime(webUrl4);
                Long buildTime = Long.valueOf(time);
                if(currentTime - buildTime > outDay * DAY_TIME || currentTime
                        < buildTime){
                    //超过有效期
                    callback.method(true);
                }else{
                    callback.method(false);
                }
            }
        }).start();
    }

    public interface TimeOutCallback{

        void method(Boolean t);

        Context getContext();
    }
}
