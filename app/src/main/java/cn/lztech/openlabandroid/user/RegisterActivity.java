package cn.lztech.openlabandroid.user;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

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
    EditText  phoneEditText;
    EditText  vcodeEdit;

    Button  registerBtn;
    Button  vcodeBtn;
    int wait_time;
    private static final  String VCODE_CONTENT="获取验证码";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initCustomActionBar();


        nameEdit= (EditText) findViewById(R.id.nameEditText);
        passEdit= (EditText) findViewById(R.id.passEditText);
        repassEdit= (EditText) findViewById(R.id.repassEditText);
        phoneEditText= (EditText) findViewById(R.id.phoneEditText);

        vcodeEdit= (EditText) findViewById(R.id.vcodeEdit);

        registerBtn= (Button) findViewById(R.id.registerBtn);
        vcodeBtn=(Button)findViewById(R.id.button6);
        vcodeBtn.setText(VCODE_CONTENT);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEdit.getText().toString();
                String pass=passEdit.getText().toString();
                String repass=repassEdit.getText().toString();
                String phone=phoneEditText.getText().toString();
                String vcode=vcodeEdit.getText().toString();

                if(name.trim().equals("")
                        ||pass.trim().equals("")
                        ||repass.trim().equals("")
                        ||phone.trim().equals("")
                        ||vcode.trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }else if(!RegexUtils.isVaildLoginName(name)){
                    Toast.makeText(RegisterActivity.this,"请输入正确的学生号",Toast.LENGTH_SHORT).show();
                }else if(!RegexUtils.isMobileNO(phone)){
                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else if(!RegexUtils.isVaildPass(pass)){
                    Toast.makeText(RegisterActivity.this,"密码由6-22位的数字、字符组成",Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(repass)){
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }else{

                    UserType userType=new UserType(0,name,pass,"",phone,null,null,null,null);
                    userType.setUserRole("student");
                    userType.setVcode(vcode);

                    new RegisterTask(RegisterActivity.this,userType).execute();
                }
            }
        });

        vcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone=phoneEditText.getText().toString();
                if(!RegexUtils.isMobileNO(phone)){
                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WSConnector.getInstance().sendShortMsgCode(phone,0);
                        } catch (WSException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                vcodeBtn.setClickable(false);
                wait_time=40;
                final Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(wait_time<0){
                            timer.cancel();
                            return;
                        }
                        Message msg=new Message();
                        msg.what=wait_time;
                        mhandler.sendMessage(msg);
                        wait_time--;
                    }
                },0,1000);

            }
        });

    }

    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what<=0){
                vcodeBtn.setText(VCODE_CONTENT);
                vcodeBtn.setClickable(true);
            }else{
                vcodeBtn.setText(msg.what+"s");
            }
        }
    };



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

                boolean isRight=WSConnector.getInstance().verificationCode(userType.getPhone(),userType.getVcode());
                if(isRight){
                    WSConnector.getInstance().createUser(userType);
                }else{
                    return "验证码错误";
                }

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
