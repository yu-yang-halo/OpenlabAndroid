package cn.lztech.openlabandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.elnet.andrmb.bean.UserType;
import cn.lztech.openlabandroid.AboutActivity;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.ScoreActivity;
import cn.lztech.openlabandroid.user.FindPassActivity;

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
                Intent intent=new Intent(getActivity(),FindPassActivity.class);
                intent.putExtra(FindPassActivity.KEY_FIND_PWD_TYPE,FindPassActivity.VALUE_FIND_PWD_TYPE_LOGINED);
                getActivity().startActivity(intent);
            }
        });
        List<Map<String,String>> data=new ArrayList<>();
        String[] titles=new String[]{"关于我们"};
        for(int i=0;i<titles.length;i++){
            Map<String,String> dict=new HashMap<>();
            dict.put("title",titles[i]);
            data.add(dict);
        }

        String[] from=new String[]{"title"};
        int[] to=new int[]{R.id.titleItem};
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(), data,
        R.layout.adapter_settings_item, from,  to);

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class activityClazz=null;
                if(position==0){
                    activityClazz= AboutActivity.class;
                }else{
                    activityClazz=AboutActivity.class;
                }
                Intent intent=new Intent(getActivity(),activityClazz);
                startActivity(intent);

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
