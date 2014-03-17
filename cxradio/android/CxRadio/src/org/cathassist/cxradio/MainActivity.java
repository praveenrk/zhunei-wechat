package org.cathassist.cxradio;

import java.util.*;
import java.text.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import org.cathassist.cxradio.R;
import org.cathassist.cxradio.data.*;
import org.cathassist.cxradio.media.*;
import org.json.*;

import com.jeremyfeinstein.slidingmenu.lib.*;


public class MainActivity extends Activity
{
	//定义标题栏上的按钮
	private ImageButton playlistBtn = null;
	//右侧列表
	private SlidingMenu playlistMenu = null;
	//电台播放列表显示
	private ListView playlistView = null;
	//
	private SimpleDateFormat fmDate = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
	//电台播放器
	private RadioPlayer player = new RadioPlayer();
	
	private RelativeLayout playerContainer = null;
	private ImageView cdBackground = null;
	private ImageView cdHandle = null;
	private ImageView playButton = null;
	private ImageView pauseButton = null;
	private PlayIconView iconButton = null;
	private ImageView prevButton = null;
	private ImageView nextButton = null;
	private TextView musicText = null;
	private SeekBar seekProgress = null;
	private TextView curDateText = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
		
		playerContainer = (RelativeLayout)findViewById(R.id.player_container);
		
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
		playlistView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int i, long l)
			{
				try
				{
					player.setPlayIndex(i);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				playlistMenu.toggle();
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
		{
			if(playButton == null)
			{
				initPlayer();
	
				//更新播放列表
				refreshPlayList();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		int iWidth = playerContainer.getWidth();
		int iHeight = playerContainer.getHeight();
		double fDegree = (float)(iWidth)*0.1;
		{
			//初始化播放器背景
			cdBackground = new ImageView(this);
			cdBackground.setImageResource(R.drawable.cd_ctrl);
			int iW = (int)(fDegree*8);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iW,ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins((int)fDegree, (int)fDegree, (int)fDegree, (int)(iHeight-iW-fDegree-10));
			cdBackground.setLayoutParams(params);
			playerContainer.addView(cdBackground);
			
			//初始化播放器里的旋转icon
			iconButton = new PlayIconView(this);
//			iconButton = new ImageView(this);
			iconButton.setImageResource(R.drawable.cx_icon);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(iW,ViewGroup.LayoutParams.WRAP_CONTENT);
			params2.setMargins((int)(fDegree*2.9), (int)(fDegree*1.1), (int)(fDegree*2.9), (int)(iHeight-iW-fDegree-10));
			iconButton.setLayoutParams(params2);
			playerContainer.addView(iconButton);
		}
		{
			//初始化播放器的handle
			cdHandle = new ImageView(this);
			cdHandle.setImageResource(R.drawable.turntable_ctrl);
			int iW = (int)(fDegree*4);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iW,ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins((int)(fDegree*5.7), (int)(fDegree*1.5), (int)(fDegree*0.3), (int)(iHeight-fDegree*8));
			cdHandle.setLayoutParams(params);
			playerContainer.addView(cdHandle);
		}
		{
			//初始化播放/暂停按钮
			playButton = new ImageView(this);
			pauseButton = new ImageView(this);
			playButton.setImageResource(R.drawable.play_ctrl);
			pauseButton.setImageResource(R.drawable.pause_ctrl);
			playButton.setVisibility(ImageView.VISIBLE);
			pauseButton.setVisibility(ImageView.INVISIBLE);
			
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
			
			int iW = (int)(fDegree*8/6);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iW,ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins((int)((iWidth-iW)/2), (int)((iWidth-iW)/2), (int)((iWidth-iW)/2), (int)(iHeight-(iWidth-iW)/2-iW));
			playButton.setLayoutParams(params);
			pauseButton.setLayoutParams(params);
			playerContainer.addView(playButton);
			playerContainer.addView(pauseButton);
		}
		{
			//初始化上一首、下一首、当前歌曲
			prevButton = new ImageView(this);
			nextButton = new ImageView(this);
			prevButton.setImageResource(R.drawable.prev_ctrl);
			nextButton.setImageResource(R.drawable.next_ctrl);
			musicText = (TextView)findViewById(R.id.cur_music_name);
			musicText.setText("晨星生命之音-因爱而相聚");
			
			int iH = (int)(fDegree*9.5);
			int iW = (int)fDegree;
			
			{
				//上一首的显示位置
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iW,ViewGroup.LayoutParams.WRAP_CONTENT);
				params.setMargins((int)(fDegree*0.2), iH, (int)(iWidth-iW), (int)(iHeight-iH-iW-fDegree*0.2));
				prevButton.setLayoutParams(params);
			}
			{
				//下一首的显示位置
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iW,ViewGroup.LayoutParams.WRAP_CONTENT);
				params.setMargins((int)(fDegree*8.8), iH, (int)(iWidth-fDegree*0.2), (int)(iHeight-iH-iW-fDegree*0.2));
				nextButton.setLayoutParams(params);
			}
			{
				//当前歌曲的显示位置
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)musicText.getLayoutParams();
				params.topMargin = (int)(iH+iW*0.2);
			}
			
			playerContainer.addView(prevButton);
			playerContainer.addView(nextButton);
		}
		{
			//初始化进度控制控件
			seekProgress = new SeekBar(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			params.topMargin = (int)(fDegree*11);
			seekProgress.setLayoutParams(params);
			
			playerContainer.addView(seekProgress);
		}
		
		{
			//日期选择
			//初始化上一日、下一日、当前日期
			curDateText = new TextView(this);
			TextView prevDay = new TextView(this);
			TextView nextDay = new TextView(this);
			curDateText.setTextAppearance(this, R.style.TextStyle);
			prevDay.setTextAppearance(this, R.style.TextStyle);
			nextDay.setTextAppearance(this, R.style.TextStyle);
			prevDay.setText("上一日");
			nextDay.setText("下一日");
			curDateText.setText("2014-03-09");
			
			{
				//上一日的显示位置
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				params.topMargin = (int)(fDegree*12);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params.leftMargin = (int)(fDegree*0.5);
				prevDay.setLayoutParams(params);
				prevDay.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v)
					{
						Date d = new Date();
						d.setTime(player.getChannel().date.getTime()-3600*24000);
						setPlayDate(d);
					}
				});
			}
			{
				//下一日的显示位置
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				params.topMargin = (int)(fDegree*12);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.rightMargin = (int)(fDegree*0.5);
				nextDay.setLayoutParams(params);
				nextDay.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v)
					{
						Date d = new Date();
						d.setTime(player.getChannel().date.getTime()+3600*24000);
						setPlayDate(d);
					}
				});
			}
			{
				//当前日期的显示位置
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.topMargin= (int)(fDegree*12);
				curDateText.setLayoutParams(params);
				curDateText.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v)
					{
						Calendar cal = Calendar.getInstance();
						cal.setTime(player.getChannel().date);
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
			}
			
			playerContainer.addView(prevDay);
			playerContainer.addView(nextDay);
			playerContainer.addView(curDateText);
		}
	}
	
	//开始播放
	private void setPlayerStart()
	{
		iconButton.setStart();
		player.setPlay();
		playButton.setVisibility(ImageView.INVISIBLE);
		pauseButton.setVisibility(ImageView.VISIBLE);
	}
	
	//暂停播放
	private void setPlayerPause()
	{
		iconButton.setPause();
		player.setPause();
		playButton.setVisibility(ImageView.VISIBLE);
		pauseButton.setVisibility(ImageView.INVISIBLE);
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

			Channel c = new Channel();
			String strUrl = "http://www.cathassist.org/radio/getradio.php?channel="+strChannel+"&date="+strDate;
	        
	        JSONParser jParser = new JSONParser();
	        // Getting JSON from URL
	        JSONObject j = jParser.getJSONFromUrl(strUrl);
	        try
	        {
	        	c.title = j.getString("title");
	        	c.date = fmDate.parse(j.getString("date"));
	        	c.logo = j.getString("logo");
	        	JSONArray items = j.getJSONArray("items");
	        	for(int i = 0; i<items.length(); ++i)
	        	{
	        		Channel.Item item = new Channel.Item();
	        		item.title = items.getJSONObject(i).getString("title");
	        		item.src = items.getJSONObject(i).getString("src");
	        		c.items.add(item);
	        	}
			}
	        catch (JSONException e)
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
			return c;
		}
		
		protected void onPostExecute(Channel c)
		{
			player.setChannel(c);
			List<String> l = new ArrayList<String>();
			for(int i=0;i<c.items.size();++i)
			{
				l.add(c.items.get(i).title);
			}
			
			playlistView.setAdapter(
					new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,l)
			);
			
			//设置当前显示日期
			curDateText.setText(fmDate.format(c.date));
			
			dialog.dismiss();
		}
	}
}
