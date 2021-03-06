package cn.lztech.openlabandroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.lztech.openlabandroid.cache.BitmapCacheManager;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.widget.TouchImageView;

/**
 * Created by Administrator on 2016/6/17.
 */
public class PhotoPreviewActivity extends StatusBarActivity {
    public static String KEY_BITMAP_DATA="bitmap_data_key";
    TouchImageView previewImage;
    private  Bitmap image_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_photo_preview);



        previewImage= (TouchImageView) findViewById(R.id.previewImage);
        image_data=BitmapCacheManager.getInstance().getCurrentBm();


        previewImage.setImageBitmap(image_data);

        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
