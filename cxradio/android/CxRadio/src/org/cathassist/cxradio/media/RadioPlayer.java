package org.cathassist.cxradio.media;

import java.util.*;

import org.cathassist.cxradio.data.*;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.*;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.*;
import android.util.Log;

public class RadioPlayer extends Service implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnInfoListener, OnErrorListener
{
	private Context context = null;
	private Channel channel = null;
	private MediaPlayer player = null;
	private Channel.Item curItem = null;
	private static RadioEvents radioEvents = null;

    private Handler handler = new Handler();
/*    {
    	public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				if(player!=null)
				{
					radioEvents.onRadioUpdateProgress(player.getCurrentPosition());
				}
				break;      
			}
			super.handleMessage(msg);
		}
	};*/
	
	private Runnable runnableUpdateProgress = new Runnable()
	{
		public void run()
		{
			radioEvents.onRadioUpdateProgress(player.getCurrentPosition());
			handler.postDelayed(this,1000);
		}
	};
		
	
	public static void setRadioEventsListener(RadioEvents e)
	{
		radioEvents = e;
	}
    
	public RadioPlayer(Context context,Channel channel)
	{
		this.context = context;
		this.channel = channel;
	}
	

	@Override
	public void onCreate()
	{
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra)
	{  //播放过程中的一些返回信息
		switch (what)
		{
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:  //进入缓冲
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:  //结束缓冲
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		//出错时候，会回调这里
		switch (what)
		{
		case MediaPlayer.MEDIA_ERROR_UNKNOWN :
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		//一首播放完毕，继续下手
		setPlayNext();
	}
	

	@Override
	public void onPrepared(MediaPlayer mp)
	{
		//初始化播放回调方法
		if(player != null)
		{
			radioEvents.onRadioPrepared(mp.getDuration());
			player.start();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent)
	{
		//获取缓冲进度
		radioEvents.onRadioBufferedUpdate(mp.getDuration() * percent / 100);
	}
	
	private MediaPlayer createPlayer(String src)
	{
		MediaPlayer p = new MediaPlayer();
		
		p.setOnBufferingUpdateListener(this); //监听缓冲数据
		p.setOnPreparedListener(this);  //监听初始化
		p.setOnCompletionListener(this); //监听是否播放完了
		p.setOnErrorListener(this);   //监听出错信息
		p.setOnInfoListener(this);   //监听播放过程中，返回的信息
		
		try
		{
			p.reset();
			p.setDataSource(src);
			p.prepareAsync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		handler.removeCallbacks(runnableUpdateProgress);
		handler.postDelayed(runnableUpdateProgress, 1000);
		
		return p;
	}
	
	private void releasePlayer(MediaPlayer p)
	{
		p.stop();
		p.release();
		handler.removeCallbacks(runnableUpdateProgress);
	}
	
	public void setChannel(Channel c)
	{
		channel = c;
	}
	
	public Channel getChannel()
	{
		return channel;
	}
	
	public void setPlayIndex(int i)
	{
		Log.d("Player", "index:"+i);
		if(channel!=null)
		{
			if(i>-1 && channel.items.size()>i)
			{
				curItem = channel.items.get(i);
				if(radioEvents!=null)
				{
					radioEvents.onRadioItemChanged(curItem);
				}
				
				setPlaySrc(curItem.src);
			}
		}
	}
	
	private void setPlaySrc(String src)
	{
		Log.d("Player", "src:"+src);
		if(player != null)
		{
			releasePlayer(player);
			player = null;
		}
		
		player = createPlayer(src);
	}
	
	public void setPlay()
	{
		try
		{
			if(player == null)
			{
				setPlayIndex(0);
			}
			else
			{
				player.start();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setPause()
	{
		if(player != null)
		{
			player.pause();
		}
	}
	
	public void setStop()
	{
		if(player != null)
		{
			player.stop();
			radioEvents.onRadioStoped();
		}
	}

	//播放上一首
	public void setPlayPrev()
	{
		int i = channel.items.indexOf(curItem)-1;
		setPlayIndex(i);
	}
	
	//播放下一首
	public void setPlayNext()
	{
		int i = channel.items.indexOf(curItem)+1;
		setPlayIndex(i);
	}
	
	
	public void release()
	{
		setStop();
		if(player != null)
		{
			releasePlayer(player);
		}
	}
	
}
