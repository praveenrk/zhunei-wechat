package org.cathassist.daily.activity;

import org.cathassist.daily.R;
import org.cathassist.daily.bean.CalendarDay;
import org.cathassist.daily.database.TodoDbAdapter;
import org.cathassist.daily.util.PublicFunction;

import com.spreada.utils.chinese.ZHConverter;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public final class MainFragment extends Fragment {
	private TextView txtDayTitle, txtDate, txtSolarTerms, txtHoliday,
			txtFestival, txtDayNature, txtBibleSentence, txtBible, txtPray;
	private TodoDbAdapter dbHelper;
	private CalendarDay calendarDay;
	private String dateString;

	private Button button;

	public static MainFragment newInstance(String dateString) {
		MainFragment fragment = new MainFragment();
		fragment.dateString = dateString;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// if ((savedInstanceState != null)
		// && savedInstanceState.containsKey(KEY_CONTENT)) {
		// mContent = savedInstanceState.getString(KEY_CONTENT);
		// }
		View view = inflater.inflate(R.layout.main_view_page_layout, container,
				false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// getActivity().findViewById(R.id.contacts_button_deleteBanned).setOnClickListener(this);
		// getActivity().findViewById(R.id.contacts_button_syncContacts).setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findViews(view);
		setListener();

		dbHelper = new TodoDbAdapter(getActivity());
		dbHelper.open();
		if (savedInstanceState != null
				&& savedInstanceState.getString("dateString") != null) {
			if (dateString == null) {
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);

			}
			dateString = savedInstanceState.getString("dateString");
		}
		txtDate.setText(dateString);
		Log.e("dateString", dateString);
		calendarDay = dbHelper.getCalendarDayByDate(dateString);
		dbHelper.close();
		txtDayTitle.setText(dateString);
		if (calendarDay != null) {
			txtDayTitle.setText(calendarDay.getSummary());
			txtSolarTerms.setText(calendarDay.getSolarTerms());
			txtHoliday.setText(calendarDay.getHoliday());
			txtFestival.setText(calendarDay.getFestival());
			txtDayNature.setText(PublicFunction.getDayNatureString(
					getActivity(), calendarDay.getMemorableDay()));
			txtBible.setText(calendarDay.getBible());
			txtBible.setTextColor(Color.DKGRAY);
			ZHConverter converter = ZHConverter
					.getInstance(ZHConverter.SIMPLIFIED);
			txtPray.setText(converter.convert(calendarDay.getPray()));
			txtPray.setTextColor(Color.DKGRAY);
			txtBibleSentence.setText(R.string.bible_sentence);
			// txtBible.setText();
			// txtPray.setText();
			setTextColor(calendarDay.getDayType(),
					calendarDay.getHoliday() != null
							&& !calendarDay.getHoliday().equals(""));
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (PublicFunction.isAvilible(getActivity(),
							"org.cathassist.bible")) {
						Intent intent = new Intent();
						intent.setClassName("org.cathassist.bible",
								"org.cathassist.bible.MainActivity");
						startActivity(intent);
					} else {
						Uri uri = Uri
								.parse("market://details?id=org.cathassist.bible");
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				}
			});
		}
	}

	private void findViews(View view) {
		txtDayTitle = (TextView) view.findViewById(R.id.txt_daytitle);
		txtDate = (TextView) view.findViewById(R.id.txt_date);
		txtSolarTerms = (TextView) view.findViewById(R.id.txt_solarterms);
		txtHoliday = (TextView) view.findViewById(R.id.txt_holiday);
		txtFestival = (TextView) view.findViewById(R.id.txt_festival);
		txtDayNature = (TextView) view.findViewById(R.id.txt_daynature);
		txtBible = (TextView) view.findViewById(R.id.txt_bible);
		txtPray = (TextView) view.findViewById(R.id.txt_pray);
		txtBibleSentence = (TextView) view.findViewById(R.id.bible_sentence);
		button = (Button) view.findViewById(R.id.button_test);
	}

	private void setListener() {

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void setTextColor(int dayType, boolean isHoliday) {
		switch (dayType) {
		case 0:
			break;
		case 1:
			int colorRed = getResources().getColor(
					android.R.color.holo_red_dark);
			txtDayTitle.setTextColor(colorRed);
			txtFestival.setTextColor(colorRed);
			txtDayNature.setTextColor(colorRed);
			break;
		case 2:
			int colorBlue = getResources().getColor(
					android.R.color.holo_blue_dark);
			txtDayTitle.setTextColor(colorBlue);
			txtFestival.setTextColor(colorBlue);
			txtDayNature.setTextColor(colorBlue);
			break;
		default:
			break;
		}
		if (isHoliday) {
			int colorRed = getResources().getColor(
					android.R.color.holo_red_dark);
			txtHoliday.setTextColor(colorRed);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("dateString", dateString);
	}

}
