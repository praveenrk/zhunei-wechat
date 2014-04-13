package org.cathassist.daily.activity;

import org.cathassist.daily.PrayInEveryday;
import org.cathassist.daily.database.TodoDbAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends SlidingFragmentActivity {
	ActionBar actionBar;
	TodoDbAdapter dbHelper;
	PrayInEveryday prayEveryday;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.setDebugMode(true);
		actionBar = getSupportActionBar();
		dbHelper = new TodoDbAdapter(this);
		prayEveryday = (PrayInEveryday) getApplicationContext();
		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");
		registerReceiver(mFinishReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mFinishReceiver);
		Log.e("Destroy", "Destroy");
	}

	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("finish".equals(intent.getAction())) {
				Log.e("#########", "I am " + getLocalClassName()
						+ ",now finishing myself...");
				finish();
			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("stop", "stop");
	}

}