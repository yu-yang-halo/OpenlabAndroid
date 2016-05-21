package cn.lztech.openlabandroid.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

import cn.lztech.openlabandroid.R;
import cn.lztech.openlabandroid.utils.DensyUtils;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MyGridViewAdapter extends BaseAdapter {

    List<Bitmap> photos;
    Context ctx;
    public MyGridViewAdapter(Context ctx,List<Bitmap> photos){
        this.ctx    = ctx;
        this.photos = photos;
    }

    public void setPhotos(List<Bitmap> photos){
        this.photos=photos;
    }
    @Override
    public int getCount() {
        if(photos!=null){
            return photos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(photos!=null){
            return photos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.layout_imageview,null);
        }

        ImageView layoutImageView= (ImageView) convertView.findViewById(R.id.layoutImageView);




//        ImageView imageViewBtn= (ImageView) convertView;
//
//        AbsListView.LayoutParams params=new AbsListView.LayoutParams(DensyUtils.dip2px(ctx,80),DensyUtils.dip2px(ctx,80));
//
//        imageViewBtn.setLayoutParams(params);

        layoutImageView.setImageBitmap(photos.get(position));

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position==photos.size()-1){
            return true;
        }
        return false;
    }
}
