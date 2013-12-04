package org.cathassist.bible;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import org.cathassist.bible.lib.Database;
import org.cathassist.bible.read.BibleReadFragment;

import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkFragment extends SherlockFragment implements OnItemClickListener {
    private MainActivity mActivity = null;
    private ActionBar mActionBar = null;
    private FragmentManager mManager = null;
    private ListView mList;
    private List<Map<String, String>> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getSherlockActivity();
        mActionBar = mActivity.getSupportActionBar();
        mManager = mActivity.getSupportFragmentManager();
        mActivity.getSupportActionBar().setTitle("书签");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mark, null);
        mList = (ListView) view.findViewById(R.id.list);
        mList.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetBookmark();
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonPara.bookmarkPos = mList.getFirstVisiblePosition();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Map<String, String> map = mData.get(arg2);

        FragmentTransaction fragTrans;
        CommonPara.currentBook = Integer.parseInt(map.get("book"));
        CommonPara.currentChapter = Integer.parseInt(map.get("chapter"));
        CommonPara.currentSection = Integer.parseInt(map.get("section"));

        CommonPara.menuIndex = CommonPara.MENU_BIBLE;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        fragTrans = mManager.beginTransaction();
        fragTrans.replace(R.id.content_frame, new BibleReadFragment());
        fragTrans.commit();
    }

    private void GetBookmark() {
        mData = GetData();
        try {
            SimpleAdapter adapter = new SimpleAdapter(mActivity,
                    mData,
                    R.layout.mark_item,
                    new String[]{"title", "content"},
                    new int[]{R.id.title, R.id.content});

            mList.setAdapter(adapter);
            mList.setSelection(CommonPara.bookmarkPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> GetData() {
        SQLiteDatabase db;
        SQLiteDatabase dbBook;
        Cursor cursor = null;
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        db = new Database(mActivity).DbConnection(CommonPara.DB_DATA_PATH + CommonPara.DB_DATA_NAME);
        dbBook = new Database(mActivity).DbConnection(CommonPara.DB_CONTENT_PATH + CommonPara.DB_CONTENT_NAME);
        try {
            String sql = "select * from bookmark order by updatetime DESC";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                int book = cursor.getInt(cursor.getColumnIndex("book"));
                String bookSql = "select chn from book where _id = " + book;
                Cursor bookCursor = dbBook.rawQuery(bookSql, null);
                bookCursor.moveToNext();
                String bookName = bookCursor.getString(bookCursor.getColumnIndex("chn"));
                bookCursor.close();

                Map<String, String> map = new HashMap<String, String>();
                map.put("book", cursor.getString(cursor.getColumnIndex("book")));
                map.put("chapter", cursor.getString(cursor.getColumnIndex("chapter")));
                map.put("section", cursor.getString(cursor.getColumnIndex("section")));
                map.put("title", cursor.getString(cursor.getColumnIndex("title")));
                map.put("content",
                        bookName + " " +
                                cursor.getInt(cursor.getColumnIndex("chapter")) + ":" +
                                cursor.getInt(cursor.getColumnIndex("section")));
                data.add(map);
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
            if (dbBook != null) {
                dbBook.close();
            }
        }
        return data;
    }
}
