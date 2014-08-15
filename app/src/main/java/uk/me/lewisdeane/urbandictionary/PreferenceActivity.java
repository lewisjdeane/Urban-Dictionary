package uk.me.lewisdeane.urbandictionary;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import uk.me.lewisdeane.urbandictionary.R;

public class PreferenceActivity extends Activity {

    PreferenceActionBarFragment preferenceActionBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        getActionBar().hide();

        preferenceActionBarFragment = (PreferenceActionBarFragment) getFragmentManager().findFragmentById(R.id.preferences_action_bar);

        Themes themes = new Themes(this);
        themes.applyPreferenceTheme();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
