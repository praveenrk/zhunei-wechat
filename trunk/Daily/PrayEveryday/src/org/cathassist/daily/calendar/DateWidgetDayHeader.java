package org.cathassist.daily.calendar;

import java.util.Calendar;

import org.cathassist.daily.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author：zp
 * @version 创建时间：2011-3-20下午04:36:14
 * @asfaf :画日历的第一列（周一到周天）
 * 
 */

public class DateWidgetDayHeader extends View {
	// fields
	private int iDayHeaderFontSize = 12;

	// fields
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private int iWeekDay = -1;
	private boolean bHoliday = false;
	Context context;

	// methods
	public DateWidgetDayHeader(Context context, int iWidth, int iHeight) {
		super(context);
		this.context = context;
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		if (iWidth < iHeight)
			iDayHeaderFontSize = iWidth / 2;
		else
			iDayHeaderFontSize = iHeight / 2;
	}

	public void setData(int iWeekDay) {
		this.iWeekDay = iWeekDay;
		this.bHoliday = false;
		if ((iWeekDay == Calendar.SATURDAY) || (iWeekDay == Calendar.SUNDAY))
			this.bHoliday = true;
	}

	private void drawDayHeader(Canvas canvas) {
		if (iWeekDay != -1) {
			// background
			pt.setColor(DayStyle.getColorFrameHeader(bHoliday));
			canvas.drawRect(rect, pt);

			// text
			pt.setTypeface(null);
			pt.setTextSize(iDayHeaderFontSize);
			pt.setAntiAlias(true);
			pt.setFakeBoldText(true);
			pt.setColor(DayStyle.getColorTextHeader(bHoliday));
			// Context context;
			final int iTextPosY = getTextHeight();
			String weekDayName[] = getWeekDayNames();
			final String sDayName = weekDayName[iWeekDay];

			// draw day name
			final int iDayNamePosX = (int) rect.left
					+ ((int) rect.width() >> 1)
					- ((int) pt.measureText(sDayName) >> 1);
			canvas.drawText(sDayName, iDayNamePosX, rect.top + iTextPosY + 2,
					pt);
		}
	}

	/* 用于显示日历每一天对应星期几的数组，位于日历第一行 */
	private String[] getWeekDayNames() {
		String[] vec = new String[10];
		vec[Calendar.SUNDAY] = context.getString(R.string.sun);
		vec[Calendar.MONDAY] = context.getString(R.string.mon);
		vec[Calendar.TUESDAY] = context.getString(R.string.tue);
		vec[Calendar.WEDNESDAY] = context.getString(R.string.wed);
		vec[Calendar.THURSDAY] = context.getString(R.string.thu);
		vec[Calendar.FRIDAY] = context.getString(R.string.fri);
		vec[Calendar.SATURDAY] = context.getString(R.string.sat);
		return vec;
	}

	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// init rectangles
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// drawing
		drawDayHeader(canvas);
	}

}
