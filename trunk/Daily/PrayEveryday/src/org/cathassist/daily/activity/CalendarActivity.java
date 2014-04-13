package org.cathassist.daily.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.cathassist.daily.R;
import org.cathassist.daily.calendar.DateWidgetDayCell;
import org.cathassist.daily.calendar.DateWidgetDayHeader;
import org.cathassist.daily.calendar.DayStyle;
import org.cathassist.daily.calendar.MyViewFlipper;
import org.cathassist.daily.provider.EventManager;
import org.cathassist.daily.util.AnimationHelper;
import org.cathassist.daily.util.PublicFunction;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.umeng.analytics.MobclickAgent;

/**
 * @类描述:日历功能主类，整体日历布局
 * @version 创建时间：2011-3-20上午11:24:42
 * @创建人: zp
 * @version 修改时间： 2011-3-20上午11:24:42
 */

public class CalendarActivity extends Activity {
	/* viewflipper的两个页面的每一格的集合 */
	private ArrayList<DateWidgetDayCell> mNextDays = new ArrayList<DateWidgetDayCell>();
	private ArrayList<DateWidgetDayCell> mLastDays = new ArrayList<DateWidgetDayCell>();
	private Calendar mCalStartDate = Calendar.getInstance();
	private Calendar mCalToday = Calendar.getInstance();
	private Calendar mCalCalendar = Calendar.getInstance();
	private Calendar mCalSelected = Calendar.getInstance();
	private Calendar mCalStartTime = Calendar.getInstance();
	private LinearLayout mLayNextMonth = null;
	private LinearLayout mLayLastMonth = null;
	private LinearLayout mCalendarHeader = null;
	private RelativeLayout mRelativeLayout = null;
	private int mFirstDayOfWeek = Calendar.SUNDAY;
	private int mMonthViewCurrentMonth = 0;
	private int mMonthViewCurrentYear = 0;
	public static final int SELECT_DATE_REQUEST = 111;
	private int mDayCellSize = 46;
	private int mDayCellHeight;
	private int mDayHeaderHeight = 24;
	private int[] mShowEvent = new int[17];
	private static final int TITLEBARSIZE = 40;

