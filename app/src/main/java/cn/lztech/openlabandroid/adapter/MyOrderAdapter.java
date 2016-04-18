package cn.lztech.openlabandroid.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.elnet.andrmb.bean.Constants;
import cn.elnet.andrmb.bean.LabInfoType;
import cn.elnet.andrmb.bean.ReservationType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.cache.ContentBox;
import cn.lztech.openlabandroid.utils.TimeUtils;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyOrderAdapter extends BaseAdapter {
    Context ctx;
    List<ReservationType> reservationTypes;
    List<LabInfoType> labInfoTypes;
    MainActivity mainActivity;
    int type;
    private String realName;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public List<LabInfoType> getLabInfoTypes() {
        return labInfoTypes;
    }

    public void setLabInfoTypes(List<LabInfoType> labInfoTypes) {
        this.labInfoTypes = labInfoTypes;
    }

    public List<ReservationType> getReservationTypes() {
        return reservationTypes;
    }

    public void setReservationTypes(List<ReservationType> reservationTypes) {
        this.reservationTypes = reservationTypes;
    }


    public MyOrderAdapter(Context ctx,int type){
        this.ctx=ctx;
        this.type=type;

        this.realName=ContentBox.getValueString(ctx, ContentBox.KEY_REALNAME, "");
    }
    @Override
    public int getCount() {
        if(reservationTypes!=null){
            return reservationTypes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(reservationTypes!=null){
            return reservationTypes.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_order,null);
        }
        TextView contentTextView= (TextView) convertView.findViewById(R.id.nameLabel);
        TextView deskNumLabel= (TextView) convertView.findViewById(R.id.deskNumLabel);
        TextView startTimetxt= (TextView) convertView.findViewById(R.id.sTimeLabel);
        TextView endTimetxt= (TextView) convertView.findViewById(R.id.eTimeLabel);
        TextView nameTextView= (TextView) convertView.findViewById(R.id.textView9);
        Button cancelBtn= (Button) convertView.findViewById(R.id.button3);


        nameTextView.setText(realName);

        startTimetxt.setText(TimeUtils.formatString(reservationTypes.get(position).getStartTime()));
        endTimetxt.setText(TimeUtils.formatString(reservationTypes.get(position).getEndTime()));


        contentTextView.setText(findLabName(reservationTypes.get(position).getLabId()));

        deskNumLabel.setText(reservationTypes.get(position).getDeskNum()+"");

        if(type== Constants.STATUS_NORMAL){
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReservationType type=reservationTypes.get(position);
                    type.setStartTime(TimeUtils.formatString(type.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
                    type.setEndTime(TimeUtils.formatString(type.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                    type.setStatus(Constants.STATUS_CANCEL);
                    new CancelTask(type).execute();
                }
            });
        }else{
            cancelBtn.setVisibility(View.GONE);
        }

        return convertView;
    }

    private String findLabName(int labId){
        if(labInfoTypes==null){
            return "***"+labId;
        }
        String labName="";
        for (LabInfoType labInfoType:labInfoTypes){
            if(labId==labInfoType.getLabId()){
                labName=labInfoType.getName();
                break;
            }
        }
        return labName;

    }

    class CancelTask extends AsyncTask<String,String,String>{
        ReservationType reservationType;
        KProgressHUD progressHUD;
        CancelTask(ReservationType reservationType){
            this.reservationType=reservationType;
        }

        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("取消中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected String doInBackground(String... params){
            try {
                WSConnector.getInstance().addOrUpdReservation(reservationType.getUserName(),
                        reservationType.getStartTime(),
                        reservationType.getEndTime(),
                        reservationType.getDeskNum(),
                        reservationType.getLabId(),
                        reservationType.getStatus(),reservationType.getResvId());
            } catch (WSException e) {
                return e.getErrorMsg();
            } catch (UnsupportedEncodingException e) {
               return e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();
            if(s==null){
                mainActivity.dataRequest();
            }else{
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
