package org.cathassist.daily.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cathassist.daily.R;
import org.cathassist.daily.bean.DateBean;
import org.cathassist.daily.database.TodoDbAdapter;
import org.cathassist.daily.util.PublicFunction;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DateListFragment extends ListFragment {
	private TodoDbAdapter dbHelper;
	private Map<Long, String> dateMap = new HashMap<Long, String>();
	private Calendar calendar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		calendar = Calendar.getInstance();
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dbHelper = new TodoDbAdapter(getActivity());
		dbHelper.open();
		List<DateBean> list = dbHelper.getDateList();
		dbHelper.close();
		SampleAdapter adapter = new SampleAdapter(getActivity());
		int position = 0;
		for (DateBean dateBean:list) {
			SampleItem sampleItem = new SampleItem(dateBean.getDate(),
					dateBean.getId());
			adapter.add(sampleItem);
			if (PublicFunction.getYearMonthDayForSql(calendar.getTime())
					.equals(dateBean.getDate())) {
				position = adapter.getPosition(sampleItem);
			}
			dateMap.put(dateBean.getId(), dateBean.getDate());
		}
//		for (int i = 0; i < list.size() ; i++) {
//			Calendar calendar=Calendar.getInstance();
//			calendar.add(Calendar.DAY_OF_YEAR, i);
//			DateBean dateBean = new DateBean(i, PublicFunction.getYearMonthDayForSql(new Date(calendar.getTimeInMillis())));
//			SampleItem sampleItem = new SampleItem(dateBean.getDate(),
//					dateBean.getId());
//			adapter.add(sampleItem);
//			if (PublicFunction.getYearMonthDayForSql(calendar.getTime())
//					.equals(dateBean.getDate())) {
//				position = adapter.getPosition(sampleItem);
//			}
//			dateMap.put(dateBean.getId(), dateBean.getDate());
//		}
		setListAdapter(adapter);
		setSelection(position);
	}

	private class SampleItem {
		public String tag;
		public long id;

		public SampleItem(String tag, long id) {
			this.tag = tag;
			this.id = id;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.row, null);
			}
			TextView title = (TextView) convertView
					.findViewById(R.id.row_title);

			if (PublicFunction.getYearMonthDayForSql(calendar.getTime())
					.equals(getItem(position).tag)) {
				title.setText(getItem(position).tag + "("
						+ getString(R.string.today) + ")");
			} else {
				title.setText(getItem(position).tag);
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return getItem(position).id;
		}

	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		switchFragment(dateMap.get(id));
	}

	// the meat of switching the above fragment
	private void switchFragment(String date) {
		if (getActivity() == null)
			return;
		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.switchContent(date);
	}
}
