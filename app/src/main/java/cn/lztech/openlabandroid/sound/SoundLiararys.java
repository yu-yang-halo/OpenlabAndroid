package cn.lztech.openlabandroid.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

import cn.lztech.openlabandroid.R;

/**
 * Created by Administrator on 2016/3/25.
 */
public class SoundLiararys {

    public   static   SoundPool  sound ;
    public   static   int   soundId ;


    public static void playSound(final Context ctx){

//        AudioAttributes attributes=new AudioAttributes
//                                          .Builder()
//                                          .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                                          .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
//                                          .setLegacyStreamType(AudioManager. STREAM_MUSIC)
//                                          .setUsage(AudioAttributes.USAGE_ALARM).build();
//
//
//
//        sound=new SoundPool.Builder()
//                     .setAudioAttributes(attributes)
//                .setMaxStreams(3)
//                .build() ;
        sound  =  new SoundPool(3,  AudioManager.STREAM_MUSIC , 0);
        sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

            }
        });
        soundId  =  sound .load(ctx.getApplicationContext(), R.raw.ceres, 1);
        try {
            Thread.sleep(1000);// 给予初始化音乐文件足够时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sound.play(soundId , 1.0f, 1.0f, 1, 1, 1.0f); //利用初始化后的SoundPool对象播放
    }

}
