package org.cathassist.daily.calendar;

import java.util.Calendar;

import android.R;
import android.content.Context;

/**
 * @author：zp
 * @version 创建时间：2011-3-20下午04:36:44
 * @asfaf :定义主要用到的颜色
 * 
 */

public class DayStyle {

	// 星期几标题中（周一到周五）的背景颜色
	public final static int iColorFrameHeader = 0xff666666;
	// 周六周天背景色
	public final static int iColorFrameHeaderHoliday = 0xff707070;
	// 第一行（周一到周五）的文字颜色
	public final static int iColorTextHeader = 0xffcccccc;
	// 周六周天文字颜色
	public final static int iColorTextHeaderHoliday = 0xffd0d0d0;

	// 日历中周一到周五字体颜色
	public final static int iColorText = 0xffdddddd;
	// 周六周天字体颜色
	public final static int iColorTextHoliday = 0xfff0f0f0;
	// 日历中周一到周五的背景色
	public final static int iColorBkg = 0xff888888;
	// 周六周天背景颜色
	public final static int iColorBkgHoliday = 0xffaaaaaa;

	// 今天的字体颜色以及背景色（当天的背景色可以是由两种颜色混合而成）
	public final static int iColorTextToday = 0xff002200;
	public final static int iColorBkgToday = 0xffbbddff;

	// 取得当前焦点的日历中的一天的背景色以及字体颜色
	public final static int iColorTextFocus = 0xff002200;
	public final static int iColorBkgFocus = 0xffbbddff;

	// 日历每一页中非当前月的天加阴影
	public final static int iAlphaInactiveMonth = 0x88;

	// 设置选中时的背景色
	public final static int iColorClicked = 0xffbbddff;

	// //选中时的字体颜色，背景色
	// public final static int iColorTextSelected = 0xff001122;
	// public final static int iColorBkgSelectedLight = 0xffbbddff;
	// public final static int iColorBkgSelectedDark = 0xff225599;
	//
	// //日历每一个获得焦点时的字体颜色以及背景色
	// public final static int iColorTextFocused = 0xff221100;
	// public final static int iColorBkgFocusLight = 0xffffddbb;
	// public final static int iColorBkgFocusDark = 0xffaa5500;

	// methods
	public static int getColorFrameHeader(boolean bHoliday) {
		if (bHoliday)
			return iColorFrameHeaderHoliday;
		return iColorFrameHeader;
	}

	public static int getColorTextHeader(boolean bHoliday) {
		if (bHoliday)
			return iColorTextHeaderHoliday;
		return iColorTextHeader;
	}

	public static int getColorText(boolean bHoliday, boolean bToday) {
		// if (bToday)
		// return iColorText;
		if (bHoliday)
			return iColorTextHoliday;
		return iColorText;
	}

	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		// if (bToday)
		// return iColorBkg;
		if (bHoliday)
			return iColorBkgHoliday;
		return iColorBkg;
	}

	public static int getWeekDay(int index, int iFirstDayOfWeek) {
		int iWeekDay = -1;

		if (iFirstDayOfWeek == Calendar.MONDAY) {
			iWeekDay = index + Calendar.MONDAY;
			if (iWeekDay > Calendar.SATURDAY)
				iWeekDay = Calendar.SUNDAY;
		}

		if (iFirstDayOfWeek == Calendar.SUNDAY) {
			iWeekDay = index + Calendar.SUNDAY;
		}
		 if(iFirstDayOfWeek == Calendar.SATURDAY){
        	 iWeekDay = index +Calendar.SATURDAY;
        	 if (iWeekDay > Calendar.SATURDAY)
 				iWeekDay = index + Calendar.SATURDAY - 7;
         }
		return iWeekDay;
	}

}
