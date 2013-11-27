package org.cathassist.bible;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.Database;
import org.cathassist.bible.lib.VerseInfo;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends SherlockFragment implements OnClickListener, OnItemClickListener {
    int count = 0;
    private Button button_search;
    private ListView list_search;
    private EditText text_search;
    private TextView text_search_count;
    private MainActivity mActivity = null;
    private ActionBar mActionBar = null;
    private FragmentManager mManager = null;
    private List<Map<String, String>> mData = new ArrayList<Map<String, String>>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getSherlockActivity();
        mActionBar = mActivity.getSupportActionBar();
        mManager = mActivity.getSupportFragmentManager();
        mActivity.getSupportActionBar().setTitle("搜索");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, null);

        button_search = (Button) view.findViewById(R.id.button_search);
        list_search = (ListView) view.findViewById(R.id.list);
        text_search = (EditText) view.findViewById(R.id.text_word);
        text_search_count = (TextView) view.findViewById(R.id.text_count);

        button_search.setOnClickListener(this);
        list_search.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = mActivity.getSharedPreferences(CommonPara.STORE_NAME, Context.MODE_PRIVATE);
        text_search.setText(settings.getString("search_key", ""));
        text_search.setSelectAllOnFocus(true);

        SearchVerse();
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonPara.searchPos = list_search.getFirstVisiblePosition();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_search:
                CommonPara.searchPos = 0;
                SearchVerse();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Map<String, String> map = mData.get(arg2);

        FragmentTransaction fragTrans;
        CommonPara.currentBook = Integer.parseInt(map.get("book"));
        CommonPara.currentChapter = Integer.parseInt(map.get("chapter"));
        CommonPara.currentSection = Integer.parseInt(map.get("section"));
        CommonPara.bibleDevitionPos = 0;

        CommonPara.menuIndex = CommonPara.MENU_BIBLE;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        fragTrans = mManager.beginTransaction();
        fragTrans.replace(R.id.content_frame, new BibleReadFragment());
        fragTrans.commit();
    }

    private void SearchVerse() {
        list_search.setAdapter(null);
        text_search_count.setText("正在搜索，请稍候");
        mData = GetData();

        try {
            SimpleAdapter adapter = new SimpleAdapter(mActivity,
                    mData,
                    R.layout.search_item,
                    new String[]{"title", "verse"},
                    new int[]{R.id.title, R.id.content});

            list_search.setAdapter(adapter);
            list_search.setSelection(CommonPara.searchPos);
            text_search_count.setText("共搜索到" + count + "条经文");

            InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService
                    (Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> GetData() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        try {
            db = new Database(mActivity)
                    .DbConnection(CommonPara.DB_CONTENT_PATH
                            + CommonPara.DB_CONTENT_NAME);

            String text = text_search.getText().toString().trim();
            if (!text.equals("")) {
                SharedPreferences settings = mActivity.getSharedPreferences(
                        CommonPara.STORE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("search_key", text);
                editor.commit();

                String condition = "%"
                        + text_search.getText().toString().trim()
                        .replace(' ', '%') + "%";
                String sql = "select book,chapter,section,chn from cathbible where chn like '" + condition + "' ";

                cursor = db.rawQuery(sql, null);

                count = cursor.getCount();
                while (cursor.moveToNext()) {
                    int book = cursor.getInt(cursor.getColumnIndex("book"));

                    String bookName = VerseInfo.CHN_ABBR[book];
                    bookName += cursor.getInt(cursor.getColumnIndex("chapter"));
                    if(cursor.getInt(cursor.getColumnIndex("section")) <= 1000) {
                        bookName += ":\n"+ cursor.getInt(cursor.getColumnIndex("section"));
                    }
                    String content = cursor.getString(cursor.getColumnIndex("chn")).trim();

                    if (!content.trim().equals("")) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("book", cursor.getString(cursor.getColumnIndex("book")));
                        map.put("chapter", cursor.getString(cursor.getColumnIndex("chapter")));
                        map.put("section", cursor.getString(cursor.getColumnIndex("section")));
                        map.put("title", bookName.trim());
                        map.put("verse", content.trim());
                        data.add(map);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return data;
    }
}
