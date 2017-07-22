package cn.lztech.openlabandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.yyydjk.library.DropDownMenu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.elnet.andrmb.bean.AssignmentReportTurple;
import cn.elnet.andrmb.bean.AssignmentType;
import cn.elnet.andrmb.bean.CourseType;
import cn.elnet.andrmb.bean.ReportInfo;
import cn.elnet.andrmb.bean.ScoreType;
import cn.elnet.andrmb.bean.SemesterType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.UploadAssignmentActivity;
import cn.lztech.openlabandroid.adapter.CourseAdapter;
import cn.lztech.openlabandroid.adapter.ListDropDownAdapter;
import cn.lztech.openlabandroid.cache.ContentBox;
import cn.lztech.openlabandroid.utils.TimeUtils;

/**
 * Created by Administrator on 2016/4/5.
 */
public class AssignmentFragment extends Fragment {


    DropDownMenu mDropDownMenu;

    ExpandableListView assignmentListView;

    List<CourseType> courseTypes=new ArrayList<CourseType>();
    List<AssignmentType> assignmentTypes=new ArrayList<AssignmentType>();
    List<ReportInfo> reportInfos=new ArrayList<ReportInfo>();

    AssignmentReportTurple assignmentReportTurple;
    int[] groupChildIndex;


    PullRefreshLayout swipeRefreshLayout;
    MainActivity mainActivity;
    CourseAdapter courseAdapter;
    String selectedSemester=null;
    String selectedYear=null;
    /**
     * popup view
     */
    private String headers[] = {"选择学年", "选择学期"};
    private List<View> popupViews = new ArrayList<>();

    private List<String> years=new ArrayList<String>();
    private String semesters[]={"第一学期","第二学期","第三学期"};


    ListDropDownAdapter yearAdapter,semesterAdapter;

