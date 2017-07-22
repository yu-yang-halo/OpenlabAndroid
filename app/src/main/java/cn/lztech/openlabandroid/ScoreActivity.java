package cn.lztech.openlabandroid;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.elnet.andrmb.bean.CourseType;
import cn.elnet.andrmb.bean.ScoreType;
import cn.elnet.andrmb.bean.SemesterType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.adapter.ListDropDownAdapter;
import cn.lztech.openlabandroid.adapter.ScoreAdapter;
import cn.lztech.openlabandroid.fragment.AssignmentFragment;

/**
 * Created by Administrator on 2016/10/20.
 */

public class ScoreActivity extends StatusBarActivity {


    private ListView scoreListView;
    /**
     * popup view
     */
    private String headers[] = {"选择学年", "选择学期"};
    private List<View> popupViews = new ArrayList<>();

    private List<String> years=new ArrayList<String>();
    private String semesters[]={"第一学期","第二学期"};
    String selectedSemester=null;
    String selectedYear=null;

    ListDropDownAdapter yearAdapter,semesterAdapter;
    DropDownMenu mDropDownMenu;

    PullRefreshLayout swipeRefreshLayout;
    int[] groupChildIndex;



    ScoreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_assignment);

        initCustomActionBar();


        mDropDownMenu= (DropDownMenu) findViewById(R.id.dropDownMenu);

        final ListView yearsView = new ListView(this);
        yearsView.setDividerHeight(0);
        yearAdapter = new ListDropDownAdapter(this, years);

        yearsView.setAdapter(yearAdapter);


        final ListView semestersView = new ListView(this);
        semestersView.setDividerHeight(0);
        semesterAdapter = new ListDropDownAdapter(this, Arrays.asList(semesters));
        semestersView.setAdapter(semesterAdapter);

        popupViews.add(yearsView);
        popupViews.add(semestersView);

        yearsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(years.get(position));
                mDropDownMenu.closeMenu();
                selectedYear=years.get(position);
                beginAssignmentRefresh();

            }
        });

        semestersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                semesterAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(semesters[position]);
                mDropDownMenu.closeMenu();
                selectedSemester=(position+1)+"";
                beginAssignmentRefresh();
            }
        });

        final View contentView = LayoutInflater.from(this).inflate(R.layout.ac_score, null);

        swipeRefreshLayout = ButterKnife.findById(contentView, R.id.swipeRefreshLayout);
        scoreListView=  ButterKnife.findById(contentView, R.id.scoreListView);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);


        groupChildIndex=new int[]{-1,-1};

        // listen refresh event
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beginAssignmentRefresh();
            }
        });



        adapter=new ScoreAdapter(this,null);

        scoreListView.setAdapter(adapter);

        new LoadSemesterTask().execute();


    }

    public void beginAssignmentRefresh(){
        if(selectedSemester!=null&&selectedYear!=null){
            new RequestScoreTask().execute();
        }else{
            Toast.makeText(this,"请选择学年学期",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    class RequestScoreTask extends AsyncTask<String,String,String>{
        List<ScoreType> scoreTypes;

        @Override
        protected String doInBackground(String... params) {
            scoreTypes=new ArrayList<>();
            try {
                List<CourseType> courseTypes=WSConnector.getInstance().getLabCourseList(selectedYear,selectedSemester);

                if(courseTypes==null){
                    return null;
                }
                for (CourseType courseType:courseTypes){

                    ScoreType scoreType=WSConnector.getInstance().getStudentScoreList(courseType.getCourseId());
                    if(scoreType==null){
                        continue;
                    }
                    scoreType.setCourseName(courseType.getName());
                    scoreTypes.add(scoreType);

                }


            } catch (WSException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.setScoreTypes(scoreTypes);

            adapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
        }
    }



    class LoadSemesterTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String[] params) {

            try {
                List<SemesterType> semesterTypes=WSConnector.getInstance().getSemesterList();

                if(semesterTypes==null){
                    return null;
                }


                for(SemesterType semesterType:semesterTypes){
                    if(years.contains(""+semesterType.getYear())){
                        continue;
                    }
                    years.add(""+semesterType.getYear());
                }


            } catch (WSException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(years!=null&&years.size()>0){
                yearAdapter.setData(years);
                yearAdapter.notifyDataSetChanged();
            }


        }
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
        tvTitle.setText("我的成绩");
        registerBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }
}
