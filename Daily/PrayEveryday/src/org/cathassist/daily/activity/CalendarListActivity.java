package org.cathassist.daily.activity;

import java.util.Calendar;
import java.util.Map;

import org.cathassist.daily.PrayInEveryday;
import org.cathassist.daily.R;
import org.cathassist.daily.database.TodoDbAdapter;
import org.cathassist.daily.provider.DailyListAdapter;
import org.cathassist.daily.util.PublicFunction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class CalendarListActivity extends SherlockActivity{
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
		mListDiary = (PullToRefreshExpandableListView)findViewById(R.id.list_calendar);
		mListView = mListDiary.getRefreshableView();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getSupportMenuInflater().inflate(R.menu.activity_diarylist, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.add_diary:
	// Intent intent = new Intent(this, EditDiaryActivity.class);
	// startActivity(intent);
	// break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	private void initViewData() {
		actionBar.setTitle(PublicFunction
				.formatDateYYYYMM(mCalendar.getTimeInMillis()));
		mListView.setAdapter(dailyListAdapter);
		
		mListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
//				Intent intent = new Intent(getActivity(),
//						ShowDiaryActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("diaryId", (int) id);
//				intent.putExtras(bundle);
//				startActivity(intent);
				return false;
			}
		});
		mListDiary.setOnRefreshListener(new OnRefreshListener2<ExpandableListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						Toast.makeText(CalendarListActivity.this, "Top", Toast.LENGTH_LONG)
								.show();
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
		mListView.expandGroup(0);
	}

	private void initGlobalData() {
		dbHelper = new TodoDbAdapter(this);
		prayEveryday = (PrayInEveryday) getApplicationContext();
		mCalendar = Calendar.getInstance();
		dbHelper.open();
		Map<String, Object> diaryListOfMonth = dbHelper
				.getDiaryListBetweenTime(PublicFunction
						.getMinTimeOfMonth(mCalendar.getTimeInMillis()),
						PublicFunction.getMaxTimeOfMonth(mCalendar
								.getTimeInMillis()));
		dbHelper.close();
		// for (Diary diary:diaryListOfMonth) {
		// Calendar diaryCalendar;
		// diaryCalendar.setTimeInMillis(diary.getDate());
		// int weekOfMonth=diaryCalendar.get(Calendar.WEEK_OF_MONTH);
		// do {
		// Map<String, Object> groupMap1 = new HashMap<String, Object>();
		// groupData.add(groupMap1);
		// groupMap1.put("week_of_month", weekOfMonth);
		// } while (weekOfMonth-->0);
		//
		//
		// List<Map<String, String>> childList = new ArrayList<Map<String,
		// String>>();
		// for (String string : mChildStrings) {
		// Map<String, String> childMap = new HashMap<String, String>();
		// childList.add(childMap);
		// childMap.put("diary", string);
		// }
		// childData.add(childList);
		//
		// }
		dailyListAdapter = new DailyListAdapter(CalendarListActivity.this,
				diaryListOfMonth,prayEveryday,mCalendar);

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
			Map<String, Object> returnData = dbHelper
					.getDiaryListBetweenTime(PublicFunction
							.getMinTimeOfMonth(mCalendar.getTimeInMillis()),
							PublicFunction.getMaxTimeOfMonth(mCalendar
									.getTimeInMillis()));
			dbHelper.close();
			return returnData;
		}

		@Override
		protected void onPostExecute(Map<String, Object> diaryListOfMonth) {

			dailyListAdapter = new DailyListAdapter(CalendarListActivity.this,
					diaryListOfMonth, prayEveryday , mCalendar);

			dailyListAdapter.notifyDataSetChanged();
			mListView.setAdapter(dailyListAdapter);
			actionBar.setTitle(PublicFunction
					.formatDateYYYYMM(mCalendar.getTimeInMillis()));
			if (dailyListAdapter.getGroupCount() != 0) {
				mListView.expandGroup(0);
			}
			// Call onRefreshComplete when the list has been refreshed.
			mListDiary.onRefreshComplete();

			super.onPostExecute(diaryListOfMonth);
		}
	}
}
