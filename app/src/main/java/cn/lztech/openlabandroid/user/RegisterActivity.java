package cn.lztech.openlabandroid.user;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;

import cn.elnet.andrmb.bean.UserType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.elnet.andrmb.elconnector.util.Util;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.cache.ContentBox;
import cn.lztech.openlabandroid.utils.RegexUtils;

public class RegisterActivity extends FragmentActivity {
    EditText  nameEdit;
    EditText  passEdit;
    EditText  repassEdit;
    EditText  realNameEdit;
    EditText  phoneEditText;

    Button  registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initCustomActionBar();


        nameEdit= (EditText) findViewById(R.id.nameEditText);
        passEdit= (EditText) findViewById(R.id.passEditText);
        repassEdit= (EditText) findViewById(R.id.repassEditText);
        realNameEdit= (EditText) findViewById(R.id.realNameEditText);
        phoneEditText= (EditText) findViewById(R.id.phoneEditText);
        registerBtn= (Button) findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEdit.getText().toString();
                String pass=passEdit.getText().toString();
                String repass=repassEdit.getText().toString();
                String realName=realNameEdit.getText().toString();
                String phone=phoneEditText.getText().toString();

                if(name.trim().equals("")
                        ||pass.trim().equals("")
                        ||repass.trim().equals("")
                        ||realName.trim().equals("")
                        ||phone.trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }else if(!RegexUtils.isVaildLoginName(name)){
                    Toast.makeText(RegisterActivity.this,"请输入正确的学生卡号",Toast.LENGTH_SHORT).show();
                }else if(!RegexUtils.isMobileNO(phone)){
                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else if(!RegexUtils.isVaildPass(pass)){
                    Toast.makeText(RegisterActivity.this,"密码由6-22位的数字、字符组成",Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(repass)){
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }else{

                    UserType userType=new UserType(0,name,pass,realName,phone,null,null,null,null);
                    userType.setUserRole("student");

                    new RegisterTask(RegisterActivity.this,userType).execute();
                }
            }
        });

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
        tvTitle.setText("注册");
        registerBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }

    class RegisterTask extends AsyncTask<String, String, String> {
        UserType userType;
        Context ctx;
        KProgressHUD progressHUD;
        RegisterTask(Context ctx,UserType userType){
            this.userType=userType;
            this.ctx=ctx;
        }
        @Override
        protected String doInBackground(String... params) {

            try {

                WSConnector.getInstance().createUser(userType);
            } catch (WSException e) {
                return e.getErrorMsg();
            } catch (UnsupportedEncodingException e) {
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressHUD=KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("注册中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressHUD.dismiss();
            if(result==null){
                Toast.makeText(ctx,"注册成功",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
            }
        }

    }
}
