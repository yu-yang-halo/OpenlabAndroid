package cn.lztech.openlabandroid.message;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.lztech.openlabandroid.R;


/**
 * Created by Administrator on 2016/3/28.
 */
public class DialogManagerUtils {
    public static void showMessage(Context ctx,String title,String message,DialogInterface.OnClickListener listener){

        new  AlertDialog.Builder(ctx)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", listener)
                        .setCancelable(false)
                        .show();

    }
    public static void showMessageAndCancel(Context ctx,String title,String message,DialogInterface.OnClickListener listener){

        new  AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();

    }



    public static void showMyToast(Context ctx,String message, final int cnt) {
        final Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_alert,null);
        TextView messageTxt= (TextView) view.findViewById(R.id.messageTxt);
        messageTxt.setText(message);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER,0,0);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);

    }


    public static void showToast(Context ctx,String message){
        Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_LONG);
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_alert,null);
        TextView messageTxt= (TextView) view.findViewById(R.id.messageTxt);
        messageTxt.setText(message);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
