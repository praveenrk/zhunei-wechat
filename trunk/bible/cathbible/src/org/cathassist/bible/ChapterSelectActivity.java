package org.cathassist.bible;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.VerseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterSelectActivity extends SherlockActivity implements OnItemClickListener {
    GridView mChapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(CommonPara.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chapter_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChapter = (GridView) findViewById(R.id.grid_act_chapter_select_chapter);

        SimpleAdapter adapter = new SimpleAdapter(this,
                GetData(),
                R.layout.grid_menu_text,
                new String[]{"chapter"},
                new int[]{R.id.grid_menu_text_text});
        mChapter.setAdapter(adapter);
        mChapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        CommonPara.currentChapter = arg2 + 1;
        CommonPara.currentSection = 0;
        CommonPara.bibleDevitionPos = 0;
        CommonPara.bibleMp3Pos = 0;

        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Map<String, String>> GetData() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        int max = VerseInfo.CHAPTER_COUNT[CommonPara.currentBook];
        for (int i = 1; i <= max; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("chapter", String.valueOf(i));
            data.add(map);
        }
        return data;
    }
}

