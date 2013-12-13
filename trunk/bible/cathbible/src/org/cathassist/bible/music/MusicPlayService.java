package org.cathassist.bible.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import org.cathassist.bible.lib.Func;
import org.cathassist.bible.lib.Para;

import java.io.File;


public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final int STATUS_IDLE = 0;
    public static final int MODE_SINGLE = 0;
    public static final int MODE_SINGLE_LOOP = 1;
    public static final int MODE_ORDER = 2;
    private OnCompletionListener mOnCompletionListener;
    private OnPlayListener mOnPlayListener;
    private OnPlayChangedListener mOnPlayChangedListener;
    private MediaPlayer mPlayer;
    private String mLast = "";
    private String mPath = "chn";
    private int mPlayMode;
    private Handler mPlayHandler;
    private Runnable mPlayRunnable;

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
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);

        mPlayHandler = new Handler();
        mPlayRunnable = new Runnable() {
            @Override
            public void run() {
                if (mPlayer.isPlaying()) {
                    if (mOnPlayListener != null) {
                        mOnPlayListener.onPlay(getProgress(), getDuration());
                    }
                }
                mPlayHandler.postDelayed(this, 1000);
            }
        };
    }

    public void playNet(String path, int book, int chapter) {
        String file = Func.getUrlPath(Func.getUrlName(path, book, chapter));
        if (mLast.equals(path + "/" + book + "/" + chapter) && getProgress() < getDuration()) {            //同一首
            if (mPlayer.isPlaying()) {           //在播放
                pause();
            } else {
                start();
            }
        } else {                            //不同首
            reset();
            if (Func.isWifi(this) || Para.allow_gprs) {
                try {
                    mPlayer.setDataSource(file);
                    mPlayer.prepareAsync();
                    mLast = path + "/" + book + "/" + chapter;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void play(String path, int book, int chapter) {
        String file = Func.getFilePath(Func.getFileName(path, book, chapter)).getAbsolutePath();
        if (mLast.equals(path + "/" + book + "/" + chapter) && getProgress() < getDuration()) {            //同一首
            if (mPlayer.isPlaying()) {           //在播放
                pause();
            } else {
                start();
            }
        } else {                            //不同首
            reset();
            try {
                mPlayer.setDataSource(file);
                mPlayer.prepareAsync();
                mLast = path + "/" + book + "/" + chapter;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        reset();
    }

    private void start() {
        mPlayer.start();
        mPlayHandler.postDelayed(mPlayRunnable, 1000);
        if (mOnPlayChangedListener != null) {
            mOnPlayChangedListener.onPlayChanged(true);
        }
    }

    private void reset() {
        mPlayer.reset();
        mLast = "";
        mPlayHandler.removeCallbacks(mPlayRunnable);
        if (mOnPlayChangedListener != null) {
            mOnPlayChangedListener.onPlayChanged(false);
        }
    }

    private void pause() {
        mPlayer.pause();
        mPlayHandler.removeCallbacks(mPlayRunnable);
        if (mOnPlayChangedListener != null) {
            mOnPlayChangedListener.onPlayChanged(false);
        }
    }

    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }

    public void setProgress(int progress) {
        if (mPlayer.isPlaying()) {
            mPlayer.seekTo(progress);
        }
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }

    public void setPlayMode(int mode) {
        mPlayMode = mode;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayMode == MODE_SINGLE) {
            stop();
        } else if (mPlayMode == MODE_SINGLE_LOOP) {
            play(mPath, Para.currentBook, Para.currentChapter);
        } else if (mPlayMode == MODE_ORDER) {
            Func.ChangeChapter(true);
            final File file = Func.getFilePath(Func.getFileName(mPath, Para.currentBook, Para.currentChapter));
            if (file.exists()) {
                play(mPath, Para.currentBook, Para.currentChapter);
            } else {
                Func.downChapter(this);
                playNet(mPath, Para.currentBook, Para.currentChapter);
            }
        }
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        start();
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    public void setOnPlayListener(OnPlayListener listener) {
        mOnPlayListener = listener;
    }

    public void setOnPlayChangedListener(OnPlayChangedListener listener) {
        mOnPlayChangedListener = listener;
    }

    public interface OnCompletionListener {
        void onCompletion();
    }

    public interface OnPlayListener {
        void onPlay(int progress, int duration);
    }

    public interface OnPlayChangedListener {
        void onPlayChanged(boolean isPlay);
    }

    public class MusicPlayBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }
}
