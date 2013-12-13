package daily.cathassist.org.provider;

import java.util.ArrayList;

import com.viewpagerindicator.IconPagerAdapter;

import daily.cathassist.org.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class MainActivityFragmentPagerAdapter extends FragmentPagerAdapter
		implements IconPagerAdapter {
	Activity context;
	private ArrayList<Fragment> fragments;
	private FragmentManager fm;

	public MainActivityFragmentPagerAdapter(Activity context,
			FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
		super(fragmentManager);
		this.context = context;
		this.fm = fragmentManager;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return 7;
	}

	@Override
	public String getPageTitle(int position) {
		switch (position) {
		case 0:
			return context.getString(R.string.main);
		case 1:
			return context.getString(R.string.laudes);
		case 2:
			return context.getString(R.string.horamedia);
		case 3:
			return context.getString(R.string.matutinum);
		case 4:
			return context.getString(R.string.vesperae);
		case 5:
			return context.getString(R.string.completorium);
		case 6:
			return context.getString(R.string.mass);
		default:
			return "";
		}
	}

	@Override
	public int getIconResId(int index) {
		return 0;
	}

	public void setFragments(ArrayList<Fragment> fragments) {
		if (this.fragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}
}
