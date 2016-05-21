package cn.lztech.openlabandroid.adapter;

import android.app.Application;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.elnet.andrmb.bean.AssignmentType;
import cn.lztech.openlabandroid.R;

/**
 * Created by Administrator on 2016/5/17.
 */
public class AssignmentAdapter extends BaseAdapter {
    List<AssignmentType> assignmentTypes;
    Context ctx;
    public AssignmentAdapter(List<AssignmentType> assignmentTypes,Context ctx){
        this.assignmentTypes=assignmentTypes;
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        if(assignmentTypes==null){
            return 0;
        }else {
            return assignmentTypes.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(assignmentTypes==null){
            return null;
        }else{
            return assignmentTypes.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        holder.nameView.setText(assignmentTypes.get(position).getDesc());
        holder.courseName.setText(assignmentTypes.get(position).getCourseCode());
        holder.dueDate.setText(assignmentTypes.get(position).getDueDate());
        //holder.statusView.setText();



        return convertView;
    }

    class AssigmentHolder{
        TextView  nameView;
        TextView  statusView;
        TextView  courseName;
        TextView  dueDate;
    }
}
