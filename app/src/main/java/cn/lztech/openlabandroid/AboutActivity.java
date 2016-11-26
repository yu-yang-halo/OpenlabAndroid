package cn.lztech.openlabandroid;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/26.
 */

public class AboutActivity extends FragmentActivity {
    private  TextView versionLabel;
    private  TextView contentLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_about);

        initCustomActionBar();

        versionLabel= (TextView) findViewById(R.id.textView14);
        contentLabel=(TextView)findViewById(R.id.textView22);


        String content="开放实验室(Android)版本\n\n此版本适用于Android4.0以上的操作系统手机，如使用低于Android4.0以下版本，出现任何问题，公司不承担责任.本软件免费下载，下载过程中产生的数据流量费用由运营商收取。\n客服电话:\n0551-67122346（合肥）\n0592-5952609  （厦门）";


        contentLabel.setText(content);

        try {
            PackageInfo info=getPackageManager().getPackageInfo(getPackageName(),0);
            versionLabel.setText("版本号 "+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionLabel.setText("版本号 0.0.0");
        }


    }


    private boolean initCustomActionBar() {
        ActionBar mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.top_back_center_bar);
        TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        Button registerBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        tvTitle.setText("关于我们");
        registerBtn.setVisibility(View.GONE);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }
}
