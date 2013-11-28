package org.cathassist.bible;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import org.cathassist.bible.lib.CommonFunc;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.Database;
import org.cathassist.bible.lib.Download;
import org.cathassist.bible.lib.ProgressShow;
import org.cathassist.bible.lib.ProgressShow.ProgressCallBack;
import org.cathassist.bible.lib.VerseInfo;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibleReadFragment extends SherlockFragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener, OnSeekBarChangeListener {
    List<Map<String, String>> mContent;
    private MainActivity mActivity = null;
    private ListView mList;
    private Button mStart;
    private SeekBar mProgress;
    private LinearLayout mMp3Layout;
    private List<Integer> mMarkedSections = new ArrayList<Integer>();
    private MediaPlayer mMediaPlayer;
    private Handler mMp3Handler = null;
    private Thread mMp3Thread = null;
    private int mMp3Pos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getSherlockActivity();
        setHasOptionsMenu(true);
        mActivity.getSupportActionBar().setTitle("圣经");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bible_read, null);
        mList = (ListView) view.findViewById(R.id.list);
        mStart = (Button) view.findViewById(R.id.button_start);
        mProgress = (SeekBar) view.findViewById(R.id.bar_mp3);
        mMp3Layout = (LinearLayout) view.findViewById(R.id.layout_mp3);

        mList.setOnItemClickListener(this);
        mList.setOnItemLongClickListener(this);

        mStart.setOnClickListener(this);
        mProgress.setOnSeekBarChangeListener(this);

        mMp3Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                CheckMp3();
                super.handleMessage(msg);
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonPara.mp3_ver == 0) {
            mMp3Layout.setVisibility(View.GONE);
            CommonPara.bibleMp3Pos = 0;
        }
        mMp3Pos = CommonPara.bibleMp3Pos;
        if (mMp3Pos == 0) {
            mProgress.setProgress(mMp3Pos);
        }
        SetButtonName();
        GetVerse();
        ChangePosition();
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonPara.currentSection = Integer.valueOf(mContent.get(mList.getFirstVisiblePosition()).get("section"));
        CommonPara.bibleMp3Pos = mMp3Pos;
        StopMp3();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.bible_read_menu,menu);
        MenuItem book = menu.findItem(R.id.book);
        MenuItem chapter = menu.findItem(R.id.chapter);
        book.setTitle(VerseInfo.CHN_ABBR[CommonPara.currentBook]);
        chapter.setTitle(String.valueOf(CommonPara.currentChapter));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.book:
                intent.setClass(mActivity, BookSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.chapter:
                intent.setClass(mActivity, ChapterSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.left:
                ChangeVerse(false);
                break;
            case R.id.right:
                ChangeVerse(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                CheckMp3();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int pos = position;
        AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setIcon(android.R.drawable.btn_star)
                .setTitle("功能")
                .setItems(CommonPara.VERSE_CHOISE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> map = mContent.get(pos);

                        String bookName = VerseInfo.CHN_NAME[Integer.parseInt(map.get("book"))];
                        String content = map.get("verse") + "\n\n"
                                + "(" + bookName + " "
                                + map.get("chapter") + ":"
                                + map.get("section") + ")";

                        switch (which) {
                            case CommonPara.VERSE_CHOISE_MARK:
                                Intent intent = new Intent();
                                intent.putExtra("book", Integer.parseInt(map.get("book")));
                                intent.putExtra("chapter", Integer.parseInt(map.get("chapter")));
                                intent.putExtra("section", Integer.parseInt(map.get("section")));
                                intent.setClass(mActivity, MarkContentActivity.class);
                                startActivity(intent);
                                break;
                            case CommonPara.VERSE_CHOISE_COPY:
                                ClipboardManager clip = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                clip.setText(content); // 复制
                                Toast.makeText(mActivity, content + "    已复制到剪贴板", Toast.LENGTH_SHORT).show();
                                break;
                            case CommonPara.VERSE_CHOISE_SHARE:
                                final UMSocialService mController = UMServiceFactory.getUMSocialService("cathbible",
                                        RequestType.SOCIAL);
                                mController.setShareContent(content);
                                mController.openShare(getActivity(), false);
                                break;
                        }
                    }

                }).create();
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Map<String, String> map = mContent.get(arg2);

        Intent intent = new Intent();
        intent.putExtra("book", Integer.parseInt(map.get("book")));
        intent.putExtra("chapter", Integer.parseInt(map.get("chapter")));
        intent.putExtra("section", Integer.parseInt(map.get("section")));
        intent.setClass(mActivity, MarkContentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (fromUser) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.seekTo(progress * mMediaPlayer.getDuration() / 100);
            }
        }
    }

    private void CheckMp3() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            StopMp3();
        } else {
            final String mp3 = CommonPara.BIBLE_MP3_PATH + "chn/"
                    + String.format("%03d", CommonPara.currentBook) + "_"
                    + String.format("%03d", CommonPara.currentChapter) + ".mp3";
            File file = new File(mp3);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                final String url = CommonPara.BIBLE_MP3_URL
                        + String.format("%03d", CommonPara.currentBook) + "/"
                        + String.format("%03d", CommonPara.currentChapter) + ".mp3";

                if (CommonFunc.isWifi(mActivity) || CommonPara.allow_gprs) {
                    final ProgressShow dialog = new ProgressShow(
                            mActivity, "请稍候", "正在载入", ProgressShow.DIALOG_TYPE_SPINNER, ProgressShow.DIALOG_DEFAULT_MAX);
                    dialog.ShowDialog(new ProgressCallBack() {
                        public void action() {
                            if (Download.DownFile(url, mp3)) {
                                mMp3Handler.sendEmptyMessage(0);
                            }
                            dialog.CloseDialog();
                        }
                    });
                } else {
                    Toast.makeText(mActivity, "下载失败\n请在WIFI环境下再下载\n或在设置中打开使用数据流量选项", Toast.LENGTH_LONG).show();
                }

            } else {
                PlayMp3();
            }
        }
    }

    private void PlayMp3() {
        String file = CommonPara.BIBLE_MP3_PATH + "chn/"
                + String.format("%03d", CommonPara.currentBook) + "_"
                + String.format("%03d", CommonPara.currentChapter) + ".mp3";
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(file);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer player) {
                    try {
                        mMediaPlayer.stop();
                        ChangeVerse(true);
                        CheckMp3();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mMediaPlayer.start();
            mMediaPlayer.seekTo(mMp3Pos);
            mMp3Thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (mMediaPlayer.isPlaying()) {
                            mProgress.setProgress(mMediaPlayer.getCurrentPosition() * 100 / mMediaPlayer.getDuration());
                            mMp3Pos = mMediaPlayer.getCurrentPosition();
                            Thread.sleep(200);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mMp3Thread.start();
            mStart.setText(R.string.pause_sign);
        } catch (Exception e) {
            e.printStackTrace();
            StopMp3();
        }
    }

    private void StopMp3() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        mStart.setText(R.string.start_sign);
        CommonPara.bibleMp3Pos = mMp3Pos;
    }

    private void GetVerse() {
        mContent = GetData();
        BibleAdapter adapter = new BibleAdapter(getSherlockActivity());
        mList.setAdapter(adapter);

        SharedPreferences settings = mActivity.getSharedPreferences(CommonPara.STORE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentBook", CommonPara.currentBook);
        editor.putInt("currentChapter", CommonPara.currentChapter);
        editor.putInt("currentSection", CommonPara.currentSection);
        editor.commit();
    }

    public void SetButtonName() {
        CommonPara.currentCount = VerseInfo.CHAPTER_COUNT[CommonPara.currentBook];

        if (CommonPara.currentBook != 1) {
            CommonPara.previousCount = VerseInfo.CHAPTER_COUNT[CommonPara.currentBook - 1];
        }

        if (isAdded()) {
            mActivity.supportInvalidateOptionsMenu();
        }
    }

    private void ChangePosition() {
        int section = CommonPara.currentSection;
        int position = 0;
        for (int i = 0; i < mContent.size(); i++) {
            Map<String, String> map = mContent.get(i);
            if (Integer.valueOf(map.get("section")) == section) {
                position = i;
                break;
            }
        }
        mList.setSelection(position);
    }

    public void ChangeVerse(Boolean isNext) {
        if (!isNext) {
            if (CommonPara.currentChapter > 1) {
                CommonPara.currentChapter--;
            } else {
                if (CommonPara.currentBook > 1) {
                    CommonPara.currentBook--;
                    CommonPara.currentChapter = CommonPara.previousCount;
                }
            }
        } else {
            if (CommonPara.currentChapter < CommonPara.currentCount) {
                CommonPara.currentChapter++;
            } else {
                if (CommonPara.currentBook < 66) {
                    CommonPara.currentBook++;
                    CommonPara.currentChapter = 1;
                }
            }
        }
        StopMp3();
        CommonPara.currentSection = 0;
        CommonPara.bibleMp3Pos = 0;
        mMp3Pos = CommonPara.bibleMp3Pos;
        mProgress.setProgress(mMp3Pos);

        SetButtonName();
        GetVerse();
        ChangePosition();
    }

    private List<Map<String, String>> GetData() {
        SQLiteDatabase db = null;
        SQLiteDatabase dbMark = null;
        Cursor cursor = null;
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        mMarkedSections.clear();

        try {
            db = new Database(mActivity).DbConnection(CommonPara.DB_CONTENT_PATH + CommonPara.DB_CONTENT_NAME);

            String sql = "select book,chapter,section,chn from cathbible where book = " + CommonPara.currentBook
                    + " and chapter = " + CommonPara.currentChapter;

            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex("chn"));

                if (!TextUtils.isEmpty(content)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("book", String.valueOf(cursor.getInt(cursor.getColumnIndex("book"))));
                    map.put("chapter", String.valueOf(cursor.getInt(cursor.getColumnIndex("chapter"))));
                    map.put("section", String.valueOf(cursor.getInt(cursor.getColumnIndex("section"))));
                    map.put("verse", content);
                    data.add(map);
                }
            }

            dbMark = new Database(mActivity).DbConnection(CommonPara.DB_DATA_PATH + CommonPara.DB_DATA_NAME);
            sql = "select * from bookmark where book = " + CommonPara.currentBook
                    + " and chapter = " + CommonPara.currentChapter;
            cursor = dbMark.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                mMarkedSections.add(cursor.getInt(cursor.getColumnIndex("section")));
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
            if (dbMark != null) {
                dbMark.close();
            }
        }

        return data;
    }

    class BibleAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public BibleAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.bible_read_item, null);

                vh.title = (TextView) convertView.findViewById(R.id.title);
                vh.content = (TextView) convertView.findViewById(R.id.content);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, CommonPara.font_size);

            Map<String, String> map = mContent.get(position);
            //int book = Integer.valueOf(map.get("book"));
            //int chapter = Integer.valueOf(map.get("chapter"));
            int section = Integer.valueOf(map.get("section"));
            String verse = map.get("verse");
            TextPaint paint = vh.content.getPaint();
            if (section > 1000) {
                vh.title.setText("");
                paint.setFakeBoldText(true);
            } else {
                vh.title.setText(String.valueOf(section));
                paint.setFakeBoldText(false);
            }
            vh.title.setText(section > 1000 ? "" : String.valueOf(section));
            vh.content.setText(verse);

            if (CommonPara.show_color) {
                if (mMarkedSections.contains(section)) {
                    vh.title.setTextColor(CommonPara.HIGHLIGHT_TEXT_COLOR);
                    vh.content.setTextColor(CommonPara.HIGHLIGHT_TEXT_COLOR);
                } else {
                    vh.title.setTextColor(CommonPara.DEFAULT_TEXT_COLOR);
                    vh.content.setTextColor(CommonPara.DEFAULT_TEXT_COLOR);
                }
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return mContent.size();
        }

        @Override
        public Object getItem(int position) {
            return mContent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView title;
            TextView content;
        }
    }

}
