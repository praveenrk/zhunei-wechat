package org.cathassist.daily.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.cathassist.daily.PrayInEveryday;
import org.cathassist.daily.R;
import org.cathassist.daily.activity.MainFragment.OnArticleSelectedListener;
import org.cathassist.daily.bean.CalendarDay;
import org.cathassist.daily.bean.DayContent;
import org.cathassist.daily.database.TodoDbAdapter;
import org.cathassist.daily.provider.MainActivityFragmentPagerAdapter;
import org.cathassist.daily.provider.UpdateApp;
import org.cathassist.daily.provider.EnumManager.ContentType;
import org.cathassist.daily.util.GetSharedPreference;
import org.cathassist.daily.util.NetworkTool;
import org.cathassist.daily.util.PublicFunction;
import org.cathassist.daily.util.PublicFunction.OnClickCancelListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends BaseActivity implements
		OnClickCancelListener, OnArticleSelectedListener {
	
	
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	ProgressDialog progressDialog;
	// private SatelliteMenu mMenu;
	protected ListFragment mFrag;
	MainActivityFragmentPagerAdapter test;
	private Calendar calendar = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (NetworkTool.isConnectNet(this)) {
			UpdateApp updateApp = new UpdateApp(this, false);
			updateApp.execute("");
		}
		setContentView(R.layout.activity_main);
		// mMenu = (SatelliteMenu) findViewById(R.id.menu);
		initViewPager(10, 0xFFFFFFFF, 0xFF000000,
				fragments(PublicFunction.getYearMonthDayForSql(calendar
						.getTime())));
		showTipsDialog();
		// addItemsToMenu();
		setSlidingMenu(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("MainonResume", "MainonResume");
	}

	private void setSlidingMenu(Bundle savedInstanceState) {
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new DateListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}

		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSlidingActionBarEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main_actionbar, menu);
		return true;
	}

	/**
	 * 加载Pager
	 * 
	 * @author jpf
	 * @param pageCount
	 * @param backgroundColor
	 * @param textColor
	 */
	private void initViewPager(int pageCount, int backgroundColor,
			int textColor, ArrayList<Fragment> fragments) {
		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		test = new MainActivityFragmentPagerAdapter(this,
				getSupportFragmentManager(), fragments);

		mPager.setAdapter(test);
		mIndicator.setViewPager(mPager);
		mPager.setCurrentItem(0);
		mIndicator.notifyDataSetChanged();
		mIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						switch (position) {
						case 0:
							getSlidingMenu().setTouchModeAbove(
									SlidingMenu.TOUCHMODE_FULLSCREEN);
							break;
						default:
							getSlidingMenu().setTouchModeAbove(
									SlidingMenu.TOUCHMODE_MARGIN);
							break;
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
						// test.notifyDataSetChanged();
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.main_actionbar_calendar:
			Intent intentCalendar=new Intent(MainActivity.this, CalendarListActivity.class);
			startActivity(intentCalendar);
			break;
		case R.id.main_actionbar_update:
			updateDate();
			break;
		case R.id.menu_preferences:
			Intent intent = new Intent(MainActivity.this, Preferences.class);
			startActivity(intent);
			break;
		case R.id.menu_exit:
			getApplicationContext().sendBroadcast(new Intent("finish"));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class DownloadData extends AsyncTask<String, Integer, String> {
		private final static int TIME_OUT = 200 * 1000;
		private boolean done = false;

		@Override
		protected String doInBackground(String... arg0) {
			try {
				cancelSelfWhenTimeOut();

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, 0);
				String startTime = PublicFunction
						.getYearMonthDayForSql(calendar.getTime());
				calendar.add(Calendar.DATE, 14);
				String endTime = PublicFunction.getYearMonthDayForSql(calendar
						.getTime());
				calendar.add(Calendar.DATE, -15);
				String deleteTime = PublicFunction
						.getYearMonthDayForSql(calendar.getTime());
				String httpUrl1 = PrayInEveryday.SERVER_URL
						+ "everyday1.php?startdate=" + startTime + "&enddate="
						+ endTime;

				dbHelper.open();
				dbHelper.deleteOldData(deleteTime);
				String[] updateTime = dbHelper.getDaysUpdateTimeByDate(
						startTime, endTime);
				dbHelper.close();
				for (int i = 0; i < updateTime.length; i++) {
					httpUrl1 += "&update" + i + "=" + updateTime[i];
				}
				String result1 = NetworkTool.getContent(httpUrl1);
				if (result1.contains("NoNewData"))
					return "NoNewData";
				CalendarDay calendarDay;
				DayContent dayContent;
				dbHelper.open();
				JSONArray jArray;
				JSONObject jsonObject;
				try {
					calendarDay = new CalendarDay();
					jArray = new JSONArray(result1);
					JSONObject json_data = null;
					for (int i = 0; i < jArray.length(); i++) {
						json_data = jArray.getJSONObject(i);
						calendarDay = new CalendarDay();
						calendarDay.setDate(json_data.getString("date"));
						calendarDay.setDayType(json_data.getInt("daytype"));
						calendarDay.setSummary(json_data.getString("summary"));
						calendarDay
								.setFestival(json_data.getString("festival"));
						calendarDay.setMemorableDay(json_data
								.getInt("ismemorableday"));
						calendarDay.setSolarTerms(json_data
								.getString("solarterms"));
						calendarDay.setHoliday(json_data.getString("holiday"));
						calendarDay.setBible(json_data.getString("bible"));
						calendarDay.setPray(json_data.getString("pray"));
						calendarDay.setUpdateTime(json_data
								.getString("updatetime"));
						dbHelper.insertCalendarDay(calendarDay);

						String httpUrl2 = PrayInEveryday.SERVER_URL2
								+ json_data.getString("date");
						String result2 = NetworkTool.getContent(httpUrl2);
						dayContent = new DayContent();
						dayContent.setDate(json_data.getString("date"));
						dayContent.setUpdateTime(json_data
								.getString("updatetime"));
						jsonObject = new JSONObject(result2);
						for (int j = 0; j < ContentType.values().length; j++) {
							dayContent.setContentType(j);
							dayContent
									.setContent(jsonObject.getString(ContentType
											.getContentDataNameFromContentType(j)));
							dbHelper.insertContent(dayContent);
						}
					}
				} catch (JSONException e1) {
					MobclickAgent.reportError(MainActivity.this, e1);
					e1.printStackTrace();
					return "fail";
				} catch (ParseException e1) {
					MobclickAgent.reportError(MainActivity.this, e1);
					e1.printStackTrace();
					return "fail";
				} finally {
					dbHelper.close();
					done = true;
				}
				// }
				return "success";
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				done = true;
			}
			return "fail";
		}

		private void cancelSelfWhenTimeOut() {
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					if (!done) {
						DownloadData.this.cancel(true);
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}, TIME_OUT);
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					PublicFunction.showToast(MainActivity.this,
							getString(R.string.update_timeout));
					progressDialog.cancel();
					break;
				}
				super.handleMessage(msg);
			}

		};

		/**
		 * 该方法将在执行实际的后台操作前被UI thread调用。 可以在该方法中做一些准备工作，如在界面上显示一个进度条。
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			progressDialog.setTitle(getString(R.string.hold_on));// 设置标题
			progressDialog.setMessage(getString(R.string.updating));
			PublicFunction.showToast(MainActivity.this,
					getString(R.string.begin_update));
			progressDialog.setIndeterminate(false);// 设置进度条是否为不明确
			progressDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
			progressDialog.show();
		}

		/**
		 * 在publishProgress方法被调用后，UI thread将调用这个方法从而在界面上展示任务的进展情况， 例如通过一个进度条进行展示
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		/**
		 * 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，
		 * 后台的计算结果将通过该方法传递到UI thread
		 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("fail"))
				PublicFunction.showToast(MainActivity.this,
						getString(R.string.update_fail));
			else if (result.equals("NoNewData"))
				PublicFunction.showToast(MainActivity.this,
						getString(R.string.no_update));
			else
				PublicFunction.showToast(MainActivity.this,
						getString(R.string.update_success));
			progressDialog.cancel();
			ArrayList<Fragment> fragments = fragments(PublicFunction
					.getYearMonthDayForSql(calendar.getTime()));
			test.setFragments(fragments);
			initViewPager(10, 0xFFFFFFFF, 0xFF000000,
					fragments(PublicFunction.getYearMonthDayForSql(calendar
							.getTime())));
			FragmentTransaction t = MainActivity.this
					.getSupportFragmentManager().beginTransaction();
			t.remove(mFrag);
			mFrag = new DateListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mPager.getCurrentItem() == 0)
				dialog();
			else
				mPager.setCurrentItem(0);
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.exit_tip);
		builder.setTitle(R.string.tip);
		builder.setPositiveButton(android.R.string.ok,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intentExit = new Intent();
						intentExit.setAction("finish");
						sendBroadcast(intentExit);
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	protected void updateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.first);
		builder.setTitle(R.string.tip);
		builder.setPositiveButton(android.R.string.ok,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						updateDate();
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void showTipsDialog() {
		if (GetSharedPreference.getVersionCode(MainActivity.this) < PublicFunction
				.getVerCode(MainActivity.this)) {
			PublicFunction.getTipsDialog(MainActivity.this, true).show();
			GetSharedPreference.setVersionCode(MainActivity.this,
					PublicFunction.getVerCode(MainActivity.this));
		}
	}

	public void switchContent(String date) {
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
		ArrayList<Fragment> fragments = fragments(date);
		test.setFragments(fragments);
		initViewPager(10, 0xFFFFFFFF, 0xFF000000, fragments);
	}

	public ArrayList<Fragment> fragments(String date) {
		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				fragmentList.add(MainFragment.newInstance(date));
				Log.e("date", date);
			} else {
				PrayFragment fragment = PrayFragment.newInstance(date);
				switch (i) {
				case 1:
					fragment.setContentType(0);
					break;
				case 2:
					fragment.setContentType(1);
					break;
				case 3:
					fragment.setContentType(4);
					break;
				case 4:
					fragment.setContentType(2);
					break;
				case 5:
					fragment.setContentType(3);
					break;
				case 6:
					fragment.setContentType(5);
					break;
				default:
					break;
				}
				fragmentList.add(fragment);
			}
		}
		return fragmentList;
	}

	@Override
	public void onClickCancelDo() {
		updateDialog();
	}

	private void updateDate() {
		if (NetworkTool.isConnectNet(this)) {
			DownloadData downloadData = new DownloadData();
			downloadData.execute("");
		} else {
			PublicFunction.showToast(MainActivity.this,
					getString(R.string.no_web));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onArticleSelected(int page) {
		mPager.setCurrentItem(page);

	}

}
