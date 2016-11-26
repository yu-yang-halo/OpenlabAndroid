package cn.lztech.openlabandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import cn.elnet.andrmb.elconnector.ErrorCode;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.elnet.andrmb.elconnector.util.IWSErrorCodeListener;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.lztech.openlabandroid.user.LoginActivity;
import im.fir.sdk.FIR;

/**
 * Created by Administrator on 2016/3/25.
 */
public class MyApplication extends Application {
    private Activity currentActivity;
    private boolean isBackground=false;
    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.err.println("Semester"+WSConnector.getInstance().getSemesterList());
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        FIR.init(this);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        setStyleBasic();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("ActivityLifecycle","onActivityCreated"+"  "+activity.getClass());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("ActivityLifecycle","onActivityStarted"+"  "+activity.getClass());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("ActivityLifecycle","onActivityResumed"+"  "+activity.getClass());
                isBackground=false;
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("ActivityLifecycle","onActivityPaused"+"  "+activity.getClass());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("ActivityLifecycle","onActivityStopped"+"  "+activity.getClass());
                if(currentActivity==activity){
                    Log.e("ActivityLifecycle","进入后台");
                    isBackground=true;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("ActivityLifecycle","onActivitySaveInstanceState"+"  "+activity.getClass());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("ActivityLifecycle","onActivityDestroyed"+"  "+activity.getClass());
            }
        });

        WSConnector.getInstance().setWSErrorCodeListener(new IWSErrorCodeListener() {
            @Override
            public void handleErrorCode(ErrorCode errorCode) {
                Message msg=new Message();
                msg.what=errorCode.getCode();
                myHandler.sendMessage(msg);
            }

            @Override
            public void handleMessage(String message) {
                Message msg=new Message();
                msg.what=999999;
                Bundle bundle=new Bundle();
                bundle.putString("message",message);
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        });
    }

    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(ErrorCode.SEC_TOKEN_EXPRIED.getCode()==msg.what
                    ||ErrorCode.USER_ALREADY_LOGOUT.getCode()==msg.what
                    ||ErrorCode.INVALID_SEC_TOKEN.getCode()==msg.what){
                new AlertDialog.Builder(currentActivity)
                        .setTitle("提示")
                        .setMessage("登录超时，请重新登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }).show();
            }else if(msg.what==999999){
                String message=msg.getData().getString("message");
                Toast.makeText(currentActivity,message,Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     *设置通知提示方式 - 基础属性
     */
    private void setStyleBasic(){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
       // builder.notificationDefaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
        JPushInterface.setPushNotificationBuilder(1, builder);
    }


    /**
     *设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom(){
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(getApplicationContext(),R.layout.customer_notitfication_layout,R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.ic_launcher;
        builder.developerArg0 = "developerArg2";
        // 指定定制的 Notification Layout
        builder.statusBarDrawable = R.mipmap.ic_launcher;

        JPushInterface.setPushNotificationBuilder(2, builder);

    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public boolean isBackground() {
        return isBackground;
    }
}
