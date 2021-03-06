package cn.lztech.openlabandroid;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.elnet.andrmb.bean.LabInfoType;
import cn.elnet.andrmb.bean.ReservationType;
import cn.elnet.andrmb.bean.UserType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.cache.ContentBox;
import cn.lztech.openlabandroid.fragment.AssignmentFragment;
import cn.lztech.openlabandroid.fragment.HomeFragment;
import cn.lztech.openlabandroid.fragment.MyOrderFragment;
import cn.lztech.openlabandroid.fragment.SettingsFragment;
import cn.lztech.openlabandroid.utils.TabEntity;
import cn.lztech.openlabandroid.utils.ViewFindUtils;

public class MainActivity extends StatusBarActivity {
	private Context mContext = this;
	private ArrayList<Fragment> mFragments = new ArrayList<>();
	private String[] mTitles = {"首页", "我的预约", "我的作业", "个人中心"};
	private int[] mIconUnselectIds = {
			R.mipmap.home_btn_home, R.mipmap.home_btn_shop,
			R.mipmap.home_btn_active, R.mipmap.home_btn_my};
	private int[] mIconSelectIds = {
			R.mipmap.home_btn_home_sel, R.mipmap.home_btn_shop_sel,
			R.mipmap.home_btn_active_sel, R.mipmap.home_btn_my_sel};
	private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
	private View mDecorView;
	private CommonTabLayout mTabLayout_1;
    private ActionBar mActionbar;
	private TextView tvTitle;
	Button leftBtn;
	Button rightBtn;
	public RelativeLayout mainRelativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mainRelativeLayout= (RelativeLayout) findViewById(R.id.mainRelativeLayout);

		initCustomActionBar();

		mFragments.add(new HomeFragment());
		mFragments.add(new MyOrderFragment());
		mFragments.add(new AssignmentFragment());
		mFragments.add(new SettingsFragment());

		for (int i = 0; i < mTitles.length; i++) {
			mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
		}
		mDecorView = getWindow().getDecorView();

		/** with nothing */
		mTabLayout_1 = ViewFindUtils.find(mDecorView, R.id.tl_1);
		mTabLayout_1.setTabData(mTabEntities, this, R.id.currentPageFragment, mFragments);
		mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {
				setSelectPos(position);



			}

			@Override
			public void onTabReselect(int position) {
				//Toast.makeText(getApplicationContext(),"onTabReselect position:"+position,Toast.LENGTH_SHORT).show();

			}
		});
		setSelectPos(0);


		dataRequest();

	}

	public void dataRequest(){
		new GetDataTask().execute();
	}

	public void setSelectPos(int position){
		mTabLayout_1.setCurrentTab(position);
		tvTitle.setText(mTitles[position]);

		if(position==2){
			rightBtn.setVisibility(View.VISIBLE);

		}else{
			rightBtn.setVisibility(View.GONE);
		}

		if(position==1){
			dataRequest();
		}

	}
	private boolean initCustomActionBar() {
		mActionbar = getActionBar();
		if (mActionbar == null) {
			return false;
		}
		mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionbar.setDisplayShowCustomEnabled(true);
		mActionbar.setCustomView(R.layout.top_back_center_bar);
		tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);

		rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
		leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
		rightBtn.setVisibility(View.GONE);
		leftBtn.setVisibility(View.GONE);



		return true;
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	class GetDataTask extends AsyncTask<String,String,String>{
		List<ReservationType> reservationTypeList;
		List<LabInfoType> labInfoTypes;
		UserType userType;
		@Override
		protected String doInBackground(String... params) {
			String loginName=WSConnector.getInstance().getUserMap().get("loginName");

			try {
				reservationTypeList=WSConnector.getInstance().getReservationList(loginName);
				labInfoTypes=WSConnector.getInstance().getLabListByIncDesk(false);
				userType=WSConnector.getInstance().getUser();


			} catch (WSException e) {
				return e.getErrorMsg();
			}


			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			if(s==null){
				if(userType!=null){

					if(userInfoProtocol!=null){
						userInfoProtocol.onDataComplete(userType);
					}
				}
				for (DataCallBackProtocol protocol:dataCallbacks){
					protocol.onDataComplete(reservationTypeList,labInfoTypes);
				}

			}else {
				Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
			}

		}
	}



	public void setUserInfoProtocol(UserInfoProtocol userInfoProtocol) {
		this.userInfoProtocol = userInfoProtocol;
	}

	public void addDataCallBackProtocol(DataCallBackProtocol dataCallback){
		this.dataCallbacks.add(dataCallback);
	}


	public interface DataCallBackProtocol{
		public void onDataComplete(List<ReservationType> reservationTypeList,List<LabInfoType> labInfoTypes);
	}

	public interface UserInfoProtocol{
		public void onDataComplete(UserType userType);
	}

	private Set<DataCallBackProtocol> dataCallbacks=new HashSet<DataCallBackProtocol>();
	private UserInfoProtocol userInfoProtocol;

}
