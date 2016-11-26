package cn.lztech.openlabandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.elnet.andrmb.bean.ReportInfo;
import cn.elnet.andrmb.elconnector.WSConnector;
import cn.elnet.andrmb.elconnector.WSException;
import cn.lztech.openlabandroid.adapter.MyGridViewAdapter;
import cn.lztech.openlabandroid.cache.FileDirManager;
import cn.lztech.openlabandroid.utils.DensyUtils;
import cn.lztech.openlabandroid.utils.ImageUtils;
import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Administrator on 2016/5/19.
 */
public class UploadAssignmentActivity extends Activity {
    public static final  String BUNDLE_KEY_COURSECODE="bundle_key_coursecode";
    public static final  String BUNDLE_KEY_ASSIGNMENTID="bundle_key_assignmentId";
    public static final  String BUNDLE_KEY_TITLE="bundle_key_title";
    public static final  String BUNDLE_KEY_REPORTINFO="bundle_key_report_info";
    private static  final int REQUEST_CODE_CAPTURE=1001;
    private static  final int REQUEST_CODE_ALBUM=1002;

    private  int SHOW_WIDTH=320;
    private  int SHOW_HEIGHT=320;


    EditText descriptionTV;
    GridView gridView;
    Button rightBtn;
    SweetSheet mSweetSheet;
    RelativeLayout uploadLayout;



    List<ReportImage> reportImages=new ArrayList<ReportImage>();


    MyGridViewAdapter myGridViewAdapter;

    private String title;
    private String courseCode;
    private int assignmentId;
    private Uri tmpCaptureUri;
    ReportInfo reportInfo;

    KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_uploadassignment);

        title=getIntent().getStringExtra(BUNDLE_KEY_TITLE);
        SHOW_WIDTH=DensyUtils.dip2px(this,160);
        SHOW_HEIGHT=DensyUtils.dip2px(this,160);

        Log.v("W|H","SHOW_WIDTH:"+SHOW_WIDTH+"-- SHOW_HEIGHT:"+SHOW_HEIGHT);


        initCustomActionBar();
        uploadLayout= (RelativeLayout) findViewById(R.id.uploadLayout);
        gridView= (GridView) findViewById(R.id.gridView);
        descriptionTV= (EditText) findViewById(R.id.descriptionTV);


        courseCode=getIntent().getStringExtra(BUNDLE_KEY_COURSECODE);
        assignmentId=getIntent().getIntExtra(BUNDLE_KEY_ASSIGNMENTID,0);

        List<ReportInfo> tmps=getIntent().getParcelableArrayListExtra(BUNDLE_KEY_REPORTINFO);
        String fileName=filterAssignmentReport(tmps);
        Log.v("ActivityLOG","courseCode "+courseCode+" : "+assignmentId+" reportInfo::"+reportInfo);

        /**初始化Add图标**/
        initAddPicture();


        myGridViewAdapter=new MyGridViewAdapter(UploadAssignmentActivity.this,reportImages);

        gridView.setAdapter(myGridViewAdapter);

        loadServerData(fileName);
    }
    private void  initAddPicture(){
       Bitmap bm=ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.icon_addpic));
       ReportImage reportImage=new ReportImage(ReportImage.RType.RType_ADD,null,bm, ReportImage.RFormat.RFORMAT_BM);
       reportImages.add(reportImage);
    }

    private String  filterAssignmentReport( List<ReportInfo> tmps){
        if(tmps==null){
            return null;
        }
        for (ReportInfo ri:tmps){
            if(ri.getAssignmentId()==assignmentId){
                reportInfo=ri;
            }
        }

       if(reportInfo!=null){
           descriptionTV.setText(reportInfo.getDescription());
           return reportInfo.getFileName();
       }
       return null;
    }





    public void removeImagePaths(int pos){
        if(reportImages!=null&&reportImages.size()>0){
            reportImages.remove(pos);
        }

    }

    public  void showSheetDialog(){
        // SweetSheet 控件,根据 rl 确认位置
        mSweetSheet = new SweetSheet(uploadLayout);
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        View customView = LayoutInflater.from(UploadAssignmentActivity.this).inflate(R.layout.layout_custom_view, null, false);
        customDelegate.setCustomView(customView);
        mSweetSheet.setDelegate(customDelegate);
        customView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet.dismiss();
            }
        });
        customView.findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCamera(REQUEST_CODE_CAPTURE);
                mSweetSheet.dismiss();
            }
        });
        customView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCamera(REQUEST_CODE_ALBUM);
                mSweetSheet.dismiss();
            }
        });

        mSweetSheet.show();
    }



    private void requestCamera(int requestCode) {

        if(requestCode==REQUEST_CODE_ALBUM){


            PhotoPicker.builder().setPhotoCount(9)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .start(this,REQUEST_CODE_ALBUM);

        }else if(requestCode==REQUEST_CODE_CAPTURE){

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            tmpCaptureUri=FileDirManager.getOutputMediaFileUri(FileDirManager.MEDIA_TYPE_IMAGE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpCaptureUri);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
               startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE);
            }
        }


    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**加载相册报告**/
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ALBUM) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String path:photos){
                    loadToReportContainer(path);
                }
                Message msg=new Message();
                msg.what=1;
                myHandler.sendMessage(msg);
            }
        }
        /**加载拍照报告**/
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAPTURE){
            if(data!=null){
                tmpCaptureUri=data.getData();
            }
            Log.v("tmpCaptureUri",tmpCaptureUri.getPath());
            loadToReportContainer(tmpCaptureUri.getPath());
            Message msg=new Message();
            msg.what=1;

            myHandler.sendMessage(msg);

        }

    }

    private void loadToReportContainer(String path){
        Bitmap bm=ImageUtils.convertToBitmap(path,SHOW_WIDTH,SHOW_HEIGHT);
        ReportImage reportImage=new ReportImage(ReportImage.RType.RType_CLIENT,path,bm, ReportImage.RFormat.RFORMAT_URI);
        reportImages.add(reportImage);
    }


    private void loadServerData(final String existImageNames){
        progressHUD=KProgressHUD.create(UploadAssignmentActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("数据加载中...")
                .setAnimationSpeed(1)
                .setDimAmount(0.3f)
                .show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                /* add camera picture */

                if(existImageNames!=null){
                    String[]  imageNames=existImageNames.split(",");
                    for (String imageName:imageNames){
                        String webImageUrl=WSConnector.getInstance().getWebImageURL(imageName);
                        Bitmap bm=ImageUtils.convertNetToBitmap(webImageUrl,SHOW_WIDTH,SHOW_HEIGHT);

                        if(bm==null){
                            continue;
                        }
                        ReportImage reportImage=new ReportImage(ReportImage.RType.RType_SERVER,webImageUrl,bm, ReportImage.RFormat.RFORMAT_BM);
                        reportImages.add(reportImage);
                    }
                }

                Message msg=new Message();
                msg.what=1;

                myHandler.sendMessage(msg);



            }
        }).start();







    }
    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                if(progressHUD!=null){
                    progressHUD.dismiss();
                }

                myGridViewAdapter.setPhotos(reportImages);
                myGridViewAdapter.notifyDataSetChanged();
                Log.v("bitmaps"," size "+reportImages.size());
                if(reportImages.size()<1){
                    rightBtn.setEnabled(false);
                }else{
                    rightBtn.setEnabled(true);
                }
            }
        }
    };

    private boolean initCustomActionBar() {
        ActionBar mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.top_back_center_bar);
        TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);

        if(title!=null&&!title.trim().equals("")){
            tvTitle.setText(title);
        }else{
            tvTitle.setText("请上传作业");
        }

        rightBtn.setText("提交");



        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportImages!=null&&reportImages.size()>1){

                    Log.v("SIZE",reportImages.size()+"....");

                    String desc=descriptionTV.getText().toString();

                    if(desc==null||desc.trim().equals("")){
                        Toast.makeText(UploadAssignmentActivity.this,"请描述说明",Toast.LENGTH_SHORT).show();
                    }else{
                        new UploadImageTask(UploadAssignmentActivity.this,desc).execute();
                    }


                }else{
                    Toast.makeText(UploadAssignmentActivity.this,"请添加上传图片",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    class UploadImageTask extends AsyncTask<String,String,String>{
        KProgressHUD progressHUD;
        boolean isUploadSuccess=true;
        boolean uploadIsEmpty=true;
        Context ctx;
        String desc;
        UploadImageTask(Context ctx,String desc){
            this.ctx=ctx;
            this.desc=desc;
        }
        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("图片上传中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();


        }

        @Override
        protected String doInBackground(String... params) {
            for(ReportImage reImage:reportImages){
                if(reImage.getType()!= ReportImage.RType.RType_CLIENT){
                    continue;
                }
                uploadIsEmpty=false;
                String base64CodeString=ImageUtils.fileToString(reImage.getPath());
                // System.out.println("base64CodeString:::"+base64CodeString);
                try {
                    WSConnector.getInstance().submitReport(courseCode,base64CodeString,desc,assignmentId);
                } catch (WSException e) {
                    e.printStackTrace();
                    isUploadSuccess=false;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();

            if(uploadIsEmpty){
                Toast.makeText(UploadAssignmentActivity.this,"没有添加新的内容无法上传",Toast.LENGTH_SHORT).show();
            }else{
                if(isUploadSuccess){
                    Toast.makeText(UploadAssignmentActivity.this,"作业提交成功",Toast.LENGTH_SHORT).show();
                    UploadAssignmentActivity.this.finish();
                }else{
                    Toast.makeText(UploadAssignmentActivity.this,"作业提交失败",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

   public static class ReportImage{
        RType type;
        String path;
        Bitmap bm;
        RFormat format;
        boolean showDelIcon;
        public static enum  RType{
            RType_SERVER,RType_CLIENT,RType_ADD;
        }
        public static enum  RFormat{
            RFORMAT_HTTP,RFORMAT_URI,RFORMAT_BM;
        }

        public ReportImage(RType type, String path, Bitmap bm, RFormat format) {
            this.type = type;
            this.path = path;
            this.bm = bm;
            this.format = format;
        }

        public RType getType() {

            return type;
        }

       public boolean isShowDelIcon() {
           return showDelIcon;
       }

       public void setShowDelIcon(boolean showDelIcon) {
           this.showDelIcon = showDelIcon;
       }

       public void setType(RType type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Bitmap getBm() {
            return bm;
        }

        public void setBm(Bitmap bm) {
            this.bm = bm;
        }

        public RFormat getFormat() {
            return format;
        }

        public void setFormat(RFormat format) {
            this.format = format;
        }
    }
}
