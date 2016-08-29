package cn.lztech.openlabandroid.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.github.florent37.viewanimator.AnimationBuilder;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.List;

import cn.lztech.openlabandroid.MainActivity;
import cn.lztech.openlabandroid.PhotoPreviewActivity;
import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.UploadAssignmentActivity;
import cn.lztech.openlabandroid.utils.DensyUtils;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MyGridViewAdapter extends BaseAdapter {

    List<UploadAssignmentActivity.ReportImage> reportImages;
    Context ctx;
    UploadAssignmentActivity activity;
    public MyGridViewAdapter(Context ctx,List<UploadAssignmentActivity.ReportImage> reportImages){
        this.ctx    = ctx;
        this.reportImages = reportImages;
        this.activity= (UploadAssignmentActivity) ctx;
    }


    public void setPhotos(List<UploadAssignmentActivity.ReportImage> reportImages){
        this.reportImages=reportImages;
    }
    @Override
    public int getCount() {
        if(reportImages!=null){
            return reportImages.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(reportImages!=null){
            return reportImages.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.layout_imageview,null);
        }

        final ImageView layoutImageView= (ImageView) convertView.findViewById(R.id.layoutImageView);
        final Button delBtn= (Button) convertView.findViewById(R.id.delBtn);
        if(reportImages.get(position).isShowDelIcon()){
            delBtn.setVisibility(View.VISIBLE);
        }else{
            delBtn.setVisibility(View.GONE);
        }

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeImagePaths(position);
                notifyDataSetChanged();
            }
        });
        if(position==0){
            layoutImageView.setOnLongClickListener(null);
            layoutImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.showSheetDialog();
                }
            });

        }else{

            final View finalConvertView = convertView;
            layoutImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("Click","pos "+position+" bm "+reportImages.get(position).getBm());
                    if(delBtn.getVisibility()==View.VISIBLE){
                        delBtn.setVisibility(View.GONE);
                    }else{
                        System.out.println("W:"+reportImages.get(position).getBm().getWidth()
                                        +" H:"+reportImages.get(position).getBm().getHeight()+" BM:"+reportImages.get(position).getBm());

                        Intent intent=new Intent(ctx,PhotoPreviewActivity.class);
                        intent.putExtra(PhotoPreviewActivity.KEY_BITMAP_DATA,reportImages.get(position).getBm());
                        ctx.startActivity(intent);
                    }

                }
            });
            layoutImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(reportImages.get(position).getType()== UploadAssignmentActivity.ReportImage.RType.RType_CLIENT){
                        delBtn.setVisibility(View.VISIBLE);
                        ViewAnimator.animate(delBtn).duration(500).wave().start();
                        reportImages.get(position).setShowDelIcon(true);
                    }
                    return true;
                }
            });

        }


        layoutImageView.setImageBitmap(reportImages.get(position).getBm());
        return convertView;
    }

}
