package org.cathassist.bible;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import org.cathassist.bible.lib.CommonPara;
import org.cathassist.bible.lib.Database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkContentActivity extends SherlockActivity implements OnClickListener {
    EditText mTitle;
    EditText mContent;
    Button mComfirm;
    Button mDelete;
    Button mCancel;
    int mBook;
    int mChapter;
    int mSection;
    boolean mIsExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(CommonPara.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_content);

        Intent intent = getIntent();
        mBook = intent.getIntExtra("book", 1);
        mChapter = intent.getIntExtra("chapter", 1);
        mSection = intent.getIntExtra("section", 1);

        mTitle = (EditText) findViewById(R.id.text_title);
        mContent = (EditText) findViewById(R.id.text_content);
        mComfirm = (Button) findViewById(R.id.button_confirm);
        mDelete = (Button) findViewById(R.id.button_delete);
        mCancel = (Button) findViewById(R.id.button_cancel);

        mComfirm.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        GetContent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_confirm:
                SaveBookmark();
                break;
            case R.id.button_delete:
                DeleteBookmark();
                break;
            case R.id.button_cancel:
                CancelBookmark();
                break;
        }

    }

    private void SaveBookmark() {
        if ((mTitle.getText() != null && TextUtils.isEmpty(mTitle.getText().toString().trim()))) {
            mTitle.setText("无标题");
        }

        SQLiteDatabase db = null;
        try {
            db = new Database(this).DbConnection(CommonPara.DB_DATA_PATH + CommonPara.DB_DATA_NAME);
            String sql;

            String updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            if (mIsExist) {
                sql = "update bookmark set updateTime = '" + updateTime + "', title = '" + mTitle.getText() + "', content = '" + mContent.getText() + "'"
                        + " where book = " + mBook + " and chapter = " + mChapter + " and section = " + mSection;
                db.execSQL(sql);
            } else {
                sql = "insert into bookmark (updateTime,book,chapter,section,title,content) values ("
                        + "'" + updateTime + "'," + mBook + "," + mChapter + "," + mSection + ","
                        + "'" + mTitle.getText() + "'," + "'" + mContent.getText() + "')";
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        this.finish();
    }

    private void DeleteBookmark() {
        if (mIsExist) {
            SQLiteDatabase db = null;
            try {
                db = new Database(this).DbConnection(CommonPara.DB_DATA_PATH + CommonPara.DB_DATA_NAME);
                String sql = "delete from bookmark where book = " + mBook + " and chapter = " + mChapter + " and section = " + mSection;
                db.execSQL(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.close();
                }
            }
        }
        this.finish();
    }

    private void CancelBookmark() {
        this.finish();
    }

    private void GetContent() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = new Database(this).DbConnection(CommonPara.DB_DATA_PATH + CommonPara.DB_DATA_NAME);

            String sql = "select * from bookmark where book = " + mBook + " and chapter = " + mChapter + " and section = " + mSection;
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                mIsExist = true;
                mTitle.setText(cursor.getString(cursor.getColumnIndex("title")));
                mContent.setText(cursor.getString(cursor.getColumnIndex("content")));
            } else {
                mIsExist = false;
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

    }

}
