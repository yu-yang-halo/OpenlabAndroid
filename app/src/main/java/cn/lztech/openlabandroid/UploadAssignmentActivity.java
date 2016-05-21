package cn.lztech.openlabandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

import java.util.ArrayList;
import java.util.List;

import cn.lztech.openlabandroid.adapter.MyGridViewAdapter;
import cn.lztech.openlabandroid.utils.DensyUtils;
import cn.lztech.openlabandroid.utils.ImageUtils;
import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Administrator on 2016/5/19.
 */
public class UploadAssignmentActivity extends Activity {
    private static  final int REQUEST_CODE_CAPTURE=1001;
    private static  final int REQUEST_CODE_ALBUM=1002;

    GridView gridView;
    Button rightBtn;
    SweetSheet mSweetSheet;
    RelativeLayout uploadLayout;

    List<Bitmap> showBitmaps;

    List<Bitmap> cameraPicBitmaps=new ArrayList<Bitmap>();


    MyGridViewAdapter myGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_uploadassignment);
        initCustomActionBar();
        uploadLayout= (RelativeLayout) findViewById(R.id.uploadLayout);
        gridView= (GridView) findViewById(R.id.gridView);


        myGridViewAdapter=new MyGridViewAdapter(this,showBitmaps);

        gridView.setAdapter(myGridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        });



        loadBitmapDatas(null,null);

    }




    private void requestCamera(int requestCode) {

        if(requestCode==REQUEST_CODE_ALBUM){
                PhotoPickerIntent intent = new PhotoPickerIntent(this);
                intent.setPhotoCount(9);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                startActivityForResult(intent,requestCode);
        }else if(requestCode==REQUEST_CODE_CAPTURE){

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, requestCode);
            }
        }




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ALBUM) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);

                loadBitmapDatas(photos, null);

            }
        }else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAPTURE){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            loadBitmapDatas(null,imageBitmap);
        }

    }

    private void loadBitmapDatas(List<String> photos,Bitmap data){
        /*
            add camera picture
         */

        if (photos!=null&&photos.size()>0){

            for (String path : photos){
                cameraPicBitmaps.add(ImageUtils.convertToBitmap(path));
            }
        }

        if(data!=null){
            cameraPicBitmaps.add(data);
        }

        showBitmaps=new ArrayList<Bitmap>();

        for (Bitmap bm : cameraPicBitmaps){
            showBitmaps.add(bm);
        }

        showBitmaps.add(ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.icon_addpic)));
        myGridViewAdapter.setPhotos(showBitmaps);
        myGridViewAdapter.notifyDataSetChanged();
        Log.v("bitmaps"," size "+cameraPicBitmaps.size());
        if(cameraPicBitmaps.size()<=0){
            rightBtn.setEnabled(false);
        }else{
            rightBtn.setEnabled(true);
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
        rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);

        tvTitle.setText("请上传作业");
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

            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
