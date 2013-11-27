package org.cathassist.bible;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import org.cathassist.bible.lib.CommonFunc;
import org.cathassist.bible.lib.CommonPara;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class SettingActivity extends SherlockPreferenceActivity implements OnPreferenceChangeListener {
    private PreferenceScreen click_about;
    private PreferenceScreen click_share;
    private PreferenceScreen click_feedback;
    private ListPreference list_font_size;
    private CheckBoxPreference check_always_bright;
    private CheckBoxPreference check_theme_black;
    private CheckBoxPreference check_auto_update;
    private CheckBoxPreference check_show_color;
    private CheckBoxPreference check_allow_gprs;
    private Intent intent = null;
    private boolean theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(CommonPara.THEME);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");

        intent = new Intent(this, MainActivity.class);
        setResult(CommonPara.NOT_NEED_RESTART, intent);

        click_about = (PreferenceScreen) findPreference("click_about");
        click_share = (PreferenceScreen) findPreference("click_share");
        click_share.setOnPreferenceChangeListener(this);
        click_feedback = (PreferenceScreen) findPreference("click_feedback");
        click_feedback.setOnPreferenceChangeListener(this);
        list_font_size = (ListPreference) findPreference("font_size");
        list_font_size.setOnPreferenceChangeListener(this);
        check_always_bright = (CheckBoxPreference) findPreference("always_bright");
        check_always_bright.setOnPreferenceChangeListener(this);
        check_theme_black = (CheckBoxPreference) findPreference("theme_black");
        check_theme_black.setOnPreferenceChangeListener(this);
        check_auto_update = (CheckBoxPreference) findPreference("auto_update");
        check_auto_update.setOnPreferenceChangeListener(this);
        check_show_color = (CheckBoxPreference) findPreference("show_color");
        check_show_color.setOnPreferenceChangeListener(this);
        check_allow_gprs = (CheckBoxPreference) findPreference("allow_gprs");
        check_allow_gprs.setOnPreferenceChangeListener(this);

        theme = CommonPara.theme_black;

        click_about.setSummary("软件版本" + "  " + CommonFunc.GetVerName(this));
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

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == click_feedback) {
            FeedbackAgent agent = new FeedbackAgent(this);
            agent.startFeedbackActivity();
        } else if (preference == click_share) {
            final UMSocialService mController = UMServiceFactory.getUMSocialService("cathbible",RequestType.SOCIAL);
            mController.openUserCenter(this, SocializeConstants.FLAG_USER_CENTER_HIDE_LOGININFO);
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        SharedPreferences settings = getSharedPreferences(CommonPara.STORE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if (preference == list_font_size) {
            editor.putFloat("font_size", Float.valueOf(objValue.toString()));
        } else if (preference == check_always_bright) {
            editor.putBoolean("always_bright", (Boolean) objValue);
        } else if (preference == check_theme_black) {
            editor.putBoolean("theme_black", (Boolean) objValue);
        } else if (preference == check_auto_update) {
            editor.putBoolean("auto_update", (Boolean) objValue);
        } else if (preference == check_show_color) {
            editor.putBoolean("show_color", (Boolean) objValue);
        } else if (preference == check_allow_gprs) {
            editor.putBoolean("allow_gprs", (Boolean) objValue);
        }

        editor.commit();

        CommonFunc.InitCommonPara(this);

        if (theme != CommonPara.theme_black) {
            setResult(CommonPara.NEED_RESTART, intent);
        } else {
            setResult(CommonPara.NOT_NEED_RESTART, intent);
        }
        return true;
    }
}
