package com.flyworkspace.prayineveryday.provider;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.flyworkspace.prayineveryday.PrayInEveryday;
import com.flyworkspace.prayineveryday.R;
import com.flyworkspace.prayineveryday.activity.Preferences;
import com.flyworkspace.prayineveryday.util.NetworkTool;
import com.flyworkspace.prayineveryday.util.PublicFunction;

public class UpdateApp extends AsyncTask<String, Integer, String> {
	private int newVerCode;
	private String newVerName;
	private final static int TIME_OUT = 20 * 1000;
	private boolean done = false;
	ProgressDialog progressDialog;
	private Activity context;
	private boolean showProgressDialog;

	public UpdateApp(Activity context, boolean showProgressDialog) {
		super();
		this.context = context;
		this.showProgressDialog = showProgressDialog;
	}

	private void cancelSelfWhenTimeOut() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				if (!done) {
					UpdateApp.this.cancel(true);
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
				PublicFunction.showToast(context,
						context.getString(R.string.update_timeout));
				if(showProgressDialog)
				progressDialog.cancel();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected String doInBackground(String... arg0) {
		try {
			cancelSelfWhenTimeOut();
			String verjson = NetworkTool.getContent(PrayInEveryday.SERVER_URL
					+ "update.php");
			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("versioncode"));
					newVerName = obj.getString("versionName");
				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return "no";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "no";
		} finally {
			done = true;
		}
		return newVerCode > PublicFunction.getVerCode(context) ? "success"
				: "no";
	}

	/**
	 * 该方法将在执行实际的后台操作前被UI thread调用。 可以在该方法中做一些准备工作，如在界面上显示一个进度条。
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (showProgressDialog) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			progressDialog.setTitle(context.getString(R.string.hold_on));// 设置标题
			progressDialog.setMessage(context.getString(R.string.checking_app));
			PublicFunction.showToast(context,
					context.getString(R.string.checking_app));
			progressDialog.setIndeterminate(false);// 设置进度条是否为不明确
			progressDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
			progressDialog.show();
		}
	}

	/**
	 * 在publishProgress方法被调用后，UI thread将调用这个方法从而在界面上展示任务的进展情况， 例如通过一个进度条进行展示
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用， 后台的计算结果将通过该方法传递到UI
	 * thread
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result.equals("success")) {
			UpdateManager updateManager = new UpdateManager(context);
			updateManager.showNoticeDialog();
		} else {
			if (showProgressDialog)
			Toast.makeText(context, R.string.soft_update_no, Toast.LENGTH_LONG)
					.show();
		}
		if (showProgressDialog)
			progressDialog.cancel();
	}
}
