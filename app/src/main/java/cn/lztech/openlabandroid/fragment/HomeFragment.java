package cn.lztech.openlabandroid.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.github.florent37.viewanimator.ViewAnimator;

import net.soulwolf.widget.materialradio.MaterialRadioGroup;
import net.soulwolf.widget.materialradio.listener.OnCheckedChangeListener;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import cn.elnet.andrmb.bean.DeskInfo;
import cn.elnet.andrmb.bean.LabInfoType;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.utils.DatePicker2;
import cn.lztech.openlabandroid.utils.OptionPicker2;
import cn.lztech.openlabandroid.utils.TimePicker2;
import cn.lztech.openlabandroid.utils.TimeUtils;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * Created by Administrator on 2016/3/19.
 */
public class HomeFragment extends Fragment {
    CircularProgressButton circularButton1;
    List<LabInfoType> labInfoTypes;
    TextView timeRangeTextView;
    TextView locLabelTextView;
    String startTime;
    String endTime;
    int labId=-1;
    int deskNum=-1;
    MaterialRadioGroup materialRadioGroup;
    RelativeLayout relativeLayoutTime;
    RelativeLayout relativeLayoutLocation;

    int checkID=1;

    private String year;
    private String month;
    private String day;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fr_home, null);
        timeRangeTextView= (TextView) view.findViewById(R.id.timeRange);
        locLabelTextView= (TextView) view.findViewById(R.id.locLabel);
        circularButton1 = (CircularProgressButton)view.findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);

        relativeLayoutTime= (RelativeLayout) view.findViewById(R.id.relativeLayoutTime);
        relativeLayoutLocation= (RelativeLayout) view.findViewById(R.id.relativeLayoutLocation);

        relativeLayoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() != 0){
                    reReservation();
                }else{
                    if(checkID==1){
                        selectReservationTime();
                    }else{
                        startTime=TimeUtils.createNowDate("yyyy-MM-dd HH:mm:ss");
                        selectReservationTimeNow();
                    }
                }



            }
        });

        relativeLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() != 0){
                    reReservation();
                }else {
                    selectLabDeskInfo();
                }

            }
        });

        materialRadioGroup= (MaterialRadioGroup) view.findViewById(R.id.view);

        materialRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialRadioGroup group, int checkedId) {
                checkID = checkedId;
                if (checkID == 1) {
                    Toast.makeText(getActivity(), "一般预约", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "临时预约", Toast.LENGTH_SHORT).show();
                }
                paramsReset();

            }
        });


        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    if(startTime==null||endTime==null){
                        showToast("请选择预约时间");
                    }else if(labId<=0){
                        showToast("请选择实验室和工位");
                    }else{
                        new PostReservationTask(startTime, endTime, labId, deskNum).execute();
                    }
                }else{
                    reReservation();
                }


            }
        });
        new GetLabsTask().execute();
        return view;
    }

    private void reReservation(){
        new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("是否重新预约?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        paramsReset();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void paramsReset(){
        timeRangeTextView.setText("");
        locLabelTextView.setText("");
        startTime = null;
        endTime = null;
        labId = -1;
        deskNum = -1;
        circularButton1.setProgress(0);
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    class GetLabsTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
               labInfoTypes=WSConnector.getInstance().getLabListByIncDesk(true);
                System.out.println(labInfoTypes);
            } catch (WSException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private String[] getDeskInfoStr(String labName){
        List<DeskInfo> deskInfos=null;
        for (LabInfoType labInfoType:labInfoTypes){
            if(labInfoType.getName().equals(labName)){
                deskInfos=labInfoType.getDeskInfos();
                labId=labInfoType.getLabId();
                break;
            }
        }
        if(deskInfos!=null&&deskInfos.size()>0){

            String[] deskNames=new String[deskInfos.size()];

            for (int i=0;i<deskInfos.size();i++){
                deskNames[i]=deskInfos.get(i).getDeskNum()+"";
            }
            return deskNames;
        }
        return null;
    }

    private void cancelLab(){
        locLabelTextView.setText("");
        deskNum=-1;
        labId=-1;
    }
    private void cancelTime(){
        timeRangeTextView.setText("");
        startTime=null;
        endTime=null;
    }

    private  void selectLabDeskInfo(){
        if(labInfoTypes!=null&&labInfoTypes.size()>0){
            String[] labInfoNames=new String[labInfoTypes.size()];
            for (int i=0;i<labInfoTypes.size();i++){
                labInfoNames[i]=labInfoTypes.get(i).getName();
            }
            final OptionPicker2 picker = new OptionPicker2(getActivity(), labInfoNames);
            picker.setTitleText("选择预约实验室");
            picker.setSelectedIndex(0);
            picker.setTextSize(11);
            picker.setOnOptionPickListener(new OptionPicker2.OnOptionPickListener() {
                @Override
                public void onOptionPicked(String option) {
                    final String selectedLabName = option;
                    String[] deskNames = getDeskInfoStr(option);
                    locLabelTextView.setText("实验室名称:" + option);
                    startViewAnimation(locLabelTextView);

                }

            });

            picker.show();
        }

    }
    class PostReservationTask extends  AsyncTask<String,String,String>{
        String sTime;
        String eTime;
        int labID;
        int dNum;

        PostReservationTask(String sTime,String eTime,int labID,int dNum){
              this.sTime=sTime;
              this.eTime=eTime;
              this.labID=labID;
              this.dNum=dNum;

        }

        @Override
        protected void onPreExecute() {
            circularButton1.setProgress(50);
        }

        @Override
        protected String doInBackground(String... params) {
            String loginName=WSConnector.getInstance().getUserMap().get("loginName");

            try {
                WSConnector.getInstance().addOrUpdReservation(loginName,sTime,eTime,deskNum,labID,0,0);
            } catch (WSException e) {
                return e.getErrorMsg();
            } catch (UnsupportedEncodingException e) {
                return e.getMessage();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                circularButton1.setProgress(100);
            }else{
                showToast(s);
                circularButton1.setProgress(-1);
            }
        }
    }

    private void selectHourMinTime(final int type){
        String title="开始时间";
        if(type==1){
            title="结束时间";
        }

        int[] hourMinutes=TimeUtils.getThisHourMinute();
        //默认选中当前时间
        TimePicker2 picker = new TimePicker2(getActivity(), TimePicker2.HOUR_OF_DAY);
        picker.setTitleText(title);

        if(type==1){
           Date date=TimeUtils.getDate(startTime, "yyyy-MM-dd HH:mm:ss");

           if(date.getMinutes()>=30){
               date.setHours(date.getHours()+1);
               picker.setSelectedItem(date.getHours(),0);
           }else{
               picker.setSelectedItem(date.getHours(),30);
           }
           int[] yMDs=TimeUtils.getThisYearMonthDay(date);

            year=yMDs[0]+"";
            month=yMDs[1]+"";
            day=yMDs[2]+"";

        }else{
            /*
                 bug 会出现
             */
            if(hourMinutes[1]>=30){
                picker.setSelectedItem(hourMinutes[0]+1,0);
            }else{
                picker.setSelectedItem(hourMinutes[0],30);
            }

        }


        picker.setTopLineVisible(true);
        picker.setOnMyCancelListener(new TimePicker2.OnMyCancelListener() {
            @Override
            public void onMyCancel() {
                cancelTime();
            }
        });
        picker.setOnTimePickListener(new TimePicker2.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {



                if(type==0){

                    startTime = TimeUtils.createDateFormat(year, month, day, hour, minute, "yyyy-MM-dd HH:mm:ss");
                    timeRangeTextView.setText("开始时间:"+startTime);

                    selectHourMinTime(1);
                }else{
                    endTime = TimeUtils.createDateFormat(year, month, day, hour, minute, "yyyy-MM-dd HH:mm:ss");


                    if(TimeUtils.compareToAMoreThanB(startTime,endTime)){
                        showToast("开始时间不得大于结束时间");
                        startTime=null;
                        endTime=null;
                        timeRangeTextView.setText("");

                    }else{
                        timeRangeTextView.setText("开始时间:"+startTime+"\n\n结束时间:"+endTime);
                    }
                }


                startViewAnimation(timeRangeTextView);


            }
        });

        picker.show();
    }
    private void startViewAnimation(View v){
        ViewAnimator.animate(v)
                .slideRight()
                .interpolator(new AccelerateInterpolator())
                .duration(1500)
                .start();
    }

    private  void selectReservationTime(){
        int[] thisYearMonthDay=TimeUtils.getThisYearMonthDay();

        DatePicker2 picker = new DatePicker2(getActivity(), DatePicker2.YEAR_MONTH_DAY);
        picker.setRange(thisYearMonthDay[0], thisYearMonthDay[0]);//年份范围
        picker.setTitleText("选择预约日期");
        picker.setSelectedItem(thisYearMonthDay[0], thisYearMonthDay[1], thisYearMonthDay[2]);

        picker.setOnDatePickListener(new DatePicker2.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(final String year1, final String month1, final String day1) {
                year=year1;
                month=month1;
                day=day1;
                selectHourMinTime(0);
            }
        });
        picker.show();

    }

    private  void selectReservationTimeNow(){

        selectHourMinTime(1);

    }

}
