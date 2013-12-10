package org.cathassist.bible.read;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.cathassist.bible.MainActivity;
import org.cathassist.bible.MarkContentActivity;
import org.cathassist.bible.R;
import org.cathassist.bible.lib.Func;
import org.cathassist.bible.lib.Para;
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
    private String mPath = "chn";
    private MainActivity mActivity = null;
    private ListView mList;
    private ImageView mLeft, mRight, mSound;
    private View mMusicPanel;
    private TextView mStart, mPlay, mPrev, mNext, mMode;
    private Button mMusicCancel, mMusicDown, mMusicDownBook;
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
        mStart = (TextView) view.findViewById(R.id.button_start);
        mProgress = (SeekBar) view.findViewById(R.id.bar_mp3);
        mMp3Layout = (LinearLayout) view.findViewById(R.id.layout_mp3);

        mLeft = (ImageView) view.findViewById(R.id.action_bar_left);
        mLeft.setOnClickListener(this);
        mRight = (ImageView) view.findViewById(R.id.action_bar_right);
        mRight.setOnClickListener(this);
        mSound = (ImageView) view.findViewById(R.id.action_bar_sound);
        mSound.setOnClickListener(this);

        mMusicPanel = view.findViewById(R.id.music_panel);
        mMusicPanel.setVisibility(View.GONE);
        mMusicCancel = (Button) view.findViewById(R.id.btn_music_cancel);
        mMusicCancel.setOnClickListener(this);
        mMusicDown = (Button) view.findViewById(R.id.btn_music_down);
        mMusicDown.setOnClickListener(this);
        mMusicDownBook = (Button) view.findViewById(R.id.btn_music_down_book);
        mMusicDownBook.setOnClickListener(this);
        mPrev = (TextView) view.findViewById(R.id.music_prev);
        mPrev.setOnClickListener(this);
        mPlay = (TextView) view.findViewById(R.id.music_play);
        mPlay.setOnClickListener(this);
        mNext = (TextView) view.findViewById(R.id.music_next);
        mNext.setOnClickListener(this);
        mMode = (TextView) view.findViewById(R.id.music_mode);
        mMode.setOnClickListener(this);

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
        if (Para.mp3_ver == 0) {
            mMp3Layout.setVisibility(View.GONE);
            Para.bibleMp3Pos = 0;
        }
        mMp3Pos = Para.bibleMp3Pos;
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
        Para.currentSection = Integer.valueOf(mContent.get(mList.getFirstVisiblePosition()).get("section"));
        Para.bibleMp3Pos = mMp3Pos;
        StopMp3();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.bible_read_menu,menu);
        MenuItem book = menu.findItem(R.id.book);
        MenuItem chapter = menu.findItem(R.id.chapter);
        book.setTitle(VerseInfo.CHN_NAME[Para.currentBook]);
        chapter.setTitle(String.valueOf(Para.currentChapter));
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                CheckMp3();
                break;
            case R.id.action_bar_left:
                ChangeVerse(false);
                break;
            case R.id.action_bar_right:
                ChangeVerse(true);
                break;
            case R.id.action_bar_sound:
                mMusicPanel.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_music_cancel:
                mMusicPanel.setVisibility(View.GONE);
                break;
            case R.id.btn_music_down:
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
                    downChapter();
                } else {
                    download(Para.currentBook, Para.currentChapter);
                    Toast.makeText(mActivity, "下载已添加", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_music_down_book:
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
                    Toast.makeText(getSherlockActivity(),"您的操作系统版本过低，不能使用此功能",Toast.LENGTH_SHORT).show();
                } else {
                    for(int i=1;i<=VerseInfo.CHAPTER_COUNT[Para.currentBook];i++) {
                        download(Para.currentBook,i);
                    }
                    Toast.makeText(mActivity, "下载已添加", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void downChapter() {
        final File file = Func.getFilePath(Func.getFileName(mPath, Para.currentBook, Para.currentChapter));

        if (!file.exists()) {
            new File(file.getParent()).mkdirs();
            final String url = Para.BIBLE_MP3_URL
                    + String.format("%03d", Para.currentBook) + "/"
                    + String.format("%03d", Para.currentChapter) + ".mp3";

            if (Func.isWifi(mActivity) || Para.allow_gprs) {
                final ProgressShow dialog = new ProgressShow(
                        mActivity, "请稍候", "正在载入", ProgressShow.DIALOG_TYPE_SPINNER, ProgressShow.DIALOG_DEFAULT_MAX);
                dialog.ShowDialog(new ProgressCallBack() {
                    public void action() {
                        if (Download.DownFile(url, file.getAbsolutePath())) {
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int pos = position;
        AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setIcon(android.R.drawable.btn_star)
                .setTitle("功能")
                .setItems(Para.VERSE_CHOISE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> map = mContent.get(pos);

                        String bookName = VerseInfo.CHN_NAME[Integer.parseInt(map.get("book"))];
                        String content = map.get("verse") + "\n\n"
                                + "(" + bookName + " "
                                + map.get("chapter") + ":"
                                + map.get("section") + ")";

                        switch (which) {
                            case Para.VERSE_CHOISE_MARK:
                                Intent intent = new Intent();
                                intent.putExtra("book", Integer.parseInt(map.get("book")));
                                intent.putExtra("chapter", Integer.parseInt(map.get("chapter")));
                                intent.putExtra("section", Integer.parseInt(map.get("section")));
                                intent.setClass(mActivity, MarkContentActivity.class);
                                startActivity(intent);
                                break;
                            case Para.VERSE_CHOISE_COPY:
                                ClipboardManager clip = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                clip.setText(content); // 复制
                                Toast.makeText(mActivity, content + "    已复制到剪贴板", Toast.LENGTH_SHORT).show();
                                break;
                            case Para.VERSE_CHOISE_SHARE:
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
            final String mp3 = Para.BIBLE_MP3_PATH + "chn/"
                    + String.format("%03d", Para.currentBook) + "_"
                    + String.format("%03d", Para.currentChapter) + ".mp3";
            final File file = new File(mp3);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                if (Func.isWifi(mActivity) || Para.allow_gprs) {
                    final String url = Para.BIBLE_MP3_URL
                            + String.format("%03d", Para.currentBook) + "/"
                            + String.format("%03d", Para.currentChapter) + ".mp3";
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
        String file = Para.BIBLE_MP3_PATH + "chn/"
                + String.format("%03d", Para.currentBook) + "_"
                + String.format("%03d", Para.currentChapter) + ".mp3";
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
        Para.bibleMp3Pos = mMp3Pos;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void download(final int book, final int chapter) {
        final File file = Func.getFilePath(Func.getFileName(mPath, book, chapter));
        if (!file.exists()) {
            new File(file.getParent()).mkdirs();
            if (Func.isWifi(mActivity) || Para.allow_gprs) {
                String url = Func.getUrlPath(Func.getUrlName(mPath, book, chapter));
                DownloadManager downloadManager = (DownloadManager) getSherlockActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDestinationUri(Uri.fromFile(file));
                request.setTitle(getString(R.string.app_name));
                request.setDescription("下载MP3中...");

                try {
                    long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Toast.makeText(getSherlockActivity(), "无法下载，请稍候重试（可能是同时进行的下载任务太多了）", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mActivity, "下载失败\n请在WIFI环境下再下载\n或在设置中打开使用数据流量选项", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void GetVerse() {
        mContent = GetData();
        BibleAdapter adapter = new BibleAdapter(getSherlockActivity());
        mList.setAdapter(adapter);

        SharedPreferences settings = mActivity.getSharedPreferences(Para.STORE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentBook", Para.currentBook);
        editor.putInt("currentChapter", Para.currentChapter);
        editor.putInt("currentSection", Para.currentSection);
        editor.commit();
    }

    public void SetButtonName() {
        Para.currentCount = VerseInfo.CHAPTER_COUNT[Para.currentBook];

        if (Para.currentBook != 1) {
            Para.previousCount = VerseInfo.CHAPTER_COUNT[Para.currentBook - 1];
        }

        if (isAdded()) {
            mActivity.supportInvalidateOptionsMenu();
        }
    }

    private void ChangePosition() {
        int section = Para.currentSection;
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
            if (Para.currentChapter > 1) {
                Para.currentChapter--;
            } else {
                if (Para.currentBook > 1) {
                    Para.currentBook--;
                    Para.currentChapter = Para.previousCount;
                }
            }
        } else {
            if (Para.currentChapter < Para.currentCount) {
                Para.currentChapter++;
            } else {
                if (Para.currentBook < 73) {
                    Para.currentBook++;
                    Para.currentChapter = 1;
                }
            }
        }
        StopMp3();
        Para.currentSection = 0;
        Para.bibleMp3Pos = 0;
        mMp3Pos = Para.bibleMp3Pos;
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
            db = new Database(mActivity).DbConnection(Para.DB_CONTENT_PATH + Para.DB_CONTENT_NAME);

            String sql = "select book,chapter,section,chn from cathbible where book = " + Para.currentBook
                    + " and chapter = " + Para.currentChapter;

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

            dbMark = new Database(mActivity).DbConnection(Para.DB_DATA_PATH + Para.DB_DATA_NAME);
            sql = "select * from bookmark where book = " + Para.currentBook
                    + " and chapter = " + Para.currentChapter;
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

            vh.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, Para.font_size);

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

            if (Para.show_color) {
                if (mMarkedSections.contains(section)) {
                    vh.title.setTextColor(Para.HIGHLIGHT_TEXT_COLOR);
                    vh.content.setTextColor(Para.HIGHLIGHT_TEXT_COLOR);
                } else {
                    vh.title.setTextColor(Para.DEFAULT_TEXT_COLOR);
                    vh.content.setTextColor(Para.DEFAULT_TEXT_COLOR);
                }
            }

            if (position == getCount() - 1) {
                convertView.setPadding(5, 5, 5, 100);
            } else {
                convertView.setPadding(5, 5, 5, 5);
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
