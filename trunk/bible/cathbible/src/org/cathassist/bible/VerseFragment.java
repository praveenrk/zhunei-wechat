package org.cathassist.bible;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.Database;
import org.cathassist.bible.lib.VerseInfo;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;

import java.util.Random;

public class VerseFragment extends SherlockFragment implements OnClickListener {
    private TextView mVerse;
    private int mVerseBook = 0;
    private int mVerseChapter = 0;
    private int mVerseSection = 0;
    private MainActivity mActivity = null;
    private ActionBar mActionBar = null;
    private FragmentManager mManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getSherlockActivity();
        mActionBar = mActivity.getSupportActionBar();
        mManager = mActivity.getSupportFragmentManager();
        mActivity.getSupportActionBar().setTitle("金句");

        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verse, null);
        mVerse = (TextView) view.findViewById(R.id.text_verse_content);
        mVerse.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        GetVerse();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.verse_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next:
                GetVerse();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragTrans;

        switch (v.getId()) {
            case R.id.text_verse_content:
                if (mVerseBook * mVerseChapter * mVerseSection != 0) {
                    CommonPara.currentBook = mVerseBook;
                    CommonPara.currentChapter = mVerseChapter;
                    CommonPara.currentSection = mVerseSection;

                    CommonPara.menuIndex = CommonPara.MENU_BIBLE;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    fragTrans = mManager.beginTransaction();
                    fragTrans.replace(R.id.content_frame, new BibleReadFragment());
                    fragTrans.commit();
                }
                break;
        }
    }

    private void GetVerse() {
        Random random = new Random(System.currentTimeMillis());
        int id = random.nextInt(CommonPara.VERSE_NUMBER) % (CommonPara.VERSE_NUMBER - 1 + 1) + 1;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = new Database(mActivity).DbConnection(CommonPara.DB_CONTENT_PATH + CommonPara.DB_CONTENT_NAME);

            if (db != null) {
                String sql = "select * from verse where _id = " + id;
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                int book = cursor.getInt(cursor.getColumnIndex("book"));
                int chapter = cursor.getInt(cursor.getColumnIndex("chapter"));
                int bSection = cursor.getInt(cursor.getColumnIndex("bSection"));
                int eSection = cursor.getInt(cursor.getColumnIndex("eSection"));

                sql = "select chn from cathbible where book = " + book + " and chapter = " + chapter
                        + " and section >= " + bSection + " and section <= " + eSection;
                cursor = db.rawQuery(sql, null);

                String content = "";
                String tempContent;
                while (cursor.moveToNext()) {
                    tempContent = cursor.getString(cursor.getColumnIndex("chn"));
                    if (tempContent != null && !"NULL".equals(tempContent)) {
                        content += tempContent.trim();
                    }
                }

                String title = "(" + VerseInfo.CHN_NAME[book] + " " + chapter + ":" + bSection;

                if (bSection == eSection) {
                    title += ")";
                } else {
                    title += "-" + eSection + ")";
                }

                mVerseBook = book;
                mVerseChapter = chapter;
                mVerseSection = bSection;

                mVerse.setText(content + title);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mVerseBook = 0;
            mVerseChapter = 0;
            mVerseSection = 0;

            mVerse.setText("神爱世人,甚至将他的独生子赐给他们,叫一切信他的,不至灭亡,反得永生。(约翰福音 3:16)");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
