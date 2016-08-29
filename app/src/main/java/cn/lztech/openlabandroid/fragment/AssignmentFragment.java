package cn.lztech.openlabandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.elnet.andrmb.bean.AssignmentReportTurple;
import cn.elnet.andrmb.bean.AssignmentType;
import cn.elnet.andrmb.bean.CourseType;
import cn.elnet.andrmb.bean.ReportInfo;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.UploadAssignmentActivity;
import cn.lztech.openlabandroid.adapter.CourseAdapter;

/**
 * Created by Administrator on 2016/4/5.
 */
public class AssignmentFragment extends Fragment {
    ExpandableListView assignmentListView;
    TextView emptyDescView;

    List<CourseType> courseTypes=new ArrayList<CourseType>();
    List<AssignmentType> assignmentTypes=new ArrayList<AssignmentType>();
    List<ReportInfo> reportInfos=new ArrayList<ReportInfo>();

    AssignmentReportTurple assignmentReportTurple;
    int[] groupChildIndex;


    PullRefreshLayout swipeRefreshLayout;
    MainActivity mainActivity;
    CourseAdapter courseAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_assignment,null);

        assignmentListView= (ExpandableListView) view.findViewById(R.id.assignmentListView);
        emptyDescView= (TextView) view.findViewById(R.id.emptyDescView);
        swipeRefreshLayout= (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        emptyDescView.setVisibility(View.GONE);
        groupChildIndex=new int[]{-1,-1};


        // listen refresh event
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AssignmentTask(null,-1).execute();
            }
        });



        new AssignmentTask(null,-1).execute();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(groupChildIndex[0]<0||groupChildIndex[1]<0){
            return;
        }

        Log.v("Expand Group Index","Expand g:"+groupChildIndex[0]+" c:"+groupChildIndex[1]);
        new AssignmentTask(courseTypes.get(groupChildIndex[0]).getCourseCode(),groupChildIndex[0]).execute();

    }



    private  void initAdapter(String courseCode,int groupIndex){

        if(courseAdapter!=null&&courseCode!=null){
            Log.v("initAdapter","courseCode : "+courseCode+"; groupIndex: "+groupIndex);
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
                    new AssignmentTask(courseTypes.get(groupPosition).getCourseCode(),-1).execute();
                }

                return false;
            }
        });
        assignmentListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                groupChildIndex[0]=groupPosition;
                groupChildIndex[1]=childPosition;

                String courseCode=courseTypes.get(groupPosition).getCourseCode();
                int  assignmentId=courseTypes.get(groupPosition).getAssignmentTypes().get(childPosition).getId();
                List<ReportInfo>  reportInfos=courseTypes.get(groupPosition).getReportInfos();

                Log.v("assignmentListView","courseCode "+courseCode+" : "+assignmentId+" reportInfos:"+reportInfos);


                Intent intent=new Intent(getActivity(),UploadAssignmentActivity.class);
                intent.putExtra(UploadAssignmentActivity.BUNDLE_KEY_ASSIGNMENTID,assignmentId);
                intent.putExtra(UploadAssignmentActivity.BUNDLE_KEY_COURSECODE,courseCode);
                intent.putExtra(UploadAssignmentActivity.BUNDLE_KEY_TITLE,courseTypes.get(groupPosition)
                                                                                     .getAssignmentTypes()
                                                                                     .get(childPosition)
                                                                                     .getDesc());

                intent.putParcelableArrayListExtra(UploadAssignmentActivity.BUNDLE_KEY_REPORTINFO, (ArrayList<? extends Parcelable>) reportInfos);


                startActivity(intent);



                return false;
            }
        });
    }


    /**
     * 判断课程是否需要重新网络加载
     * 节省服务端请求流量
     * @param courseCode
     * @return
     */
    boolean isNeedReloadAssignment(String courseCode){
        boolean isNeedReload=true;
        for (int i=0;i<courseTypes.size();i++){
            if(courseTypes.get(i).getCourseCode().equals(courseCode)){
                if(courseTypes.get(i).getAssignmentTypes()!=null){
                    isNeedReload=false;
                }

                break;
            }
        }

        return isNeedReload;
    }

    class AssignmentTask extends AsyncTask<String,String,String>{
        String courseCode;
        int groupIndex;
        AssignmentTask(String courseCode,int groupIndex){
            this.courseCode=courseCode;
            this.groupIndex=groupIndex;
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                if(courseCode==null){
                    courseTypes=WSConnector.getInstance().getLabCourseList();
                }else{
                    if(isNeedReloadAssignment(courseCode)||groupIndex>=0){
                        assignmentReportTurple=WSConnector.getInstance().getAassignmentList(courseCode);

                        assignmentTypes=assignmentReportTurple.getAssignmentTypeList();
                        reportInfos=assignmentReportTurple.getReportInfoList();

                        for (int i=0;i<courseTypes.size();i++){
                            if(courseTypes.get(i).getCourseCode().equals(courseCode)){
                                courseTypes.get(i).setAssignmentTypes(assignmentTypes);
                                courseTypes.get(i).setReportInfos(reportInfos);
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


            initAdapter(courseCode,groupIndex);

            swipeRefreshLayout.setRefreshing(false);

        }
    }



}
