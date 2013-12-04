package org.cathassist.bible.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import org.cathassist.bible.R;
import org.cathassist.bible.lib.CommonPara;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mp3ManageActivity  extends SherlockActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private ArrayAdapter<Map<String,String>> mAdapter;
    ArrayList<Map<String,String>> mMp3List = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(CommonPara.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp3_manage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("圣经音频管理");

        mListView = (ListView) findViewById(R.id.list);

        Map<String,String> map = new HashMap<String, String>();
        map.put("name","思高中文版");
        map.put("path","chn");
        mMp3List.add(map);
        mAdapter = new Mp3ManageAdapter(this,mMp3List);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
            Toast.makeText(this,"您的操作系统版本过低，不能使用此功能",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this,Mp3ManageDetailActivity.class);
        intent.putExtra("name",mMp3List.get(position).get("name"));
        intent.putExtra("path",mMp3List.get(position).get("path"));
        startActivity(intent);
    }

    public static class Mp3ManageAdapter extends ArrayAdapter<Map<String,String>> {

        private Context mContext;

        public Mp3ManageAdapter(Context context, List<Map<String, String>> tags) {
            super(context, 0, tags);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null || convertView.getTag() == null) {
                vh = new ViewHolder();
                convertView  = LayoutInflater.from(mContext).inflate(R.layout.mp3_manage_item, null);
                vh.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.text.setText(getItem(position).get("name"));

            return convertView;
        }

        private class ViewHolder {
            TextView text;
        }
    }
}
