package org.cathassist.bible;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.VerseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSelectActivity extends SherlockActivity implements OnItemClickListener {
    ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(CommonPara.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_book_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = (ListView) findViewById(R.id.list_act_book_select_book);
        mList.setDivider(null);

        SimpleAdapter adapter = new SimpleAdapter(this,
                GetData(),
                R.layout.list_menu_text,
                new String[]{"book"},
                new int[]{R.id.list_menu_text_text});
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        CommonPara.currentBook = arg2 + 1;
        CommonPara.currentChapter = 1;
        CommonPara.currentSection = 0;
        CommonPara.bibleDevitionPos = 0;
        CommonPara.bibleMp3Pos = 0;

        Intent intent = new Intent();

        intent.setClass(this, ChapterSelectActivity.class);
        startActivity(intent);
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
        for (int i = 1; i <= 73; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("book", VerseInfo.CHN_NAME[i] + "  "
                    + VerseInfo.ENG_NAME[i]);
            data.add(map);
        }
        return data;
    }
}
