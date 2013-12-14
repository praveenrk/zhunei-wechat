package org.cathassist.bible.lib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.mozillaonline.providers.DownloadManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cathassist.bible.R;

import java.io.File;

public class Func {
    private static DownloadManager mDownloadManager;

    public static void setDownloadManager(DownloadManager manager) {
        mDownloadManager = manager;
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static void InitCommonPara(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Para.font_size = settings.getFloat("font_size", 18);
        editor.putFloat("font_size", Para.font_size);
        Para.always_bright = settings.getBoolean("always_bright", true);
        editor.putBoolean("always_bright", Para.always_bright);
        Para.theme_black = settings.getBoolean("theme_black", false);
        editor.putBoolean("theme_black", Para.theme_black);
        Para.auto_update = settings.getBoolean("auto_update", true);
        editor.putBoolean("auto_update", Para.auto_update);
        Para.show_color = settings.getBoolean("show_color", true);
        editor.putBoolean("show_color", Para.show_color);
        Para.allow_gprs = settings.getBoolean("allow_gprs", false);
        editor.putBoolean("allow_gprs", Para.allow_gprs);
        Para.mp3_ver = settings.getInt("mp3_ver", 1);
        editor.putInt("mp3_ver", Para.mp3_ver);
        editor.commit();
    }

    public static void LoadTheme() {
        Para.THEME = Para.theme_black ? R.style.DarkTheme : R.style.LightTheme;
        Para.BACKGROUND = Para.theme_black ? R.drawable.dark_bg : R.drawable.default_bg;
    }

    public static void InitTheme(Context context) {
        Para.DEFAULT_TEXT_COLOR
                = context.getTheme().obtainStyledAttributes(Para.THEME, new int[]{android.R.attr.textColorSecondary})
                .getColor(0, Para.theme_black ? Color.WHITE : Color.BLACK);
        Para.HIGHLIGHT_TEXT_COLOR = Para.theme_black ? Color.YELLOW : Color.BLUE;
    }

    public static void InitOncePara(Context context) {
        Para.DB_CONTENT_PATH = context.getDatabasePath(Para.DB_CONTENT_NAME).getParent() + "/";
        Para.DB_DATA_PATH = context.getDatabasePath(Para.DB_DATA_NAME).getParent() + "/";
        Para.BIBLE_MP3_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cathbible/bible/mp3/";

        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        Para.currentBook = settings.getInt("currentBook", 1);
        Para.currentChapter = settings.getInt("currentChapter", 1);
        Para.currentSection = settings.getInt("currentSection", 0);
        Para.mp3Mode = settings.getInt("mp3Mode", 0);
    }

    public static void getMp3RootUrl() {
        String httpUrl = "http://cathassist.org/bible/getbiblemp3root.php";
        HttpGet httpRequest = new HttpGet(httpUrl);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Para.BIBLE_MP3_URL = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {

        }
    }

    public static String GetVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {

        }
        return verName;
    }

    public static File getFilePath(String name) {
        File file;
        file = new File(Para.BIBLE_MP3_PATH + name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static String getUrlPath(String name) {
        String url = Para.BIBLE_MP3_URL + name;

        return url;
    }

    public static String getFileName(String version, int book, int chapter) {
        String name = version + "/"
                + String.format("%03d", book) + "_"
                + String.format("%03d", chapter) + ".mp3";
        return name;
    }

    public static String getUrlName(String version, int book, int chapter) {
        String name = String.format("%03d", book) + "/"
                + String.format("%03d", chapter) + ".mp3";
        return name;
    }

    public static void ChangeChapter(boolean isNext) {
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
        Para.currentCount = VerseInfo.CHAPTER_COUNT[Para.currentBook];
        if (Para.currentBook != 1) {
            Para.previousCount = VerseInfo.CHAPTER_COUNT[Para.currentBook - 1];
        }
        Para.currentSection = 0;
    }

    public static void downChapter(Context context, String version, int book, int chapter) {
        final File file = Func.getFilePath(Func.getFileName(version, book, chapter));
        if (!file.exists()) {
            new File(file.getParent()).mkdirs();
            if (Func.isWifi(context) || Para.allow_gprs) {
                download(context, version, book, chapter);
                Toast.makeText(context, "下载已添加", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "下载失败\n请在WIFI环境下再下载\n或在设置中打开使用数据流量选项", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void downBook(Context context, String version, int book) {
        if (Func.isWifi(context) || Para.allow_gprs) {
            for (int i = 1; i <= VerseInfo.CHAPTER_COUNT[book]; i++) {
                download(context, version, book, i);
            }
            Toast.makeText(context, "下载已添加", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "下载失败\n请在WIFI环境下再下载\n或在设置中打开使用数据流量选项", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long download(Context context, String version, int book, int chapter) {
        long reference = -1;
        final File file = Func.getFilePath(Func.getFileName(version, book, chapter));
        if (!file.exists()) {
            new File(file.getParent()).mkdirs();
            String url = Func.getUrlPath(Func.getUrlName(version, book, chapter));
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationUri(Uri.fromFile(file));
            request.setTitle(VerseInfo.CHN_NAME[book]+"第"+chapter+"章");
            request.setDescription("下载中");

            try {
                reference = mDownloadManager.enqueue(request);
            } catch (Exception e) {
                Toast.makeText(context, "无法下载，请稍候重试（可能是同时进行的下载任务太多了）", Toast.LENGTH_SHORT).show();
            }
        }
        return reference;
    }

}