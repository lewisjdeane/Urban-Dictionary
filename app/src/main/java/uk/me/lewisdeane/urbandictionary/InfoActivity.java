package uk.me.lewisdeane.urbandictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import uk.me.lewisdeane.urbandictionary.R;

public class InfoActivity extends Activity {

    private PreferenceActionBarFragment mPreferenceActionBarFragment;
    private Themes mThemes;
    public static CustomTextViewJustified mText;
    public static ScrollView mScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getActionBar().hide();
        mText = (CustomTextViewJustified) findViewById(R.id.info_text);
        mScroll = (ScrollView) findViewById(R.id.info_scroll);
        mText.setText(mText.getText().toString(), true);

        mPreferenceActionBarFragment = (PreferenceActionBarFragment) getFragmentManager().findFragmentById(R.id.info_action_bar);
        mPreferenceActionBarFragment.mHeader.setText(getString(R.string.info));

        mThemes = new Themes(this);
        mThemes.applyInfoTheme();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
