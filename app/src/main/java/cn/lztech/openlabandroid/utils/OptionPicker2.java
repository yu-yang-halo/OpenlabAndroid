package cn.lztech.openlabandroid.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by Administrator on 2016/3/21.
 */
public class OptionPicker2 extends OptionPicker {


    /**
     * Instantiates a new Option picker.
     *
     * @param activity the activity
     * @param options  the options
     */
    public OptionPicker2(Activity activity, String[] options) {
        super(activity, options);
    }

    @Override
    protected void onCancel() {
        if(onMyCancelListener!=null){
            onMyCancelListener.onMyCancel();
        }
    }

    private OnMyCancelListener onMyCancelListener;

    public OnMyCancelListener getOnMyCancelListener() {
        return onMyCancelListener;
    }

    public void setOnMyCancelListener(OnMyCancelListener onMyCancelListener) {
        this.onMyCancelListener = onMyCancelListener;
    }

    /**
     * The interface On option pick listener.
     */
    public interface OnMyCancelListener {


        void onMyCancel();

    }




}
