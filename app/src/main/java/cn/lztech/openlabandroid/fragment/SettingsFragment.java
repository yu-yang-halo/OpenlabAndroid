package cn.lztech.openlabandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.elnet.andrmb.bean.UserType;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;

/**
 * Created by Administrator on 2016/4/1.
 */
public class SettingsFragment extends Fragment implements MainActivity.UserInfoProtocol{
    ListView listView;
    Button realNameButton;
    TextView accountNameTxt;
    TextView phoneTxt;
    MainActivity mainActivity;
    RelativeLayout infoLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
        mainActivity.setUserInfoProtocol(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fr_settings, null);
        listView= (ListView) view.findViewById(R.id.listView2);
        realNameButton= (Button) view.findViewById(R.id.button4);
        accountNameTxt= (TextView) view.findViewById(R.id.textView11);
        phoneTxt= (TextView) view.findViewById(R.id.textView10);
        infoLayout= (RelativeLayout) view.findViewById(R.id.relativeLayout6);

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onDataComplete(UserType userType) {
        realNameButton.setText(userType.getRealName());
        accountNameTxt.setText(userType.getName());
        phoneTxt.setText("手机号:"+userType.getPhone());
    }
}
