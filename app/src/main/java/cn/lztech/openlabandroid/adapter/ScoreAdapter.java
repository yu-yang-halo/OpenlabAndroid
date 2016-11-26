package cn.lztech.openlabandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.elnet.andrmb.bean.ScoreType;
import cn.lztech.openlabandroid.R;

/**
 * Created by Administrator on 2016/10/20.
 */

public class ScoreAdapter extends BaseAdapter {
    private List<ScoreType> scoreTypes;

    public void setScoreTypes(List<ScoreType> scoreTypes){
        this.scoreTypes=scoreTypes;
    }
    private Context ctx;
    public ScoreAdapter(Context ctx,List<ScoreType> scoreTypes){
        this.ctx=ctx;
        this.scoreTypes=scoreTypes;

    }
    @Override
    public int getCount() {
        if(scoreTypes==null){
            return 0;
        }
        return scoreTypes.size();
    }

    @Override
    public Object getItem(int position) {
        if(scoreTypes==null){
            return null;
        }

        return scoreTypes.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_score,null);
        }
        TextView courseNamelabel= (TextView) convertView.findViewById(R.id.textView17);
        TextView scorelabel= (TextView) convertView.findViewById(R.id.textView18);
        TextView commentlabel= (TextView) convertView.findViewById(R.id.textView19);


        ScoreType scoreType=scoreTypes.get(position);

        if(scoreType!=null){

            courseNamelabel.setText("课程:"+scoreType.getCourseName());
            scorelabel.setText(String.format("分数:%.1f",scoreType.getScore()));
            commentlabel.setText(""+scoreType.getComment());
        }



        return convertView;
    }
}
