package org.cathassist.bible.setting;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import org.cathassist.bible.R;
import org.cathassist.bible.lib.Func;
import org.cathassist.bible.lib.Para;
import org.cathassist.bible.lib.VerseInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Mp3ManageDetailActivity extends SherlockActivity {
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                mMusicAdapter.notifyDataSetChanged();
            }
        }
    };

    private String mName;
    private String mPath;
    private ExpandableListView mExpandableListView;
    private MusicManagementAdapter mMusicAdapter;
    private List<String> mGroup = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Para.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp3_manage_detail);

        mName = getIntent().getStringExtra("name");
        mPath = getIntent().getStringExtra("path");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mName);

        mExpandableListView = (ExpandableListView) findViewById(R.id.list);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                download(groupPosition + 1, childPosition + 1);
                Toast.makeText(Mp3ManageDetailActivity.this, "下载已添加", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final View v = view;
                int packedPositionType = ExpandableListView.getPackedPositionType(id);
                switch (packedPositionType) {
                    case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
                        final int childPosition = ExpandableListView.getPackedPositionChild(id);
                        View titleView = v.findViewById(R.id.child_name);
                        if (titleView != null && titleView.getTag() != null && titleView.getTag() instanceof Integer) {
                            int book = (Integer) titleView.getTag() + 1;
                            int chapter = childPosition + 1;
                            final File file = Func.getFilePath(Func.getFileName(mPath, book, chapter));
                            if (file.exists()) {
                                new AlertDialog.Builder(Mp3ManageDetailActivity.this).setTitle("确定删除此音频文件？")
                                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                file.delete();
                                                Toast.makeText(Mp3ManageDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                mMusicAdapter.notifyDataSetChanged();
                                            }
                                        }).setNegativeButton(R.string.cancel, null).show();
                            }
                        }
                        return true;
                    case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
                        final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        new AlertDialog.Builder(Mp3ManageDetailActivity.this).setTitle("确定下载整卷" + VerseInfo.CHN_NAME[groupPosition + 1] + "？")
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int book = groupPosition + 1;
                                        int chapterCount = VerseInfo.CHAPTER_COUNT[book];
                                        for (int i = 1; i <= chapterCount; i++) {
                                            download(book, i);
                                        }
                                        Toast.makeText(Mp3ManageDetailActivity.this, "《" + VerseInfo.CHN_NAME[book] + "》整卷书已加入下载队列", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton(R.string.cancel, null).show();

                        return true;
                    default:
                        break;
                }

                return false;
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.registerReceiver(receiver, filter);

        loadDate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void remove(final int book, final int chapter) {
        final File file = Func.getFilePath(Func.getFileName(mPath, book, chapter));
        file.delete();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void download(final int book, final int chapter) {
        final File file = Func.getFilePath(Func.getFileName(mPath, book, chapter));
        if (file.exists()) {
            new AlertDialog.Builder(this).setTitle("是否重新下载此音频？")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            file.delete();
                            download(book, chapter);
                        }
                    }).setNegativeButton(R.string.cancel, null).show();
        } else {
            if (Func.isWifi(this) || Para.allow_gprs) {
                String url = Func.getUrlPath(Func.getUrlName(mPath, book, chapter));
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDestinationUri(Uri.fromFile(file));
                request.setTitle(getString(R.string.app_name));
                request.setDescription("下载MP3中...");

                try {
                    long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Toast.makeText(this, "无法下载，请稍候重试（可能是同时进行的下载任务太多了）", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "下载失败\n请在WIFI环境下再下载\n或在设置中打开使用数据流量选项", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadDate() {
        mGroup.clear();
        for (int i = 1; i <= 73; i++) {
            mGroup.add(VerseInfo.CHN_NAME[i]);
        }
        mMusicAdapter = new MusicManagementAdapter(this);
        mExpandableListView.setAdapter(mMusicAdapter);
        Toast toast = Toast.makeText(this, "长按书卷名,下载该章节全部音频", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            try {
                this.unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {

            }
        }
        super.onDestroy();

    }

    class MusicManagementAdapter extends BaseExpandableListAdapter {
        Activity activity;

        public MusicManagementAdapter(Activity a) {
            activity = a;
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            return "第" + (childPosition + 1) + "章";
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return VerseInfo.CHAPTER_COUNT[groupPosition + 1];
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.mp3_manage_detail_child, null);
                vh = new ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.child_name);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            File file = Func.getFilePath(Func.getFileName(mPath, groupPosition + 1, childPosition + 1));
            String state = file.exists() ? " (已下载)":"";// : " (点击下载)";

            String title = getGroup(groupPosition) + getChild(groupPosition, childPosition) + state;
            SpannableString spannable = new SpannableString(title);
            spannable.setSpan(new ForegroundColorSpan(file.exists() ? Color.BLUE : Color.BLACK), title.length() - state.length(), title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            vh.title.setText(spannable);
            vh.title.setTag(groupPosition);

            return convertView;
        }

        @Override
        public String getGroup(int groupPosition) {
            return mGroup.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return mGroup.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.mp3_manage_detail_group, null);
                vh = new ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.name);
                vh.icon = (ImageView) convertView.findViewById(R.id.icon);
                vh.indicator = (ImageView) convertView.findViewById(R.id.indicator);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if (isExpanded) {
                vh.indicator.setBackgroundResource(R.drawable.arrow_sans_down);
            } else {
                vh.indicator.setBackgroundResource(R.drawable.arrow_sans_right);
            }
            vh.icon.setBackgroundResource(R.drawable.double_circle);
            vh.title.setText(mGroup.get(groupPosition));

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewHolder {
            public TextView title;
            public ImageView icon;
            public ImageView indicator;
        }
    }
}
