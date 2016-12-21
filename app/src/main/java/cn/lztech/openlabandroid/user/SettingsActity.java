package cn.lztech.openlabandroid.user;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.StatusBarActivity;
import cn.lztech.openlabandroid.cache.ContentBox;
import cn.lztech.openlabandroid.utils.RegexUtils;

/**
 * Created by Administrator on 2016/12/14.
 */

public class SettingsActity extends StatusBarActivity {

    EditText ipEditText;
    EditText portEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        initCustomActionBar();

        ipEditText= (EditText) findViewById(R.id.editText);
        portEditText= (EditText) findViewById(R.id.editText2);





        String[] ipPortArr=IpConfigHelper.fetchIpAddrPortArr(this);


        String ipAddrStr=ipPortArr[0];
        String portStr  =ipPortArr[1];



        ipEditText.setText(ipAddrStr);
        portEditText.setText(portStr);

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
        tvTitle.setText("服务配置");


        registerBtn.setVisibility(View.VISIBLE);
        registerBtn.setText("保存");
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ipAddr=ipEditText.getText().toString();
                String portStr=portEditText.getText().toString();

                if(!RegexUtils.isIPAddress(ipAddr)&&!RegexUtils.isDomain(ipAddr)){
                    showMessage("不合法的ip地址或域名");
                    return;
                }
                if(!RegexUtils.isNumber(portStr)){
                    showMessage("不合法的端口");
                    return;
                }

                IpConfigHelper.saveIpAddrPortArr(SettingsActity.this,ipAddr,portStr);

                showMessage("保存成功");

                finish();



            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        return true;
    }

}
