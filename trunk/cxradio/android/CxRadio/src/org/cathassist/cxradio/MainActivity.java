package org.cathassist.cxradio;

import java.util.*;
import java.text.*;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import org.cathassist.cxradio.R;
import org.cathassist.cxradio.data.*;
import org.cathassist.cxradio.media.*;
import org.cathassist.utils.Common;
import org.json.*;

import com.jeremyfeinstein.slidingmenu.lib.*;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;


public class MainActivity extends Activity implements RadioEvents, OnSeekBarChangeListener, RadioDownloadEvents
{
	//定义标题栏上的按钮
	private ImageButton playlistBtn = null;
	//右侧列表
	private SlidingMenu playlistMenu = null;
	//电台播放列表显示
	private ListView playlistView = null;
	private PlayListAdapter playlistAdapter = null;
	
	//时间格式化
	private SimpleDateFormat fmDate = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
	
	//CD把手
	private PlayHandleView cdHandle = null;
	//播放、暂停按钮
	private ImageView playButton = null;
	private ImageView pauseButton = null;
	//旋转的ICON
	private PlayIconView iconButton = null;
	//上一首、下一首、当前歌曲名称
	private ImageView prevButton = null;
	private ImageView nextButton = null;
	private TextView musicText = null;

	//当前播放的时间、总时间、拖动条
	private TextView curTime = null;
	private TextView maxTime = null;
	private SeekBar seekProgress = null;
	
	//当前电台日期
	private TextView curDateText = null;
	
	private TextView downloadText = null;
	private TextView clearText = null;
	
	//下载管理
	RadioDownloadManager managerDownload = null;
	
	
	private boolean isInit = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		//设置监听电台事件
		RadioPlayer.getRadioPlayer().setRadioEventsListener(this);
		
