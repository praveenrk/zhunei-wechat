package org.cathassist.daily.calendar;

import java.util.Calendar;

import org.cathassist.daily.R;
import org.cathassist.daily.R.drawable;
import org.cathassist.daily.activity.CalendarActivity;
import org.cathassist.daily.util.Lunar;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author：zp
 * @version 创建时间：2011-3-20下午04:35:47
 * @asfaf :日历每一个对应的操作：画数字，画图
 * 
 */

public class DateWidgetDayCell extends View {
	public static int ANIM_ALPHA_DURATION = 100;
	// fields
	private final static int iMargin = 1;

	// fields
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;
	// fields
	// private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private String sDate = "";

	// fields
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bHoliday = false;
	private boolean bTouchedDown = false;

	private Context context;
	private int iconPositionX;
	private int iconPositionY;
	private int[][] eventStatus;
	/* 某一天在日历中对应的位置 */
	private int dayPosition;
	private int[] mShowEvent = new int[17];
	private final int MAXEVENTNUM = 4;
	private final int[] mEventImage = { R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings, R.drawable.ic_btn_settings,
			R.drawable.ic_btn_settings };

	// methods
	public DateWidgetDayCell(Context context, int iWidth, int iHeight,
			int[] showEvent) {
		super(context);
		this.context = context;
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		this.mShowEvent = showEvent;
	}

	/**
	 * 取得当前日历页面有需要统计活动的信息
	 * 
	 * @param eventStatus
	 */
	public void setEventStatus(int[][] eventStatus) {
		this.eventStatus = eventStatus;
	}

	public boolean getSelected() {
		return this.bSelected;
	}

	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
		}
		this.invalidate();
	}

	/**
	 * 取得从DateWidget页面传过来的参数
	 * 
	 * @param iYear
	 * @param iMonth
	 * @param iDay
	 * @param bToday
	 * @param bHoliday
	 * @param iActiveMonth
	 * @param dayPosition
	 */
	public void setData(int iYear, int iMonth, int iDay, boolean bToday,
			boolean bHoliday, int iActiveMonth, int dayPosition) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;
		this.dayPosition = dayPosition;

		this.sDate = Integer.toString(iDateDay);
		this.bIsActiveMonth = (iDateMonth == iActiveMonth);
		this.bToday = bToday;
		this.bHoliday = bHoliday;
	}

	// public void setItemClick(OnItemClick itemClick) {
	// this.itemClick = itemClick;
	// }

	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			// doItemClick();
		}
		return bResult;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyUp(keyCode, event);
		return bResult;
	}

	// public void doItemClick() {
	// if (itemClick != null)
	// itemClick.OnClick(this);
	// }

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		invalidate();
	}

	public Calendar getDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// init rectangles
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// drawing
		final boolean bFocused = isViewFocused();

		drawDayView(canvas, bFocused);
		drawDayNumber(canvas, bFocused);
	}

	/**
	 * 画日历的每一格（背景，颜色）
	 * 
	 * @param canvas
	 * @param bFocused
	 */
	private void drawDayView(Canvas canvas, boolean bFocused) {
		if (bSelected || bFocused) {
			LinearGradient lGradBkg = null;

			if (bFocused) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						DayStyle.iColorBkgFocus, DayStyle.iColorBkgFocus,
						Shader.TileMode.CLAMP);
			}

			if (bSelected) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						DayStyle.iColorBkgToday, DayStyle.iColorBkgToday,
						Shader.TileMode.CLAMP);
			}

			if (lGradBkg != null) {
				pt.setShader(lGradBkg);
				canvas.drawRect(rect, pt);
			}

			pt.setShader(null);

		} else {
			pt.setColor(DayStyle.getColorBkg(bHoliday, false));
			// 设置非当前月日历的阴影
			if (bIsActiveMonth)
				pt.setAlpha(DayStyle.iAlphaInactiveMonth);
			canvas.drawRect(rect, pt);
		}
	}

	/**
	 * 画日历的每一格的日期，对应活动的图标
	 * 
	 * @param canvas
	 * @param bFocused
	 */
	public void drawDayNumber(Canvas canvas, boolean bFocused) {
		// draw day number
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		if (CalendarActivity.getGridWidth(context)[0] < CalendarActivity
				.getGridWidth(context)[1])
			pt.setTextSize(CalendarActivity.getGridWidth(context)[0] / 3);
		else
			pt.setTextSize(CalendarActivity.getGridWidth(context)[1] / 3);

		pt.setUnderlineText(false);
		if (bToday)
			pt.setUnderlineText(true);

		int iTextPosX = (int) rect.right - (int) pt.measureText(sDate);
		int iTextPosY = (int) rect.bottom + (int) (-pt.ascent())
				- getTextHeight();

		iTextPosX -= ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDate) >> 1);
		iTextPosY -= ((int) rect.height() >> 1) - (getTextHeight() >> 1);

		// draw text
		if (bSelected || bFocused) {
			if (bSelected)
				pt.setColor(DayStyle.iColorTextToday);
			pt.setUnderlineText(true);
			if (bFocused)
				pt.setColor(DayStyle.iColorTextFocus);
		} else {
			pt.setColor(DayStyle.getColorText(bHoliday, false));
		}

		if (!bIsActiveMonth)
			pt.setAlpha(0x8f);
		canvas.drawText(sDate, (int) (iTextPosX * 1.8),
				(iTextPosY + iMargin) / 2, pt);
		
		Lunar l = new Lunar(getDate().getTimeInMillis());
		canvas.drawText(l.getLunarDayString(), (int) (iTextPosX * 1),
				(iTextPosY), pt);
		canvas.drawText(l.getTermString(), (int) (iTextPosX * 1),
				(iTextPosY + iMargin) / 2+iTextPosY, pt);
		iconPositionX = CalendarActivity.getGridWidth(context)[0];
		iconPositionY = CalendarActivity.getGridWidth(context)[1];
		if (iconPositionX < iconPositionY) {// 竖屏
			// 遍历整个showEvent 找出要显示的四个事件类型
			int[] iconPosX = { iconPositionX / 2 + 2, 3, iconPositionX / 2 + 2,
					3 };
			int[] iconPosY = { iconPositionY / 5 * 2 + 3,
					iconPositionY / 3 * 2 + 3, iconPositionY / 3 * 2 + 3,
					iconPositionY / 5 * 2 + 3 };
			for (int i = 0, j = 0; i < MAXEVENTNUM && j < mShowEvent.length; j++) {
				if (mShowEvent[j] == 1 && eventStatus[j][dayPosition] == 1) {
						canvas.drawBitmap(((BitmapDrawable) this.getResources()
								.getDrawable(mEventImage[j])).getBitmap(),
								iconPosX[i], iconPosY[i], pt);
					
					i++;
				}
			}
		} else {// 横屏
			int[] iconPosX = { 3, 3, iconPositionX / 3,
					iconPositionX / 3 * 2 - 2 };
			int[] iconPosY = { 2, iconPositionY / 2, iconPositionY / 2,
					iconPositionY / 2 };
			for (int i = 0, j = 0; i < MAXEVENTNUM && j < mShowEvent.length; j++) {
				if (mShowEvent[j] == 1 && eventStatus[j][dayPosition] == 1) {
				
						canvas.drawBitmap(((BitmapDrawable) this.getResources()
								.getDrawable(mEventImage[j])).getBitmap(),
								iconPosX[i], iconPosY[i], pt);
					
					i++;
				}
			}
		}
		pt.setUnderlineText(false);
	}

	public boolean isViewFocused() {
		return (this.isFocused() || bTouchedDown);

	}

}