	private MyViewFlipper mViewFlipper;
	// 手势启示点击的x坐标
	private int mOldTouchValue = 0;
	private boolean mCurrentView;
	// 保存日历每一页对应的每一天的各种活动情况
	private int[][] mEventStatus;
	// 设置日历的titlebar：内容格式为：yyyy/mm
	TextView mTxtTitle;
	ImageButton mImageBtnMenu;
	SharedPreferences mSharedPreferences;
	private final int MAXEVENTNUM = 4;
	private final String EVENTINDER = "showEventIndexInCalendar";
	private final String FIRSTEVENTINDER = "showEventIndexInCalendarfirst";
	private final int[] mDefaultEvent = { 1, 2, 3, 4 };

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		initializeValues();
	}

	public void initializeValues() {
		mSharedPreferences = getSharedPreferences("", 0);
		int isFirst = mSharedPreferences.getInt(FIRSTEVENTINDER, -1);
		// 第一次使用 默认四个选择
		if (isFirst == -1) {
			Editor editor = mSharedPreferences.edit();
			for (int i = 0; i < MAXEVENTNUM; i++) {
				editor.putInt(EVENTINDER + i, mDefaultEvent[i]);
			}
			editor.putInt(FIRSTEVENTINDER, 1);
			editor.commit();
		}

		for (int i = 0; i < MAXEVENTNUM; i++) {
			int showEventIndex = mSharedPreferences.getInt(EVENTINDER + i, -1);
			if (showEventIndex != -1) {
				mShowEvent[showEventIndex] = 1;
			}
		}
		mDayCellSize = getGridWidth(CalendarActivity.this)[0];
		mDayCellHeight = getGridWidth(CalendarActivity.this)[1];
		mDayHeaderHeight = getGridWidth(CalendarActivity.this)[2];
		// mFirstDayOfWeek = Calendar.MONDAY;
		initFirstDayOfWeek();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(generateContentView());
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.header);
		setHeaderBackground();

		mTxtTitle = (TextView) findViewById(R.id.titlebar_text);
		mImageBtnMenu = (ImageButton) findViewById(R.id.btn_titlebar_menu);
		mImageBtnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent();
				// intent.setClass(CalendarActivity.this,
				// SelectEventInCalendar.class);
				// startActivity(intent);
			}
		});
		mCalStartDate = getCalendarStartDate();
	}

	private void setHeaderBackground() {
		// mBabySkin = mSharedPreferences.getInt(mBabyID+"", 0);
		// switch (mBabySkin) {
		// case 0:
		// mRelativeLayout
		// .setBackgroundResource(R.drawable.ab_bottom_solid_actionbarstyle);
		// break;
		// case 1:
		// mRelativeLayout
		// .setBackgroundResource(R.drawable.ab_bottom_solid_pink);
		// break;
		// case 2:
		// mRelativeLayout
		// .setBackgroundResource(R.drawable.ab_bottom_solid_green);
		// break;
		// case 3:
		// mRelativeLayout
		// .setBackgroundResource(R.drawable.ab_transparent_black);
		// break;
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.setting);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Intent intent = new Intent();
		// intent.setClass(this, SelectEventInCalendar.class);
		// startActivity(intent);
		PublicFunction.showToast(CalendarActivity.this, "Test");
		return true;
	}

	private void initFirstDayOfWeek() {
		Calendar c = Calendar.getInstance();
		int i = Calendar.getInstance(TimeZone.getDefault()).getFirstDayOfWeek();
		switch (i) {
		case 1:
			mFirstDayOfWeek = Calendar.SUNDAY;
			break;
		case 2:
			mFirstDayOfWeek = Calendar.MONDAY;
			break;
		case 3:
			mFirstDayOfWeek = Calendar.TUESDAY;
			break;
		case 4:
			mFirstDayOfWeek = Calendar.WEDNESDAY;
			break;
		case 5:
			mFirstDayOfWeek = Calendar.THURSDAY;
			break;
		case 6:
			mFirstDayOfWeek = Calendar.FRIDAY;
			break;
		case 7:
			mFirstDayOfWeek = Calendar.SATURDAY;
			break;
		}

	}

	@Override
	public void onResume() {
		for (int i = 0; i < mShowEvent.length; i++) {
			mShowEvent[i] = 0;
		}
		for (int i = 0; i < MAXEVENTNUM; i++) {
			int showEventIndex = mSharedPreferences.getInt(EVENTINDER + i, -1);
			if (showEventIndex != -1) {
				mShowEvent[showEventIndex] = 1;
			}
		}
		long startCreateTime = System.currentTimeMillis();
		super.onResume();
		MobclickAgent.onResume(this);
		DateWidgetDayCell daySelected = updateCalendar();
		if (daySelected != null)
			daySelected.requestFocus();
		long endCreateTime = System.currentTimeMillis();
		Log.v("DateWidgetcreateTime:", (endCreateTime - startCreateTime) + "");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(CalendarActivity.this,
					MainActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 创建layout
	 * 
	 * @param iOrientation
	 * @return
	 */
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		return lay;
	}

	/**
	 * 横竖屏切换时的处理
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDayCellSize = getGridWidth(CalendarActivity.this)[0];
		mDayCellHeight = getGridWidth(CalendarActivity.this)[1];
		mDayHeaderHeight = getGridWidth(CalendarActivity.this)[2];
		mLayLastMonth.removeAllViews();
		generateCalendars(mLayLastMonth);
		mLayNextMonth.removeAllViews();
		generateCalendar(mLayNextMonth);
		mCalendarHeader.removeAllViews();
		mCalendarHeader.addView(generateCalendarHeader());
		// calendarHeader=generateCalendarHeader();
		mCalendarHeader.invalidate();

		this.onResume();
	}

	/**
	 * 
	 * 设置日历的整体布局
	 * 
	 * @return
	 */
	private View generateContentView() {
		LinearLayout layMain = createLayout(LinearLayout.VERTICAL);
		layMain.setPadding(0, 0, 0, 0);
		mLayLastMonth = createLayout(LinearLayout.VERTICAL);
		generateCalendars(mLayLastMonth);

		mLayNextMonth = createLayout(LinearLayout.VERTICAL);
		generateCalendar(mLayNextMonth);

		mViewFlipper = new MyViewFlipper(CalendarActivity.this);
		mViewFlipper.findViewById(R.id.view_flipper);
		mViewFlipper.removeAllViews();
		mViewFlipper.addView(mLayNextMonth);
		mViewFlipper.addView(mLayLastMonth);

		mCalendarHeader = generateCalendarHeader();
		mViewFlipper.setPadding(0, 0, 0, 0);
		mCalendarHeader.setPadding(0, 0, 0, 0);
		layMain.addView(mCalendarHeader);
		layMain.addView(mViewFlipper);
		return layMain;
	}

	/**
	 * 设置日历每的每一列
	 * 
	 * @return
	 */
	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this,
					mDayCellSize, (mDayCellHeight), mShowEvent);
			dayCell.setOnClickListener(mOnDayCellClick);
			dayCell.setOnTouchListener(mOnDayTouchListener);
			mNextDays.add(dayCell);
			layRow.addView(dayCell);
		}
		return layRow;
	}

	/**
	 * 设置日历开始第一列的周期显示（周一到周日）
	 * 
	 * @return
	 */
	private LinearLayout generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		// iDayHeaderHeigh=iDayCellHeight
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this,
					mDayCellSize, mDayHeaderHeight);
			final int iWeekDay = DayStyle.getWeekDay(iDay, mFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}
		return layRow;
	}

	/**
	 * 日历的每一行
	 * 
	 * @param layContent
	 */
	private void generateCalendar(LinearLayout layContent) {
		// layContent.addView(generateCalendarHeader());
		mNextDays.clear();
		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRow());
		}
	}

	private void generateCalendars(LinearLayout layContent) {
		// layContent.addView(generateCalendarHeader());
		mLastDays.clear();
		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRows());
		}
	}

	private View generateCalendarRows() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this,
					mDayCellSize, (mDayCellHeight), mShowEvent);
			// dayCell.setItemClick(mOnDayCellClick);
			dayCell.setOnClickListener(mOnDayCellClick);
			dayCell.setOnTouchListener(mOnDayTouchListener);
			mLastDays.add(dayCell);
			layRow.addView(dayCell);
		}
		return layRow;
	}

	/**
	 * 取得日历每一页的开始时间
	 * 
	 * @return
	 */
	private Calendar getCalendarStartDate() {
		mCalToday.setTimeInMillis(System.currentTimeMillis());
		mCalToday.setFirstDayOfWeek(mFirstDayOfWeek);

		if (mCalSelected.getTimeInMillis() == 0) {
			mCalStartDate.setTimeInMillis(System.currentTimeMillis());
			mCalStartDate.setFirstDayOfWeek(mFirstDayOfWeek);
		} else {
			mCalStartDate.setTimeInMillis(mCalSelected.getTimeInMillis());
			mCalStartDate.setFirstDayOfWeek(mFirstDayOfWeek);
		}

		UpdateStartDateForMonth();

		return mCalStartDate;
	}

	/**
	 * 设置日历的每一格
	 * 
	 * @return
	 */
	private DateWidgetDayCell updateCalendar() {
		DateWidgetDayCell daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (mCalSelected.getTimeInMillis() != 0);
		final int iSelectedYear = mCalSelected.get(Calendar.YEAR);
		final int iSelectedMonth = mCalSelected.get(Calendar.MONTH);
		final int iSelectedDay = mCalSelected.get(Calendar.DAY_OF_MONTH);

		mCalCalendar.setTimeInMillis(mCalStartDate.getTimeInMillis());
		for (int i = 0; i < 42; i++) {
			final int iYear = mCalCalendar.get(Calendar.YEAR);
			final int iMonth = mCalCalendar.get(Calendar.MONTH);
			final int iDay = mCalCalendar.get(Calendar.DAY_OF_MONTH);

			if (i == 0) {
				mCalStartTime.set(mCalCalendar.get(Calendar.YEAR),
						mCalCalendar.get(Calendar.MONTH),
						mCalCalendar.get(Calendar.DAY_OF_MONTH), 0, 0);
				Calendar endCalendar = Calendar.getInstance();
				double time = 3628800000f;// 42天的时间
				endCalendar.setTimeInMillis((long) (mCalStartTime
						.getTimeInMillis() + time - 60001));
				EventManager eventMananger = new EventManager(
						CalendarActivity.this);
				mEventStatus = eventMananger.getEventByDTime(0, mCalStartTime,
						endCalendar);
			}
			final int iDayOfWeek = mCalCalendar.get(Calendar.DAY_OF_WEEK);
			DateWidgetDayCell dayCell;
			if (!mCurrentView)
				dayCell = mNextDays.get(i);
			else
				dayCell = mLastDays.get(i);
			// check today
			boolean bToday = false;
			if (mCalToday.get(Calendar.YEAR) == iYear)
				if (mCalToday.get(Calendar.MONTH) == iMonth)
					if (mCalToday.get(Calendar.DAY_OF_MONTH) == iDay)
						bToday = false;
			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;
			dayCell.setEventStatus(mEventStatus);
			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday,
					mMonthViewCurrentMonth, i);
			bSelected = false;
			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}
			dayCell.setSelected(bSelected);
			if (bSelected)
				daySelected = dayCell;
			mCalCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (!mCurrentView)
			mLayNextMonth.invalidate();
		else
			mLayLastMonth.invalidate();
		return daySelected;
	}

	/**
	 * 取得每一页的第一天
	 */
	private void UpdateStartDateForMonth() {
		mMonthViewCurrentMonth = mCalStartDate.get(Calendar.MONTH);
		mMonthViewCurrentYear = mCalStartDate.get(Calendar.YEAR);
		mCalStartDate.set(Calendar.DAY_OF_MONTH, 1);
		UpdateCurrentMonthDisplay();
		// update days for week
		int iDay = 0;
		int iStartDay = mFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = mCalStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = mCalStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SATURDAY) {
			iDay = mCalStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SATURDAY;
			if (iDay < 0) {
				iDay = mCalStartDate.get(Calendar.DAY_OF_WEEK);
			}
		}
		mCalStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	/**
	 * 更新titlebar显示
	 */
	private void UpdateCurrentMonthDisplay() {
		mCalCalendar.setTimeInMillis(mCalStartDate.getTimeInMillis());
		String s = mCalCalendar.get(Calendar.YEAR) + "/"
				+ (mCalCalendar.get(Calendar.MONTH) + 1);// dateMonth.format(calCalendar.getTime());
		mTxtTitle.setText(s);
	}

	/**
	 * 点击上一月的按钮时显示上个月对应的日历
	 */
	private void setPrevViewItem() {
		mCurrentView = !mCurrentView;
		mMonthViewCurrentMonth--;
		if (mMonthViewCurrentMonth == -1) {
			mMonthViewCurrentMonth = 11;
			mMonthViewCurrentYear--;
		}
		mCalStartDate.set(Calendar.DAY_OF_MONTH, 1);
		mCalStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth);
		mCalStartDate.set(Calendar.YEAR, mMonthViewCurrentYear);

		UpdateStartDateForMonth();
		updateCalendar();
		//
		// viewFlipper.removeAllViews();
		// if (!currentView)
		// viewFlipper.addView(layNextMonth);
		// else
		// viewFlipper.addView(layLastMonth);
		mViewFlipper.setInAnimation(AnimationHelper.inFromLeftAnimation());
		mViewFlipper.setOutAnimation(AnimationHelper.outToRightAnimation());
		mViewFlipper.showPrevious();
	}

	/**
	 * 点击下一页时显示日历的下一个月
	 */
	private void setNextViewItem() {
		mCurrentView = !mCurrentView;
		mMonthViewCurrentMonth++;
		if (mMonthViewCurrentMonth == 12) {
			mMonthViewCurrentMonth = 0;
			mMonthViewCurrentYear++;
		}
		mCalStartDate.set(Calendar.DAY_OF_MONTH, 1);
		mCalStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth);
		mCalStartDate.set(Calendar.YEAR, mMonthViewCurrentYear);

		UpdateStartDateForMonth();
		updateCalendar();

		mViewFlipper.setInAnimation(AnimationHelper.inFromRightAnimation());
		mViewFlipper.setOutAnimation(AnimationHelper.outToLeftAnimation());
		mViewFlipper.showNext();
	}

	/**
	 * 点击日历的每一格对应的事件
	 */
	private DateWidgetDayCell.OnClickListener mOnDayCellClick = new DateWidgetDayCell.OnClickListener() {
		@Override
		public void onClick(View v) {
			// // 设置选中时的背景色
			for (DateWidgetDayCell cell:mNextDays) {
				cell.setBackgroundColor(DayStyle.iAlphaInactiveMonth);
			}
			v.setBackgroundColor(DayStyle.iColorClicked);
			DateWidgetDayCell item = (DateWidgetDayCell) v;

			int day = 0;
			if (!mCurrentView)
				for (int i = 0; i < mNextDays.size(); i++) {
					if (mNextDays.get(i) == item) {
						day = i;
						item = mNextDays.get(day);
					}
				}
			else
				for (int i = 0; i < mLastDays.size(); i++) {
					if (mLastDays.get(i) == item) {
						day = i;
						item = mLastDays.get(day);
					}
				}
			// int size = ThemeSettings.activityList.size();
			// for (int i = 0; i < size; i++) {
			// if (ThemeSettings.activityList.get(i) != null) {
			// Activity activity = (Activity) ThemeSettings.activityList
			// .get(i);
			// activity.finish();
			// }
			// }
			// ThemeSettings.activityList.clear();
			//
			// Intent i = new Intent(CalendarActivity.this,
			// BabyCareMainActivity.class);
			// i.putExtra("SelectedTime", item.getDate().getTimeInMillis());
			// i.putExtra("setviewpageposition", true);
			// i.putExtra("CalenderToHistory", true);
			// i.putExtra(BabyCarePlus.FRAGMENT_NAME,
			// BabyCarePlus.HISTORY_FRAGMENT);
			// startActivity(i);
			// finish();
		}
	};

	/**
	 * 监听日历的滑动事件
	 */
	private DateWidgetDayCell.OnTouchListener mOnDayTouchListener = new DateWidgetDayCell.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mOldTouchValue = (int) event.getX();
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP: // if(this.searchOk==false) return
				// false;
				int currentX = (int) event.getX();
				if (mOldTouchValue - currentX > getGridWidth(CalendarActivity.this)[0] * 2) {
					setNextViewItem();
				} else if (currentX - mOldTouchValue > getGridWidth(CalendarActivity.this)[0] * 2) {
					setPrevViewItem();
				}
				break;
			}
			return false;
		}
	};

	/**
	 * 根据屏幕自适应设置每一格的宽度与高度
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getGridWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int titleHeight = 20;
		int dayHeaderHeight = 24;
		if (dm.widthPixels < 300 || dm.heightPixels < 300) {
			titleHeight += 38;
			dayHeaderHeight = 18;
		} else if (dm.widthPixels > 400 && dm.heightPixels > 400) {
			titleHeight += 70;
		} else {
			titleHeight += 50;
			dayHeaderHeight = 30;
		}
		return new int[] {
				(dm.widthPixels / 7) + 1,
				((dm.heightPixels - dip2px(context, dayHeaderHeight) - dip2px(
						context, TITLEBARSIZE)) / 6),
				dip2px(context, dayHeaderHeight) };
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
