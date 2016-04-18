package cn.lztech.openlabandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.elnet.andrmb.bean.Constants;
import cn.elnet.andrmb.bean.LabInfoType;
import cn.elnet.andrmb.bean.ReservationType;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.adapter.MyOrderAdapter;

/**
 * Created by Administrator on 2016/3/30.
 */
public class OrderClassFragment extends IOrderStatusFragment implements MainActivity.DataCallBackProtocol  {
    private int type;
    ListView listView;
    MyOrderAdapter myOrderAdapter;
    MainActivity mainActivity;
    PullRefreshLayout swipeRefreshLayout;
    TextView list_empty_description;


    public static OrderClassFragment getInstance(int type){
        OrderClassFragment fragment=new OrderClassFragment();
        fragment.type=type;
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
        mainActivity.addDataCallBackProtocol(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fr_orderclass, null);

        swipeRefreshLayout= (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        list_empty_description= (TextView) view.findViewById(R.id.list_empty_description);

        listView= (ListView) view.findViewById(R.id.listView);

        myOrderAdapter=new MyOrderAdapter(getActivity(),type);
        listView.setAdapter(myOrderAdapter);
        listView.setDividerHeight(20);


        // listen refresh event
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                mainActivity.dataRequest();

            }
        });



        return view;
    }



    @Override
    public void onDataComplete(List<ReservationType> reservationTypeList, List<LabInfoType> labInfoTypes) {
         if(swipeRefreshLayout!=null){
             swipeRefreshLayout.setRefreshing(false);
         }


         if(reservationTypeList==null||labInfoTypes==null){
             return;
         }


        List<ReservationType> temp=new ArrayList<ReservationType>();

        for (ReservationType reservationType:reservationTypeList){
            if(reservationType.getStatus()==type){
                temp.add(reservationType);
            }
        }

        if(temp==null||temp.size()<=0){
            list_empty_description.setVisibility(View.VISIBLE);

            String desc=Constants.getEmptyDescriptionMap().get(type);
            if(desc!=null){
                list_empty_description.setText(desc);
            }else{
                list_empty_description.setText("还没有任何数据");
            }


        }else{
            list_empty_description.setVisibility(View.GONE);
        }
        myOrderAdapter.setReservationTypes(temp);
        myOrderAdapter.setLabInfoTypes(labInfoTypes);
        myOrderAdapter.setMainActivity(mainActivity);

        myOrderAdapter.notifyDataSetChanged();


    }
}
