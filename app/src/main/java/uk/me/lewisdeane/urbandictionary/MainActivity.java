package uk.me.lewisdeane.urbandictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, CustomEditText.TextChangedCallback {

    public static NavigationDrawerFragment mNavigationDrawerFragment;
    public static ActionBarFragment mActionBarFragment;
    public static MainFragment mMainFragment;
    public static LinearLayout mContainer;

    public static Themes mThemes;

    public static String BASE_URL = "http://www.urbandictionary.com/define.php?term=";
    public static boolean NETWORK_AVAILABLE = true;
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        checkForInternet();
        checkForDatabase();
        setRefreshVisibility();
    }

    public void init() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mActionBarFragment = (ActionBarFragment) getFragmentManager().findFragmentById(R.id.action_bar);
        mMainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.results);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mContainer = (LinearLayout) findViewById(R.id.main_container);

        onNavigationDrawerItemSelected(getPosition(mSharedPreferences.getString("PREF_DEFAULT", getString(R.string.heading_section1))), "", false);

        getActionBar().hide();

        mThemes = new Themes(getApplicationContext());
        mThemes.applyBaseTheme();
    }

    public int getPosition(String _mode) {
        if (_mode.equals(getString(R.string.heading_section3)))
            return 2;
        else
            return _mode.equals(getString(R.string.heading_section1)) ? 0 : 1;
    }

    @Override
    public void onNavigationDrawerItemSelected(int _position, String _header, boolean _shouldClose) {
        if (!_header.equals(mActionBarFragment.mHeader.getText().toString())) {
            setBaseUrl(_position);
            mActionBarFragment.mHeader.setText(_header.equals("") ? mSharedPreferences.getString("PREF_DEFAULT", getResources().getStringArray(R.array.defaults_array)[0]) : _header);
            ActionBarFragment.mSearchBox.setText("");
            ActionBarFragment.mSearchOn.setVisibility(View.GONE);
            ActionBarFragment.mSearchOff.setVisibility(View.VISIBLE);
            mNavigationDrawerFragment.mDrawerAdapter.notifyDataSetChanged();
        }
        if (_shouldClose)
            mNavigationDrawerFragment.mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void setBaseUrl(int _position) {
        switch (_position) {
            case 0:
                BASE_URL = "http://www.urbandictionary.com/define.php?term=";
                break;
            case 1:
                BASE_URL = "http://www.urbandictionary.com/random.php";
                break;
            case 2:
                BASE_URL = null;
                break;
            default:
                BASE_URL = "http://www.urbandictionary.com/define.php?term=";
                break;
        }
    }

    public void onTextChange(String _text) {
        _text = _text.trim();

        mMainFragment.updateSuggestions(_text);

        if(NETWORK_AVAILABLE)
            mMainFragment.loadSearchResults(_text);

        mMainFragment.suggestions.setVisibility(View.VISIBLE);

        if (NavigationDrawerFragment.mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            NavigationDrawerFragment.mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void setRefreshVisibility() {
        mActionBarFragment.mRefresh.setVisibility(View.GONE);
        if(NETWORK_AVAILABLE && mSharedPreferences.getString("PREF_DEFAULT", getString(R.string.heading_section1)).equals(getString(R.string.heading_section2)))
            mActionBarFragment.mRefresh.setVisibility(View.VISIBLE);
    }

    public void checkForInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (!(netInfo != null && netInfo.isConnectedOrConnecting())) {
            NETWORK_AVAILABLE = false;
            BASE_URL = null;
            mActionBarFragment.mSearchIcon.setVisibility(View.GONE);
            mNavigationDrawerFragment.mDrawerItems.remove(0);
            mNavigationDrawerFragment.mDrawerItems.remove(0);
            mNavigationDrawerFragment.mDrawerItems.get(0).setIsSelected(true);
            mNavigationDrawerFragment.mDrawerAdapter.notifyDataSetChanged();
            mMainFragment.loadSearchResults("");
            mActionBarFragment.mHeader.setText(getString(R.string.heading_section3));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.no_network_heading));
            builder.setMessage(getString(R.string.no_network_message));
            builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    public void checkForDatabase() {
        if(NETWORK_AVAILABLE)
            new DatabaseHelper(this).checkForDatabase();
    }
}
