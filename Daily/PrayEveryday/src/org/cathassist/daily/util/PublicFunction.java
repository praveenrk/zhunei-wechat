package org.cathassist.daily.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cathassist.daily.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 公共方法类
 * 
 * @author jpf
 * 
 */
public class PublicFunction {
	/**
	 * 得到年的格式化输出
	 * 
	 * @param date
	 * @return
	 */
	public static String getYearFormat(Date date) {
		String str = String.format("%tY", date);
		return str;
	}

	/**
	 * 得到月的格式化输出
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthFormat(Date date) {
		String str = String.format("%tb", date);
		return str;
	}

	/**
	 * 得到日的格式化输出
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayFormat(Date date) {
		String str = String.format("%td", date);
		return str;
	}

	/**
	 * 得到星期的格式化输出
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekFormat(Date date) {
		String str = String.format("%ta", date);
		return str;
	}

	public static String getMonthDay(Context context, Date date) {
		return getMonthFormat(date) + getDayFormat(date)
				+ context.getString(R.string.day);
	}

	public static String getYearMonthDayForSql(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(date);
	}

	public static void showToast(Activity activity, String str) {
		Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
	}

	public static Bitmap buildTabBitmap(Context ctx, int drawableId,
			Boolean isTabSelect) {
		ColorFilter tab_colorColorFilter;
		if (isTabSelect) {
			tab_colorColorFilter = new LightingColorFilter(Color.rgb(255, 126,
					0), Color.rgb(255, 90, 0));
		} else {
			tab_colorColorFilter = new LightingColorFilter(Color.WHITE,
					Color.WHITE);
			// tab_colorColorFilter=new LightingColorFilter(Color.BLACK,
			// Color.BLACK);
		}
		Bitmap d = BitmapFactory.decodeResource(ctx.getResources(), drawableId);
		Bitmap output = Bitmap.createBitmap(d.getWidth(), d.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, d.getWidth(), d.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 0;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		paint.setColorFilter(tab_colorColorFilter);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(d, rect, rect, paint);
		return output;

	}

	public static String getDayNatureString(Context context, int memorableDay) {
		switch (memorableDay) {
		case 1:
			return context.getString(R.string.memorableday);
		case 2:
			return context.getString(R.string.qingri);
		case 3:
			return context.getString(R.string.jieri);
		default:
			break;
		}
		return "";
	}

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"org.cathassist.daily", 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"org.cathassist.daily", 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;
	}

	public static AlertDialog getTipsDialog(Context context,
			final boolean updateDate) {
		AlertDialog.Builder builder;
		final AlertDialog alertDialog;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.tips, null);
		builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
				R.style.BlackDialogTheme));
		builder.setView(layout);
		builder.setTitle(R.string.app_name);
		final OnClickCancelListener candelDo = (OnClickCancelListener) context;
		// TODO
		builder.setIcon(R.drawable.ic_launcher);
		builder.setNegativeButton(context.getString(android.R.string.ok),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (updateDate) {
							candelDo.onClickCancelDo();
						}
					}
				});
		final TextView content = (TextView) layout
				.findViewById(R.id.tips_content);
		content.setText(Html.fromHtml(context.getString(R.string.tips_content,
				PublicFunction.getVerName(context))));
		final TextView contactUs = (TextView) layout
				.findViewById(R.id.contactUs);
		contactUs.setText(Html.fromHtml(context.getString(R.string.contact)));
		alertDialog = builder.create();
		return alertDialog;
	}

	public interface OnClickCancelListener {
		public void onClickCancelDo();// 接口中定义一个方法
	}
}
