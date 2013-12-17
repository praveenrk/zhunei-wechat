package org.cathassist.daily.activity;

import org.cathassist.daily.R;
import org.cathassist.daily.provider.UpdateApp;
import org.cathassist.daily.util.PublicFunction;
import org.cathassist.daily.util.PublicFunction.OnClickCancelListener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
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
