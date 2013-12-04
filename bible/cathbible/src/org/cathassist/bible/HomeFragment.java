package org.cathassist.bible;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.Database;
import org.cathassist.bible.lib.VerseInfo;
import org.cathassist.bible.read.BibleReadFragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import java.util.Random;

public class HomeFragment extends SherlockFragment implements OnClickListener {
    private MainActivity mActivity;
    private ActionBar mActionBar;
    private FragmentManager mManager;
    private TextView mVerse;
    private TextView mMark;
    private TextView mLast;
    private Button mShare;
    private LinearLayout mStatus;
    private int mVerseBook = 0;
    private int mVerseChapter = 0;
    private int mVerseSection = 0;
    private int mMarkBook = 0;
    private int mMarkChapter = 0;
    private int mMarkSection = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mActivity = (MainActivity) getSherlockActivity();
        mActionBar = mActivity.getSupportActionBar();
        mManager = mActivity.getSupportFragmentManager();
        mActivity.getSupportActionBar().setTitle(R.string.app_name);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, null);
        mVerse = (TextView) view.findViewById(R.id.text_verse);
        mMark = (TextView) view.findViewById(R.id.text_mark);
        mLast = (TextView) view.findViewById(R.id.text_last);
        mShare = (Button) view.findViewById(R.id.button_share);
        mStatus = (LinearLayout) view.findViewById(R.id.layout_status);

        mVerse.setOnClickListener(this);
        mMark.setOnClickListener(this);
        mLast.setOnClickListener(this);
        mShare.setOnClickListener(this);

        if (new Database(mActivity).OpenDbCheck()) {
            mStatus.setVisibility(View.VISIBLE);
            mShare.setVisibility(View.VISIBLE);
            ShowVerse();
            ShowMark();
            ShowLast();
        } else {
            mVerse.setText("天主竟这样爱了世界，甚至赐下了自己的独生子，使凡信他的人不至丧亡，反而获得永生。(若望福音 3:16)");
            mStatus.setVisibility(View.GONE);
            mShare.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                if (new Database(mActivity).OpenDbCheck()) {
                    mStatus.setVisibility(View.VISIBLE);
                    mShare.setVisibility(View.VISIBLE);
                    ShowVerse();
                    ShowMark();
                    ShowLast();
                } else {
                    mVerse.setText("天主竟这样爱了世界，甚至赐下了自己的独生子，使凡信他的人不至丧亡，反而获得永生。(若望福音 3:16)");
                    mStatus.setVisibility(View.GONE);
                    mShare.setVisibility(View.GONE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragTrans;

        switch (v.getId()) {
            case R.id.text_verse:
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
            case R.id.text_mark:
                if (mMarkBook * mMarkChapter * mMarkSection != 0) {
                    CommonPara.currentBook = mMarkBook;
                    CommonPara.currentChapter = mMarkChapter;
                    CommonPara.currentSection = mMarkSection;

                    CommonPara.menuIndex = CommonPara.MENU_BIBLE;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    fragTrans = mManager.beginTransaction();
                    fragTrans.replace(R.id.content_frame, new BibleReadFragment());
                    fragTrans.commit();
                }
                break;
            case R.id.text_last:
                if (CommonPara.lastBook * CommonPara.lastChapter * CommonPara.lastSection != 0) {
                    CommonPara.currentBook = CommonPara.lastBook;
                    CommonPara.currentChapter = CommonPara.lastChapter;
                    CommonPara.currentSection = CommonPara.lastSection;

                    CommonPara.menuIndex = CommonPara.MENU_BIBLE;
                    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    fragTrans = mManager.beginTransaction();
                    fragTrans.replace(R.id.content_frame, new BibleReadFragment());
                    fragTrans.commit();
                }
                break;
            case R.id.button_share:
                if (mVerseBook * mVerseChapter * mVerseSection != 0) {
                    String content = mVerse.getText().toString().trim();
                    final UMSocialService mController = UMServiceFactory.getUMSocialService("cathbible",
                            RequestType.SOCIAL);
                    mController.setShareContent(content);
                    mController.openShare(getActivity(), false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void ShowVerse() {
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
                while (cursor.moveToNext()) {
                    String tempContent = cursor.getString(cursor.getColumnIndex("chn"));
                    if (!TextUtils.isEmpty(tempContent) && !"NULL".equals(tempContent)) {
                        content += tempContent;
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
            mVerseBook = 0;
            mVerseChapter = 0;
            mVerseSection = 0;

            mVerse.setText("天主竟这样爱了世界，甚至赐下了自己的独生子，使凡信他的人不至丧亡，反而获得永生。(若望福音 3:16)");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void ShowMark() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = new Database(getSherlockActivity()).DbConnection(CommonPara.DB_DATA_PATH + CommonPara.DB_DATA_NAME);

            if (db != null) {
                String sql = "select * from bookmark order by updatetime DESC limit 1";
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                int book = cursor.getInt(cursor.getColumnIndex("book"));
                int chapter = cursor.getInt(cursor.getColumnIndex("chapter"));
                int section = cursor.getInt(cursor.getColumnIndex("section"));
                String title = cursor.getString(cursor.getColumnIndex("title"));

                String bookName = "(" + VerseInfo.CHN_NAME[book]
                        + " " + chapter;
                if (section > 1000) {
                    bookName += ")";
                } else {
                    bookName += ":" + section + ")";
                }

                mMarkBook = book;
                mMarkChapter = chapter;
                mMarkSection = section;

                mMark.setText(title + bookName);
            }
        } catch (Exception e) {
            mMarkBook = 0;
            mMarkChapter = 0;
            mMarkSection = 0;

            mMark.setText("无最新书签");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void ShowLast() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = new Database(mActivity).DbConnection(CommonPara.DB_CONTENT_PATH + CommonPara.DB_CONTENT_NAME);

            if (db != null) {
                String sql = "select chn from cathbible where book = " + CommonPara.lastBook
                        + " and chapter = " + CommonPara.lastChapter
                        + " and section = " + CommonPara.lastSection;
                cursor = db.rawQuery(sql, null);

                cursor.moveToFirst();
                String content = cursor.getString(cursor.getColumnIndex("chn"));

                String title = "(" + VerseInfo.CHN_NAME[CommonPara.lastBook] + " " + CommonPara.lastChapter;
                if (CommonPara.lastSection > 1000) {
                    title += ")";
                } else {
                    title += ":" + CommonPara.lastSection + ")";
                }

                mLast.setText(content + title);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonPara.lastBook = 0;
            CommonPara.lastChapter = 0;
            CommonPara.lastSection = 0;

            mLast.setText("无上次阅读经文");
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
