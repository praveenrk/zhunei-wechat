package org.cathassist.bible;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import org.cathassist.bible.lib.CommonPara;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavFragment extends SherlockFragment implements OnItemClickListener {
    private static final int INTENT_SET = 0;
    private MainActivity mActivity;
    private ActionBar mActionBar;
    private FragmentManager mManager;
    private Fragment mFragment;
    private ListView mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getSherlockActivity();
        mActionBar = mActivity.getSupportActionBar();
        mManager = mActivity.getSupportFragmentManager();
        CommonPara.menuIndex = CommonPara.MENU_HOME;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav, null);
        mMenu = (ListView) view.findViewById(R.id.list);
        mMenu.setOnItemClickListener(this);
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < CommonPara.MENU_NAME.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("img", Integer.toString(R.drawable.ic_launcher));
            map.put("text", CommonPara.MENU_NAME[i]);
            list.add(map);
        }

        String[] from = {"img", "text"};
        int[] to = {R.id.img, R.id.text};

        SimpleAdapter adapter = new SimpleAdapter(mActivity, list, R.layout.nav_item, from, to);
        mMenu.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case CommonPara.MENU_HOME:
                if (CommonPara.menuIndex != CommonPara.MENU_HOME) {
                    CommonPara.menuIndex = CommonPara.MENU_HOME;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    FragmentTransaction fragTrans = mManager.beginTransaction();
                    mFragment = new HomeFragment();
                    fragTrans.replace(R.id.content_frame, mFragment);
                    fragTrans.commit();
                }
                delayToggle();
                break;

            case CommonPara.MENU_BIBLE:
                if (CommonPara.menuIndex != CommonPara.MENU_BIBLE) {
                    CommonPara.menuIndex = CommonPara.MENU_BIBLE;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    FragmentTransaction fragTrans = mManager.beginTransaction();
                    mFragment = new BibleReadFragment();
                    fragTrans.replace(R.id.content_frame, mFragment);
                    fragTrans.commit();
                }
                delayToggle();
                break;

            case CommonPara.MENU_MARK:
                if (CommonPara.menuIndex != CommonPara.MENU_MARK) {
                    CommonPara.menuIndex = CommonPara.MENU_MARK;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    FragmentTransaction fragTrans = mManager.beginTransaction();
                    mFragment = new MarkFragment();
                    fragTrans.replace(R.id.content_frame, mFragment);
                    fragTrans.commit();
                }
                delayToggle();
                break;

            case CommonPara.MENU_VERSE:
                if (CommonPara.menuIndex != CommonPara.MENU_VERSE) {
                    CommonPara.menuIndex = CommonPara.MENU_VERSE;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    FragmentTransaction fragTrans = mManager.beginTransaction();
                    mFragment = new VerseFragment();
                    fragTrans.replace(R.id.content_frame, mFragment);
                    fragTrans.commit();
                }
                delayToggle();
                break;
            case CommonPara.MENU_SEARCH:
                if (CommonPara.menuIndex != CommonPara.MENU_SEARCH) {
                    CommonPara.menuIndex = CommonPara.MENU_SEARCH;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    FragmentTransaction fragTrans = mManager.beginTransaction();
                    mFragment = new SearchFragment();
                    fragTrans.replace(R.id.content_frame, mFragment);
                    fragTrans.commit();
                }
                delayToggle();
                break;

            case CommonPara.MENU_SET:
                Intent intent;
                intent = new Intent();
                intent.setClass(mActivity, SettingActivity.class);
                startActivityForResult(intent, INTENT_SET);
                break;
        }
    }

    private void delayToggle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.getSlidingMenu().toggle();
            }
        }, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_SET:
                if (resultCode == CommonPara.NEED_RESTART) {
                    Reload();
                }
                break;
        }
    }

    protected void Reload() {
        Intent intent = mActivity.getIntent();
        mActivity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mActivity.finish();
        mActivity.overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
