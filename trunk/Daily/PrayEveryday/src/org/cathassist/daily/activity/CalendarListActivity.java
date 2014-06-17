package org.cathassist.daily.activity;

import java.util.Calendar;
import java.util.Map;

import org.cathassist.daily.PrayInEveryday;
import org.cathassist.daily.R;
import org.cathassist.daily.database.TodoDbAdapter;
import org.cathassist.daily.provider.CalendarManager;
import org.cathassist.daily.provider.DailyListAdapter;
import org.cathassist.daily.util.PublicFunction;

import android.R.anim;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class CalendarListActivity extends SherlockActivity {
	private PullToRefreshExpandableListView mListDiary;
	private ExpandableListView mListView;
	private DailyListAdapter dailyListAdapter;

	private Calendar mCalendar;
	private static final int TOP_REFRESH = 0;
	private static final int BOTTOM_REFRESH = 1;
	ActionBar actionBar;
	TodoDbAdapter dbHelper;
	PrayInEveryday prayEveryday;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_list);
		findView();
		initGlobalData();
		initViewData();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void findView() {
		actionBar = getSupportActionBar();
		mListDiary = (PullToRefreshExpandableListView) findViewById(R.id.list_calendar);
		mListView = mListDiary.getRefreshableView();
	}

	private void backToTime(Calendar calendar) {

		mCalendar = calendar;
		long time = calendar.getTimeInMillis();
		actionBar.setTitle(PublicFunction.formatDateYYYYMM(time));
		dbHelper.open();
		CalendarManager calendarManager = CalendarManager.getInstance();
		Map<String, Object> diaryListOfMonth = calendarManager
				.getCalendarListByMonth(time);
		dbHelper.close();
		dailyListAdapter = new DailyListAdapter(CalendarListActivity.this,
				diaryListOfMonth, prayEveryday, mCalendar);
		mListView.setAdapter(dailyListAdapter);
		mListView.expandGroup(calendarManager.getOpen());
	}

	private void initViewData() {
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		mListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// Intent intent = new Intent(getActivity(),
				// ShowDiaryActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putInt("diaryId", (int) id);
				// intent.putExtras(bundle);
				// startActivity(intent);
				return false;
			}
		});
		mListDiary
				.setOnRefreshListener(new OnRefreshListener2<ExpandableListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						Toast.makeText(CalendarListActivity.this, "Top",
								Toast.LENGTH_LONG).show();
						new GetDataTask(TOP_REFRESH).execute();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						Toast.makeText(CalendarListActivity.this, "Bottom",
								Toast.LENGTH_LONG).show();
						new GetDataTask(BOTTOM_REFRESH).execute();
					}
				});
		

	}

	private void initGlobalData() {
		dbHelper = new TodoDbAdapter(this);
		prayEveryday = (PrayInEveryday) getApplicationContext();
		backToTime(Calendar.getInstance());

	}

	private class GetDataTask extends
			AsyncTask<Void, Void, Map<String, Object>> {
		private int updateType;

		private GetDataTask(int updateType) {
			this.updateType = updateType;
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			switch (updateType) {
			case TOP_REFRESH:
				mCalendar.add(Calendar.MONTH, -1);
				break;
			case BOTTOM_REFRESH:
				mCalendar.add(Calendar.MONTH, 1);
				break;
			default:
				break;
			}
			dbHelper.open();
			// Map<String, Object> returnData = dbHelper
			// .getDiaryListBetweenTime(PublicFunction
			// .getMinTimeOfMonth(mCalendar.getTimeInMillis()),
			// PublicFunction.getMaxTimeOfMonth(mCalendar
			// .getTimeInMillis()));
			CalendarManager calendarManager = CalendarManager.getInstance();
			Map<String, Object> returnData = calendarManager
					.getCalendarListByMonth(mCalendar.getTimeInMillis());
			dbHelper.close();
			return returnData;
		}

		@Override
		protected void onPostExecute(Map<String, Object> diaryListOfMonth) {

			dailyListAdapter = new DailyListAdapter(CalendarListActivity.this,
					diaryListOfMonth, prayEveryday, mCalendar);

			dailyListAdapter.notifyDataSetChanged();
			mListView.setAdapter(dailyListAdapter);
			actionBar.setTitle(PublicFunction.formatDateYYYYMM(mCalendar
					.getTimeInMillis()));
			if (dailyListAdapter.getGroupCount() != 0) {
				mListView.expandGroup(CalendarManager.getInstance().getOpen());

			}
			// Call onRefreshComplete when the list has been refreshed.
			mListDiary.onRefreshComplete();

			super.onPostExecute(diaryListOfMonth);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.calendar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.calendar_select_time:
			new DatePickerDialog(CalendarListActivity.this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mCalendar.set(Calendar.YEAR, year);
					mCalendar.set(Calendar.MONTH, monthOfYear);
					mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					backToTime(mCalendar);
				}
			}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.calendar_back:
			backToTime(Calendar.getInstance());
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
