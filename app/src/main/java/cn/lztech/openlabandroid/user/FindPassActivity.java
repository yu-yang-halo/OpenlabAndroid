package cn.lztech.openlabandroid.user;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import cn.elnet.andrmb.bean.Constants;
import cn.elnet.andrmb.bean.ReservationType;
import cn.elnet.andrmb.bean.UserType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.adapter.MyOrderAdapter;
import cn.lztech.openlabandroid.utils.RegexUtils;
import cn.lztech.openlabandroid.utils.TimeUtils;

/**
 * Created by Administrator on 2016/9/18.
 */
public class FindPassActivity extends FragmentActivity {
    public static  String KEY_FIND_PWD_TYPE="key_find_pwd";
    public static  int VALUE_FIND_PWD_TYPE_LOGINED=1000;
    public static  int VALUE_FIND_PWD_TYPE_UNLOGIN=1001;
    private  EditText numberEditText;
    private  EditText phoneEditText;
    private  EditText vcodeEdit;
    private  EditText passwordEditText;
    private  EditText passwordEditText2;
    private  Button   registerBtn;

    private  Button vcodeBtn;
    int wait_time;
    private static final  String VCODE_CONTENT="获取验证码";
    private int value_find_pwd_type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value_find_pwd_type=getIntent().getIntExtra(KEY_FIND_PWD_TYPE,VALUE_FIND_PWD_TYPE_UNLOGIN);

        if(value_find_pwd_type==VALUE_FIND_PWD_TYPE_LOGINED){
            setContentView(R.layout.ac_findpass2);
        }else{
            setContentView(R.layout.ac_findpass);

            numberEditText=(EditText)findViewById(R.id.phoneEditText4);
            phoneEditText=(EditText)findViewById(R.id.phoneEditText);
            vcodeEdit= (EditText) findViewById(R.id.vcodeEdit);
            vcodeBtn=(Button)findViewById(R.id.button6);
            vcodeBtn.setText(VCODE_CONTENT);
            vcodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String phone=phoneEditText.getText().toString();
                    if(!RegexUtils.isMobileNO(phone)){
                        Toast.makeText(FindPassActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
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


        initCustomActionBar();


        passwordEditText= (EditText) findViewById(R.id.passwordEditText);
        passwordEditText2= (EditText) findViewById(R.id.passwordEditText2);

        registerBtn=(Button)findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass=passwordEditText.getText().toString();
                String repass=passwordEditText2.getText().toString();
                if(value_find_pwd_type==VALUE_FIND_PWD_TYPE_UNLOGIN){
                    String loginName=numberEditText.getText().toString();
                    String vcode=vcodeEdit.getText().toString();

                    String phone=phoneEditText.getText().toString();
                    if(loginName.trim().equals("")
                            ||vcode.trim().equals("")
                            ||newpass.trim().equals("")
                            ||phone.trim().equals("")
                            ){
                        Toast.makeText(FindPassActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    }else if(!RegexUtils.isMobileNO(phone)){
                        Toast.makeText(FindPassActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    }else if(!RegexUtils.isVaildLoginName(loginName)){
                        Toast.makeText(FindPassActivity.this,"请输入正确的学生号",Toast.LENGTH_SHORT).show();
                    }else if(!RegexUtils.isVaildPass(newpass)){
                        Toast.makeText(FindPassActivity.this,"密码由6-22位的数字、字符组成",Toast.LENGTH_SHORT).show();
                    }else if(!newpass.equals(repass)){
                        Toast.makeText(FindPassActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                    }else{

                        UserType userType=new UserType(0,loginName,newpass,null,phone,null,null,null,null);
                        userType.setVcode(vcode);

                        new UpdateUserTask(FindPassActivity.this,userType).execute();

                    }
                }else{
                    if(!RegexUtils.isVaildPass(newpass)){
                        Toast.makeText(FindPassActivity.this,"密码由6-22位的数字、字符组成",Toast.LENGTH_SHORT).show();
                    }else if(!newpass.equals(repass)){
                        Toast.makeText(FindPassActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                    }else{

                        UserType userType=new UserType(0,null,newpass,null,null,null,null,null,null);


                        new UpdateUserTask(FindPassActivity.this,userType).execute();

                    }
                }

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
        tvTitle.setText("修改密码");
        registerBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }
    class UpdateUserTask extends AsyncTask<String, String, String> {
        UserType userType;
        Context ctx;
        KProgressHUD progressHUD;
        UpdateUserTask(Context ctx,UserType userType){
            this.userType=userType;
            this.ctx=ctx;
        }
        @Override
        protected String doInBackground(String... params) {

            try {

                WSConnector.getInstance().updateUser(userType.getName(),userType.getPhone(),userType.getPassword(),userType.getVcode());

            } catch (WSException e) {
                return e.getErrorMsg();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressHUD=KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("密码修改中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressHUD.dismiss();
            if(result==null){

                if(value_find_pwd_type==VALUE_FIND_PWD_TYPE_LOGINED){
                    new AlertDialog.Builder(ctx)
                            .setTitle("提示")
                            .setMessage("密码修改成功,请重新登录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(FindPassActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }).setCancelable(false).show();

                }else{
                    finish();
                }


            }else{
                Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
            }
        }

    }
}