		//实例化标题栏按钮并设置监听
		playlistBtn = (ImageButton) findViewById(R.id.playlist_btn);
//		titleBtn.setOnClickListener(this);
		playlistBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Log.d("CxRadio", "playlist btn clicked...");
				playlistMenu.toggle();
			}
		});
		
		//播放列表
		playlistMenu = new SlidingMenu(this);
		playlistMenu.setMode(SlidingMenu.RIGHT);
		playlistMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		playlistMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		playlistMenu.setFadeDegree(0.35f);
		playlistMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		playlistMenu.setMenu(R.layout.menu_playlist);
		

		//播放内容列表
		playlistView = (ListView) findViewById(R.id.playlistView);
		playlistAdapter = new PlayListAdapter(playlistMenu.getContext());
		playlistView.setAdapter(playlistAdapter);
		playlistView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int i, long l)
			{
				try
				{
					RadioPlayer.getRadioPlayer().setPlayIndex(i);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				playlistMenu.toggle();
			}
		});
		
		initPlayer();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d("Activity", "Destroy...");
		
		if(!RadioPlayer.getRadioPlayer().isPlaying())
		{
			RadioNotification.clearNotification(this);
			RadioPlayer.getRadioPlayer().release();
			RadioPlayer.getRadioPlayer().stopSelf();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);

		if(isInit == false)
		{
			if(RadioPlayer.getRadioPlayer().getChannel()!=null)
			{
				Log.d("Activity", "Init to play...");
				//恢复播放列表
				setPlayChannel(RadioPlayer.getRadioPlayer().getChannel());
			}
			else
			{
				Log.d("Activity", "Init to refresh...");
				//尝试加载缓存json文件

		        JSONParser jParser = new JSONParser();
		        // Getting JSON from URL
		        JSONObject j = jParser.getJSONFromStr(Common.readContentFromFile(RadioDownloadManager.lastChannelDir));

	        	try
	        	{
					Date dtFile = fmDate.parse(j.getString("date"));
					if(Common.isDateSame(dtFile, new Date()))
					{
						setPlayChannel(Channel.getChannelFormJson(j));
						isInit = true;
						return;
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//更新播放列表
				refreshPlayList();
			}
			isInit = true;
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//拖动条的监视
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		//拖动seekbar时候，调用这里
		if (fromUser)
		{
			RadioPlayer.getRadioPlayer().setPlay();
			RadioPlayer.getRadioPlayer().setSeekTo(progress);  //播放跳转到拖动位置
		}
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		//开始拖动seekbar
		RadioPlayer.getRadioPlayer().setPause();
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		//拖动seekbar结束
	}
	
	//Radio响应事件
	@Override
	public void onRadioItemChanged(Channel.Item item)
	{
		Log.e("RadioEvent", "onRadioItemChanged");
		musicText.setText(item.getTitle());
		RadioNotification.showNotification(this, item.getTitle());
	}
	
	@Override
	public void onRadioPrepared(int max)
	{
	//	Log.e("RadioEvent", "onRadioPrepared");
		if(seekProgress !=null )
		{
			seekProgress.setMax(max);
			seekProgress.setProgress(0);
			int seconds = max/1000;
			maxTime.setText(String.format("%02d:%02d", seconds/60,seconds%60));
			this.setPlayerStart();
		}
	}
	
	@Override
	public void onRadioStoped()
	{
	//	Log.e("RadioEvent", "onRadioStoped");
		updatePlayState(false);
	//	RadioNotification.clearNotification(this);
	}
	
	@Override
	public void onRadioPaused()
	{
	//	Log.e("RadioEvent", "onRadioPaused");
		updatePlayState(false);
	}
	
	@Override
	public void onRadioBufferedUpdate(int progress)
	{
	//	Log.e("RadioEvent", "onRadioBufferedUpdate");
		if(seekProgress != null)
			seekProgress.setSecondaryProgress(progress);
	}
	
	@Override
	public void onRadioUpdateProgress(int progress)
	{
	//	Log.e("RadioEvent", "onRadioUpdateProgress");
		if(seekProgress != null)
		{
			seekProgress.setProgress(progress);
			int seconds = progress/1000;
			curTime.setText(String.format("%02d:%02d", seconds/60,seconds%60));
		}
	}
	
	private boolean refreshPlayList()
	{
		setPlayDate(new Date());
		return true;
    }
	
	private void setPlayDate(Date d)
	{
		new LoadPlayListTask().execute("cx",fmDate.format(d));
	}
	
	private void initPlayer()
	{
		//初始化播放器里的旋转icon
		iconButton = (PlayIconView)findViewById(R.id.imageView_icon);
		iconButton.initAnimate();
		//初始化播放器的handle
		cdHandle = (PlayHandleView)findViewById(R.id.imageView_handle);
		//初始化播放/暂停按钮
		playButton = (ImageView)findViewById(R.id.imageView_play);
		pauseButton = (ImageView)findViewById(R.id.imageView_pause);
		
		playButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				setPlayerStart();
			}
		});

		pauseButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				setPlayerPause();
			}
		});
		//初始化上一首、下一首、当前歌曲
		prevButton = (ImageView)findViewById(R.id.imageView_prev);
		nextButton = (ImageView)findViewById(R.id.imageView_next);
		musicText = (TextView)findViewById(R.id.textView_music);
		prevButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				RadioPlayer.getRadioPlayer().setPlayPrev();
			}
		});
		nextButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				RadioPlayer.getRadioPlayer().setPlayNext();
			}
		});
		
		//初始化进度控制控件
		seekProgress = (SeekBar)findViewById(R.id.seekBar_progress);
		curTime = (TextView)findViewById(R.id.textView_current);
		maxTime = (TextView)findViewById(R.id.textView_max);
		seekProgress.setOnSeekBarChangeListener(this);
		
		//日期选择
		//初始化上一日、下一日、当前日期
		curDateText = (TextView)findViewById(R.id.textView_day);
		TextView prevDay = (TextView)findViewById(R.id.textView_prev);
		TextView nextDay = (TextView)findViewById(R.id.textView_next);
		prevDay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Date d = new Date();
				d.setTime(RadioPlayer.getRadioPlayer().getChannel().date.getTime()-3600*24000);
				setPlayDate(d);
			}
		});
		nextDay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Date d = new Date();
				d.setTime(RadioPlayer.getRadioPlayer().getChannel().date.getTime()+3600*24000);
				setPlayDate(d);
			}
		});
		curDateText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(RadioPlayer.getRadioPlayer().getChannel().date);
				DatePickerDialog dlg = new DatePickerDialog(MainActivity.this,
					new DatePickerDialog.OnDateSetListener(){
						@Override
						public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
						{
							Calendar cal1 = Calendar.getInstance();
							cal1.set(arg1, arg2, arg3);
							setPlayDate(cal1.getTime());
						}
					},
					cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH));
				dlg.show();
			}
		});
		
		downloadText = (TextView)findViewById(R.id.textView_download);
		clearText = (TextView)findViewById(R.id.textView_clear);
		downloadText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				setDownloadChannel();
			}
		});
		
		clearText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				setClearAll();
			}
		});
		
		//发送反馈、晨星与小助手的copyright
		TextView feedback = (TextView)findViewById(R.id.textView_feedback);
		TextView cxcopyright = (TextView)findViewById(R.id.textView_cxcopyright);
		TextView cacopyright = (TextView)findViewById(R.id.textView_cacopyright);

		feedback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
			    agent.startFeedbackActivity();
			}
		});
		
		cxcopyright.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				openUrlInBrowser("http://www.cxsm.org/");
			}
		});
		cacopyright.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				openUrlInBrowser("http://www.cathassist.org/");
			}
		});
	}
	
	private void setPlayChannel(Channel c)
	{
		RadioPlayer.getRadioPlayer().setChannel(c);
		playlistAdapter.setChannel(c);
		
		if(seekProgress!=null)
		{
			//更新进度条
			int max = RadioPlayer.getRadioPlayer().getDuration();
			seekProgress.setMax(max);
			int seconds = max/1000;
			maxTime.setText(String.format("%02d:%02d", seconds/60,seconds%60));
			
			int progress = RadioPlayer.getRadioPlayer().getCurrentPosition();
			seekProgress.setProgress(progress);
			seconds = progress/1000;
			curTime.setText(String.format("%02d:%02d", seconds/60,seconds%60));
		}

		//更新界面上的播放状态
		updatePlayState(RadioPlayer.getRadioPlayer().isPlaying());
		
		Channel.Item item = RadioPlayer.getRadioPlayer().getCurItem();
		if(item!=null)
		{
			musicText.setText(item.getTitle());
		}
		else
		{
			musicText.setText(c.items.get(0).getTitle());
		}
		
		//设置当前显示日期
		curDateText.setText(fmDate.format(c.date));
	}
	
	//开始播放
	private void setPlayerStart()
	{
		if(playButton.getVisibility() == ImageView.INVISIBLE)
			return;
		
		RadioPlayer.getRadioPlayer().setPlay();
		updatePlayState(true);
	}
	
	//暂停播放
	private void setPlayerPause()
	{
		if(pauseButton.getVisibility() == ImageView.INVISIBLE)
			return;
		
		RadioPlayer.getRadioPlayer().setPause();
		updatePlayState(false);
	}
	
	private void updatePlayState( boolean isPlaying)
	{
		if(isPlaying)
		{
			iconButton.setStart();
			playButton.setVisibility(ImageView.INVISIBLE);
			pauseButton.setVisibility(ImageView.VISIBLE);
			cdHandle.setOn(1000);
		}
		else
		{
			iconButton.setPause();
			playButton.setVisibility(ImageView.VISIBLE);
			pauseButton.setVisibility(ImageView.INVISIBLE);
			cdHandle.setOff(1000);
		}
	}
	
	public void openUrlInBrowser(String url)
	{
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		this.startActivity(it);
	}
	
	public void setDownloadChannel()
	{
		if(managerDownload!=null)
		{
			managerDownload.cancel();
			managerDownload=null;
			downloadText.setText(R.string.text_download);
			RadioPlayer.getRadioPlayer().getChannel().updateLoadings();
			playlistAdapter.notifyDataSetChanged();
			Toast.makeText(this, "已取消下载", Toast.LENGTH_SHORT).show();
			return;
		}
		//下载当前音频
		downloadText.setText(R.string.text_cancel);
		managerDownload = new RadioDownloadManager(RadioPlayer.getRadioPlayer().getChannel(),this,this);
		managerDownload.run();
		Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
	}
	
	public void setClearAll()
	{
		//清空所有音频
		clearText.setEnabled(false);
		RadioDownloadManager.clearAllTracks();
		clearText.setEnabled(true);
		RadioPlayer.getRadioPlayer().getChannel().updateLoadings();
		playlistAdapter.notifyDataSetChanged();
		Toast.makeText(this, "已清空下载", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onRadioDownloadItemChanged(Channel.Item item)
	{
		if(managerDownload==null)
			return;
		Log.d("MainActivity", "UpdateItem...");
		playlistAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRadioDownloadFinished()
	{
		managerDownload=null;
		downloadText.setText(R.string.text_download);
		RadioPlayer.getRadioPlayer().getChannel().updateLoadings();
		playlistAdapter.notifyDataSetChanged();
		Toast.makeText(this, "全部下载完成", Toast.LENGTH_SHORT).show();
	}
	
	//异步获取电台播放列表的Task
	private class LoadPlayListTask extends AsyncTask<String, Void, Channel>
	{
		ProgressDialog dialog;
		protected void onPreExecute()
		{
            dialog = ProgressDialog.show(MainActivity.this ,"加载中...","正在加载播放列表");
		}
		
		protected Channel doInBackground(String... params)
		{
			String strChannel = "cx";
			String strDate = fmDate.format(new Date());
			if(params.length>1)
			{
				strChannel = params[0];
				strDate = params[1];
			}

			String strUrl = "http://www.cathassist.org/radio/getradio.php?channel="+strChannel+"&date="+strDate;
	        
	        JSONParser jParser = new JSONParser();
	        // Getting JSON from URL
	        JSONObject j = jParser.getJSONFromUrl(strUrl);
	        Channel c = Channel.getChannelFormJson(j);
	        if(Common.isDateSame(c.date, new Date()))
	        {
	        	Common.writeContentToFile(RadioDownloadManager.lastChannelDir, j.toString());
	        }
	        
			return c;
		}
		
		protected void onPostExecute(Channel c)
		{
			if(c.items.size()>0)
				setPlayChannel(c);
			dialog.dismiss();
		}
	}
}
