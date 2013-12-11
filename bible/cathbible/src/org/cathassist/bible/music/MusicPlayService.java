package org.cathassist.bible.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import org.cathassist.bible.lib.Func;

import java.io.IOException;

public class MusicPlayService extends Service{
    private MediaPlayer mPlayer;
    private String mLast = "";

    public static final int STATUS_IDLE = 0;

    public static final int MODE_SINGLE = 0;
    public static final int MODE_SINGLE_LOOP =1;
    public static final int MODE_ORDER = 2;

    private int mPlayMode;

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.release();
        mPlayer = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPlayer = new MediaPlayer();
        mPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
    }

    public MediaPlayer getMusicPlayer() {
        return mPlayer;
    }

    public void play(String path, int book, int chapter) {
        String file = Func.getFilePath(Func.getFileName(path, book, chapter)).getAbsolutePath();

        if(mLast.equals(file) && getProgress() < getDuration()) {            //同一首
            if(mPlayer.isPlaying()) {           //在播放
                mPlayer.pause();
            } else {
                mPlayer.start();                //已停止
            }
        } else {                            //不同首
            mPlayer.reset();
            try {
                mPlayer.setDataSource(file);
                mPlayer.prepare();
                mPlayer.start();
                mLast = file;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }

    public void setProgress(int progress) {
        if(mPlayer.isPlaying()) {
            mPlayer.seekTo(progress);
        }
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }

    public void setPlayMode(int mode) {
        mPlayMode = mode;
    }

    public class MusicPlayBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }
}
