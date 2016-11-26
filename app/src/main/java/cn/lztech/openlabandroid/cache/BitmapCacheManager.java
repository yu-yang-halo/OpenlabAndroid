package cn.lztech.openlabandroid.cache;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/11/26 0026.
 */
public class BitmapCacheManager {
    private static BitmapCacheManager instance=new BitmapCacheManager();

    private BitmapCache bitmapCache;

    private Bitmap currentBm;

    public Bitmap getCurrentBm() {
        return currentBm;
    }

    public void setCurrentBm(Bitmap currentBm) {
        this.currentBm = currentBm;
    }

    private BitmapCacheManager(){
        bitmapCache=new BitmapCache();

    }
    public static BitmapCacheManager getInstance(){
        if(instance==null){
            instance=new BitmapCacheManager();
        }
        return instance;
    }

    public void putBitmap(String key, Bitmap bm){
        bitmapCache.putBitmap(key,bm);
    }
    public Bitmap getBitmap(String key){
       return bitmapCache.getBitmap(key);
    }





}
