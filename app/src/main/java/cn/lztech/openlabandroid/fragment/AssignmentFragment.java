package cn.lztech.openlabandroid.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

import java.util.ArrayList;
import java.util.List;

import cn.elnet.andrmb.bean.AssignmentType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.UploadAssignmentActivity;
import cn.lztech.openlabandroid.adapter.AssignmentAdapter;
import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Administrator on 2016/4/5.
 */
public class AssignmentFragment extends Fragment {
    ListView assignmentListView;
    TextView emptyDescView;
    List<AssignmentType> assignmentTypes=new ArrayList<AssignmentType>();
    PullRefreshLayout swipeRefreshLayout;
    MainActivity mainActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_assignment,null);

        assignmentListView= (ListView) view.findViewById(R.id.assignmentListView);
        emptyDescView= (TextView) view.findViewById(R.id.emptyDescView);
        swipeRefreshLayout= (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        emptyDescView.setVisibility(View.GONE);


        for (int i=0;i<3;i++){
            AssignmentType type=new AssignmentType(-1,"科学实验"+(i+1),"请提交实验报告材料",
                    "2016-05-28", "2016-05-21", -1);
            assignmentTypes.add(type);
        }


        // listen refresh event
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });




        AssignmentAdapter adapter=new AssignmentAdapter(assignmentTypes,getActivity().getApplicationContext());

        assignmentListView.setAdapter(adapter);
        assignmentListView.setDividerHeight(10);

        assignmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent intent=new Intent(getActivity(),UploadAssignmentActivity.class);
                startActivity(intent);


            }
        });


        return view;
    }




    class AssignmentTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            return null;
        }
    }



}
