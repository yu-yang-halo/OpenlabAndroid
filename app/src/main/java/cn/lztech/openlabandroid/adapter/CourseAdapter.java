package cn.lztech.openlabandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.elnet.andrmb.bean.AssignmentType;
import cn.elnet.andrmb.bean.CourseType;
import cn.elnet.andrmb.bean.ReportInfo;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.fragment.AssignmentFragment;
import cn.lztech.openlabandroid.utils.TimeUtils;

/**
 * Created by Administrator on 2016/6/14.
 */
public class CourseAdapter extends BaseExpandableListAdapter {
    private List<CourseType> courseTypes;
    private Context ctx;

    public CourseAdapter(Context ctx,List<CourseType> courseTypes){
        this.ctx=ctx;
        this.courseTypes=courseTypes;
    }



    public void setCourseTypes(List<CourseType> courseTypes) {
        this.courseTypes = courseTypes;
    }

    @Override
    public int getGroupCount() {
        if(courseTypes==null){
            return 0;
        }
        return courseTypes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(courseTypes==null||courseTypes.get(groupPosition).getAssignmentTypes()==null){
            return 0;
        }

        return courseTypes.get(groupPosition).getAssignmentTypes().size();


    }

    @Override
    public Object getGroup(int groupPosition) {
        return courseTypes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return courseTypes.get(groupPosition).getAssignmentTypes().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView titleView;
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_course,null);
            titleView= (TextView) convertView.findViewById(R.id.textView15);
            convertView.setTag(titleView);
        }
        titleView= (TextView) convertView.getTag();

        titleView.setText(courseTypes.get(groupPosition).getName());


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        List<AssignmentType> assignmentTypes=courseTypes.get(groupPosition).getAssignmentTypes();
        AssigmentHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_assigment,null);
            holder=new AssigmentHolder();

            holder.nameView= (TextView) convertView.findViewById(R.id.aNameView);
            holder.statusView= (TextView) convertView.findViewById(R.id.aStatusView);
            holder.dueDate= (TextView) convertView.findViewById(R.id.dueDate);
            holder.courseName= (TextView) convertView.findViewById(R.id.courseName);
            convertView.setTag(holder);

        }

        holder= (AssigmentHolder) convertView.getTag();

        holder.nameView.setText(assignmentTypes.get(childPosition).getDesc());
        holder.courseName.setText(assignmentTypes.get(childPosition).getCourseCode());
        String dueDateStr=TimeUtils.formatDueDate(assignmentTypes.get(childPosition).getDueDate(),"yyyy-MM-dd HH:mm");
        holder.dueDate.setText(dueDateStr);


        if(isHasReport(groupPosition,assignmentTypes.get(childPosition).getId())){
            holder.statusView.setText("已上传报告");
        }else{
            holder.statusView.setText("未上传报告");
        }


        return convertView;
    }

    private boolean isHasReport(int groupPos,int assignmentId){
        boolean reportYN=false;
        List<ReportInfo> reportInfos=courseTypes.get(groupPos).getReportInfos();

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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }


}
class AssigmentHolder{
    TextView  nameView;
    TextView  statusView;
    TextView  courseName;
    TextView  dueDate;
}