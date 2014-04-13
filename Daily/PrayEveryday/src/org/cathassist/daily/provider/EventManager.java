package org.cathassist.daily.provider;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;

public class EventManager {
	private Context context;

	public EventManager(Context context) {
		this.context = context;
	}

	public int[][] getEventByDTime(int babyID, Calendar startCalendar,
			Calendar endCalendar) {
		int[][] eventStatus = new int[17][45];
		
		return eventStatus;
	}

}
