package daily.cathassist.org.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import daily.cathassist.org.R;
import daily.cathassist.org.bean.DateBean;
import daily.cathassist.org.database.TodoDbAdapter;
import daily.cathassist.org.util.PublicFunction;

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
		SampleAdapter adapter = new SampleAdapter(getActivity());
		dbHelper.open();
		List<DateBean> dateList = dbHelper.getDateList();
		dbHelper.close();
		int position = 0;
		for (int i = 0; i < dateList.size(); i++) {
			DateBean dateBean = dateList.get(i);
			SampleItem sampleItem = new SampleItem(dateBean.getDate(),
					dateBean.getId());
			adapter.add(sampleItem);
			if (PublicFunction.getYearMonthDayForSql(calendar.getTime())
					.equals(dateBean.getDate())) {
				position = adapter.getPosition(sampleItem);
			}

			dateMap.put(dateBean.getId(), dateBean.getDate());
		}
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