package com.flyworkspace.prayineveryday;

import com.flyworkspace.prayineveryday.database.TodoDbAdapter;
import com.flyworkspace.prayineveryday.provider.UpdateApp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class PrayInEveryday extends Application {
	public final static String SERVER_URL = "http://jinpengfeie.yupage.com/";
	public final static String SERVER_URL2 = "http://t.liyake.com/getstuff/getstuff.php?date=";
//	public final static String SERVER_URL ="http://192.168.137.1/web/";
	private TodoDbAdapter dbHelper;

	public TodoDbAdapter openAndGetDbHelper() {
		dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
		return dbHelper;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		getApplicationContext().sendBroadcast(new Intent("finish"));
		Log.e("APPonCreate", "APPonCreate");
		
	}
	
}
