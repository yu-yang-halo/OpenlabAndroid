package cn.lztech.openlabandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

import cn.elnet.andrmb.bean.Constants;
import cn.elnet.andrmb.bean.ReservationType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.utils.TabEntity;
import cn.lztech.openlabandroid.utils.ViewFindUtils;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MyOrderFragment extends IOrderStatusFragment {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private View mDecorView;
    private CommonTabLayout mTabLayout_1;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.from(getActivity()).inflate(R.layout.fr_myorder,null);
        mFragments.add(OrderClassFragment.getInstance(Constants.STATUS_NORMAL));
        mFragments.add(OrderClassFragment.getInstance(Constants.STATUS_ACTIVE));
        mFragments.add(OrderClassFragment.getInstance(Constants.STATUS_CANCEL));
        mFragments.add(OrderClassFragment.getInstance(Constants.STATUS_EXPIRED));
        mFragments.add(OrderClassFragment.getInstance(Constants.STATUS_USED));


        mDecorView = getActivity().getWindow().getDecorView();

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], R.drawable.icon_arrow,R.drawable.icon_arrow));
        }

        /** with nothing */
        mTabLayout_1 = (CommonTabLayout) view.findViewById(R.id.sl_1);
        mTabLayout_1.setTabData(mTabEntities, getActivity(), R.id.currentPageFragment2, mFragments);


        return view;
    }

}
