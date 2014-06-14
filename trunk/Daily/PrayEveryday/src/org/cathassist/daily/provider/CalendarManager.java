package org.cathassist.daily.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cathassist.daily.bean.CalendarDay;
import org.cathassist.daily.calendar.LiturgicDay;
import org.cathassist.daily.calendar.LiturgicYear;
import org.cathassist.daily.util.Lunar;
import org.cathassist.daily.util.PublicFunction;

public class CalendarManager {
	public static final long DAY_MI = 86400000;
	private static HashMap<Integer, LiturgicYear> mapLiturgicYear;
	private static CalendarManager calendarManager;

	public static CalendarManager getInstance() {
		if (calendarManager == null) {
			calendarManager = new CalendarManager();
		}
		return calendarManager;
	}

	public CalendarManager() {
		initCalendar();
	}

	public void initCalendar() {
		// 初始化礼仪年数据
		mapLiturgicYear = new HashMap<Integer, LiturgicYear>();
		LiturgicYear.initPropers();
	}

	public static LiturgicDay getLiturgicDay(int year, int month, int day) {
		return getLiturgicDay(new org.cathassist.daily.calendar.Date(year,
				month, day));
	}

	public static LiturgicDay getLiturgicDay(
			org.cathassist.daily.calendar.Date d) {
		int year = d.year();
		LiturgicYear pYear = mapLiturgicYear.get(year);
		if (pYear == null) {
			pYear = new LiturgicYear(year);
			mapLiturgicYear.put(year, pYear);
		}

		return pYear.getLiturgicDay(d);
	}

	public Map<String, Object> getCalendarListByMonth(long nowTime) {
		List<Integer> groupData = new ArrayList<Integer>();
		List<List<CalendarDay>> childData = new ArrayList<List<CalendarDay>>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		long time = PublicFunction.getMinTimeOfMonth(nowTime);
		Calendar c = Calendar.getInstance();
		int weekOfMonth = 0;
		List<CalendarDay> diaryList = new ArrayList<CalendarDay>();
		c.setTimeInMillis(time);
		do {
			if (weekOfMonth != c.get(Calendar.WEEK_OF_MONTH)) {
				weekOfMonth = c.get(Calendar.WEEK_OF_MONTH);
				groupData.add(weekOfMonth);
				if (diaryList.size() > 0)
					childData.add(diaryList);
				diaryList = new ArrayList<CalendarDay>();
			}
			CalendarDay calendarDay = new CalendarDay();
			Lunar lunar = new Lunar(c.getTimeInMillis());
			LiturgicDay ld = getLiturgicDay(c.get(Calendar.YEAR),
					c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
			calendarDay.setDate(PublicFunction.getYearMonthDayForSql(new Date(
					c.getTimeInMillis())));
			calendarDay.setDayType(0);
			calendarDay.setFestival(lunar.getLunarDateString());
			calendarDay.setHoliday(lunar.getSFestivalName());
			calendarDay.setMemorableDay(0);
			calendarDay.setSolarTerms(lunar.getTermString());
			ld.getCalendarDay(calendarDay);
			diaryList.add(calendarDay);
			c.add(Calendar.DAY_OF_MONTH, 1);
		} while (c.get(Calendar.DAY_OF_MONTH) != 1);
		if (diaryList.size() > 0)
			childData.add(diaryList);
		// childData.add(diaryList);
		returnMap.put("groupData", groupData);
		returnMap.put("childData", childData);
		return returnMap;
	}
}
