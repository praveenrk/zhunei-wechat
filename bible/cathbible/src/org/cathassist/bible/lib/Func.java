package org.cathassist.bible.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.cathassist.bible.R;

import java.io.File;
import java.util.Calendar;

public class Func {
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return  (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static void InitCommonPara(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Calendar calendar = Calendar.getInstance();

        Para.font_size = settings.getFloat("font_size", 18);
        editor.putFloat("font_size", Para.font_size);
        Para.always_bright = settings.getBoolean("always_bright", false);
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

}
