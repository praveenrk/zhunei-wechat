package com.flyworkspace.prayineveryday.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flyworkspace.prayineveryday.PrayInEveryday;
import com.flyworkspace.prayineveryday.R;
import com.flyworkspace.prayineveryday.activity.MainActivity.DownloadData;
import com.flyworkspace.prayineveryday.provider.UpdateApp;
import com.flyworkspace.prayineveryday.provider.UpdateManager;
import com.flyworkspace.prayineveryday.util.NetworkTool;
import com.flyworkspace.prayineveryday.util.PublicFunction;
import com.flyworkspace.prayineveryday.util.PublicFunction.OnClickCancelListener;
import com.umeng.analytics.MobclickAgent;

public class Preferences extends SherlockPreferenceActivity implements
		OnPreferenceClickListener,OnClickCancelListener {
	private Preference aboutPreference, updatePreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		findPreference();
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		setListener();
		aboutPreference.setSummary(getString(R.string.version,
				PublicFunction.getVerName(Preferences.this)));
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void setListener() {
		aboutPreference.setOnPreferenceClickListener(this);
		updatePreference.setOnPreferenceClickListener(this);
	}

	private void findPreference() {
		aboutPreference = findPreference("about");
		updatePreference = findPreference("update");
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == aboutPreference) {
			PublicFunction.getTipsDialog(Preferences.this,false).show();
		} else if (preference == updatePreference) {
			ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cwjManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				UpdateApp updateApp = new UpdateApp(Preferences.this,true);
				updateApp.execute("");
			} else {
				PublicFunction.showToast(Preferences.this,
						getString(R.string.no_web));
			}
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClickCancelDo() {
		// TODO Auto-generated method stub
		
	}

}