    Date serverDate;
    View contentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_assignment,null);


        mDropDownMenu=ButterKnife.findById(view, R.id.dropDownMenu);

        final ListView yearsView = new ListView(getActivity());
        yearsView.setDividerHeight(0);
        yearAdapter = new ListDropDownAdapter(getActivity(), years);

        yearsView.setAdapter(yearAdapter);


        final ListView semestersView = new ListView(getActivity());
        semestersView.setDividerHeight(0);
        semesterAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(semesters));
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

        contentView = inflater.inflate(R.layout.assignment_item, null);
        assignmentListView = ButterKnife.findById(contentView, R.id.assignmentListView);
        swipeRefreshLayout = ButterKnife.findById(contentView, R.id.swipeRefreshLayout);

        //mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);


        groupChildIndex=new int[]{-1,-1};

        // listen refresh event
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beginAssignmentRefresh();
            }
        });

        new LoadSemesterTask().execute();

        return view;
    }





    public void beginAssignmentRefresh(){
        if(selectedSemester!=null&&selectedYear!=null){
            new AssignmentTask(-1,-1).execute();
        }else{
            Toast.makeText(getActivity(),"请选择学年学期",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if(groupChildIndex[0]<0||groupChildIndex[1]<0){
            return;
        }

        if(selectedSemester!=null&&selectedYear!=null){
            Log.v("Expand Group Index","Expand g:"+groupChildIndex[0]+" c:"+groupChildIndex[1]);
            new AssignmentTask(courseTypes.get(groupChildIndex[0]).getCourseId(),groupChildIndex[0]).execute();
        }else{
            Toast.makeText(getActivity(),"请选择学年学期",Toast.LENGTH_SHORT).show();
        }



    }


    class LoadSemesterTask extends AsyncTask<String,String,String>{
        int[] yearSemesterArr;
        @Override
        protected String doInBackground(String[] params) {

            try {
                List<SemesterType> semesterTypes=WSConnector.getInstance().getSemesterList();
                yearSemesterArr=WSConnector.getInstance().getCurrentSemester();


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
            int pos0=0;
            int pos1=0;

            if(years!=null&&years.size()>0&&yearSemesterArr.length==2){
                yearAdapter.setData(years);
                yearAdapter.notifyDataSetChanged();
                for (int i=0;i<years.size();i++){
                    if(yearSemesterArr[0]==Integer.parseInt(years.get(i))){
                        pos0=i;
                    }
                }

                pos1=yearSemesterArr[1]-1;

                if(pos1<0){
                    pos1=0;
                }
                selectedYear=years.get(pos0);
                selectedSemester=yearSemesterArr[1]+"";
            }

            yearAdapter.setCheckItem(pos0);
            semesterAdapter.setCheckItem(pos1);
            if(pos0>=0&&pos1>=0){
                mDropDownMenu.setDropDownMenu(Arrays.asList(new String[]{selectedYear,semesters[pos1]}), popupViews, contentView);

            }else{
                mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
            }


            beginAssignmentRefresh();



        }
    }



    private  void initAdapter(int  courseId,int groupIndex){

        if(courseAdapter!=null&&courseId>0){
            Log.v("initAdapter","courseId : "+courseId+"; groupIndex: "+groupIndex);
            courseAdapter.setCourseTypes(courseTypes);
            if(groupIndex>=0){
                assignmentListView.expandGroup(groupIndex);
            }
            courseAdapter.notifyDataSetChanged();
            return;
        }

        courseAdapter=new CourseAdapter(getActivity(),courseTypes);

        assignmentListView.setAdapter(courseAdapter);
        assignmentListView.setDividerHeight(1);
        assignmentListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.v("assignmentListView","Group Click groupPosition:"
                        +groupPosition
                        +" isExpand "+parent.isGroupExpanded(groupPosition));
                if(!parent.isGroupExpanded(groupPosition)){
                    new AssignmentTask(courseTypes.get(groupPosition).getCourseId(),-1).execute();
                }

                return false;
            }
        });
        assignmentListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                groupChildIndex[0]=groupPosition;
                groupChildIndex[1]=childPosition;

                int courseId=courseTypes.get(groupPosition).getCourseId();

                AssignmentType assignmentType=courseTypes.get(groupPosition).getAssignmentTypes().get(childPosition);

                int  assignmentId=assignmentType.getId();
                List<ReportInfo>  reportInfos=courseTypes.get(groupPosition).getReportInfos();



                Date dueDate=TimeUtils.getDate(assignmentType.getDueDate(),"yyyy-MM-dd'T'HH:mm:ss.SSS");



                if(dueDate.compareTo(serverDate)<0&&!isHasReport(reportInfos,assignmentId)){
                    Toast.makeText(getActivity(),"上传作业通道已关闭,请下次注意作业过期时间",Toast.LENGTH_SHORT).show();
                    return false;
                }

                Log.v("assignmentListView","courseId "+courseId+" : "+assignmentId+" reportInfos:"+reportInfos);


                Intent intent=new Intent(getActivity(),UploadAssignmentActivity.class);
                intent.putExtra(UploadAssignmentActivity.BUNDLE_KEY_ASSIGNMENTID,assignmentId);
                intent.putExtra(UploadAssignmentActivity.BUNDLE_KEY_COURSEID,courseId);
                intent.putExtra(UploadAssignmentActivity.BUNDLE_KEY_TITLE,courseTypes.get(groupPosition)
                                                                                     .getAssignmentTypes()
                                                                                     .get(childPosition)
                                                                                     .getDesc());

                if(reportInfos!=null){
                    intent.putParcelableArrayListExtra(UploadAssignmentActivity.BUNDLE_KEY_REPORTINFO, (ArrayList<? extends Parcelable>) reportInfos);
                }



                startActivity(intent);



                return false;
            }
        });
    }
    private boolean isHasReport(List<ReportInfo>  reportInfos,int assignmentId){
        boolean reportYN=false;
        if(reportInfos==null||reportInfos.size()==0){
            return reportYN;
        }

        for (ReportInfo reportInfo:reportInfos){
            if(reportInfo.getAssignmentId()==assignmentId){
                reportYN=true;
                break;
            }
        }

        return reportYN;

    }

    /**
     * 判断课程是否需要重新网络加载
     * 节省服务端请求流量
     * @param courseId
     * @return
     */
    boolean isNeedReloadAssignment(int courseId){
        boolean isNeedReload=true;
        for (int i=0;i<courseTypes.size();i++){
            if(courseTypes.get(i).getCourseId()==courseId){
                if(courseTypes.get(i).getAssignmentTypes()!=null){
                    isNeedReload=false;
                }

                break;
            }
        }

        return isNeedReload;
    }

    class AssignmentTask extends AsyncTask<String,String,String>{
        int courseId;
        int groupIndex;
        AssignmentTask(int courseId,int groupIndex){
            this.courseId=courseId;
            this.groupIndex=groupIndex;
        }


        @Override
        protected String doInBackground(String... params) {

            try {

                serverDate=TimeUtils.getServerTime();

                if(courseId<=0){
                    courseTypes=WSConnector.getInstance().getLabCourseList(selectedYear,selectedSemester);
                }else{
                    if(isNeedReloadAssignment(courseId)||groupIndex>=0){
                        assignmentReportTurple=WSConnector.getInstance().getAassignmentList(courseId);
                        ScoreType scoreType=WSConnector.getInstance().getStudentScoreList(courseId);

                        // assignmentReportTurple.setScoreType(scoreType);

                        assignmentTypes=assignmentReportTurple.getAssignmentTypeList();
                        reportInfos=assignmentReportTurple.getReportInfoList();

                        for (int i=0;i<courseTypes.size();i++){
                            if(courseTypes.get(i).getCourseId()==courseId){
                                courseTypes.get(i).setAssignmentTypes(assignmentTypes);
                                courseTypes.get(i).setReportInfos(reportInfos);
                                courseTypes.get(i).setScoreType(scoreType);
                                break;
                            }
                        }
                    }
                }

            } catch (WSException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            initAdapter(courseId,groupIndex);

            swipeRefreshLayout.setRefreshing(false);

        }
    }



}
