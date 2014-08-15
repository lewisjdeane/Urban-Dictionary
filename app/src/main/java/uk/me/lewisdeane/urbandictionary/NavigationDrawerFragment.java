package uk.me.lewisdeane.urbandictionary;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private NavigationDrawerCallbacks mCallbacks;
    public ActionBarDrawerToggle mDrawerToggle;

    public static DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    public static int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    public static boolean isOpen = false;

    public DrawerAdapter mDrawerAdapter;
    public ArrayList<DrawerItem> mDrawerItems = new ArrayList<DrawerItem>();
    public static LinearLayout mContainer;
    private SharedPreferences mSharedPreferences;

    View v;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (!mUserLearnedDrawer) {
            isOpen = true;
        }

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        init();
        loadDrawerItems();
        return v;
    }

    public void init() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDrawerListView = (ListView) v.findViewById(R.id.navigation_drawer_list);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerAdapter = new DrawerAdapter(getActivity(), R.layout.drawer_item, mDrawerItems);

        mContainer = (LinearLayout) v.findViewById(R.id.navigation_drawer_container);

        mDrawerListView.setAdapter(mDrawerAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
    }

    public void loadDrawerItems() {
        mDrawerItems.add(new DrawerItem(R.drawable.ic_action_news_white, getString(R.string.heading_section1), getIsSelected(getString(R.string.heading_section1)), false));
        mDrawerItems.add(new DrawerItem(R.drawable.ic_action_shuffle_white, getString(R.string.heading_section2), getIsSelected(getString(R.string.heading_section2)), false));
        mDrawerItems.add(new DrawerItem(R.drawable.ic_action_favorite_white, getString(R.string.heading_section3), getIsSelected(getString(R.string.heading_section3)), false));
        mDrawerItems.add(new DrawerItem(R.drawable.ic_action_settings_grey, getString(R.string.heading_section4), false, true));
        mDrawerItems.add(new DrawerItem(R.drawable.ic_action_info_outline_grey, getString(R.string.heading_section5), false, true));
    }

    private boolean getIsSelected(String _text){
        return _text.equals(mSharedPreferences.getString("PREF_DEFAULT", getActivity().getResources().getStringArray(R.array.defaults_array)[0])) ? true : false;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isOpen = false;
                MainFragment.results.setEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                isOpen = true;
                MainFragment.results.setEnabled(false);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                rotateIcon(slideOffset);
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static void rotateIcon(float _offset) {
        ActionBarFragment.mMenu.setRotation(-90 * _offset);
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;

        for (DrawerItem di : mDrawerItems) {
            di.setIsSelected(false);
        }

        mDrawerItems.get(position).setIsSelected(true);

        if(MainActivity.NETWORK_AVAILABLE) {
            if (mCallbacks != null && position < 3)
                mCallbacks.onNavigationDrawerItemSelected(position, getHeaderFromPosition(getActivity(), position), true);
            MainActivity.mActionBarFragment.mRefresh.setVisibility(View.GONE);
            if (position == 1)
                MainActivity.mActionBarFragment.mRefresh.setVisibility(View.VISIBLE);
            else if (position > 2) {
                startActivity(new Intent(getActivity(), position == 3 ? PreferenceActivity.class : InfoActivity.class));
                getActivity().finish();
            }
        } else{
            MainActivity.mActionBarFragment.mRefresh.setVisibility(View.GONE);
            if(position > 0){
                startActivity(new Intent(getActivity(), position == 1 ? PreferenceActivity.class : InfoActivity.class));
                getActivity().finish();
            }
        }

        mDrawerAdapter.notifyDataSetChanged();
    }

    public static String getHeaderFromPosition(Context _context, int _position) {
        switch (_position) {
            case 0:
                return _context.getString(R.string.heading_section1);
            case 1:
                return _context.getString(R.string.heading_section2);
            case 2:
                return _context.getString(R.string.heading_section3);
            default:
                return _context.getString(R.string.heading_section1);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position, String header, boolean shouldClose);
    }
}
