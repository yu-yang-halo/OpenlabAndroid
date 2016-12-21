package cn.lztech.openlabandroid.user;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashSet;
import java.util.Set;

import cn.elnet.andrmb.bean.UserType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.elnet.andrmb.elconnector.util.MD5Generator;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.StatusBarActivity;
import cn.lztech.openlabandroid.cache.ContentBox;
import cn.lztech.openlabandroid.fir.FirManagerService;

import static cn.lztech.openlabandroid.R.color.theme_color;

public class LoginActivity extends StatusBarActivity {
    Button regBtn;
    Button loginBtn;
    Button findPassBtn;
    EditText usernameEdit;
    EditText passwordEdit;
    UserType userType;

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initCustomActionBar();



        regBtn= (Button) findViewById(R.id.regBtn);
        loginBtn= (Button) findViewById(R.id.loginBtn);
        findPassBtn= (Button) findViewById(R.id.button8);

        usernameEdit= (EditText) findViewById(R.id.userNameEditText);
        passwordEdit= (EditText) findViewById(R.id.passwordEditText);

        TextView versionTV= (TextView) findViewById(R.id.textView16);


        versionTV.setText("v"+FirManagerService.getVersionInfo(this).versionName);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdit.getText().toString();
                String password=passwordEdit.getText().toString();

                if(!username.trim().equals("")&&!password.trim().equals("")){
                    new LoginTask(LoginActivity.this,username,password).execute();
                }else{
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        findPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,FindPassActivity.class);
                startActivity(intent);
            }
        });

        usernameEdit.setText(ContentBox.getValueString(this, ContentBox.KEY_USERNAME, ""));
        passwordEdit.setText(ContentBox.getValueString(this, ContentBox.KEY_PASSWORD, ""));

        FirManagerService.checkUpdate(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        String[] arr=IpConfigHelper.fetchIpAddrPortArr(this);


        String ipAddrStr=arr[0];
        String portStr  =arr[1];

        WSConnector.getInstance(ipAddrStr,portStr,false);
    }

    private boolean initCustomActionBar() {
        ActionBar  mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.top_back_center_bar);
        TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        Button registerBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.GONE);
        tvTitle.setText("科大开放实验室");


        registerBtn.setVisibility(View.GONE);
        registerBtn.setText("配置");
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SettingsActity.class);
                startActivity(intent);
            }
        });

        return true;
    }


    class LoginTask extends AsyncTask<String, String, String>{
        String loginName;
        String password;
        Context ctx;
        KProgressHUD progressHUD;
        LoginTask(Context ctx, String loginName, String password){
            this.loginName=loginName;
            this.password=password;
            this.ctx=ctx;
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                WSConnector.getInstance().login(loginName,password);
                userType=WSConnector.getInstance().getUser();
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
                    .setLabel("登录中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressHUD.dismiss();
            if(result==null){

                if(userType!=null) {
                    ContentBox.loadString(LoginActivity.this,
                            ContentBox.KEY_REALNAME, userType.getRealName());
                }


                String userId=WSConnector.getInstance().getUserMap().get("userId");
                Set<String> tags=new HashSet<String>();

                tags.add(userId);

                JPushInterface.setAliasAndTags(getApplicationContext(), null, tags, new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                       //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                    }
                });
                ContentBox.loadString(ctx,ContentBox.KEY_USERNAME,loginName);
                ContentBox.loadString(ctx,ContentBox.KEY_PASSWORD,password);
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
            }
        }

    }


}
