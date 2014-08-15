package uk.me.lewisdeane.urbandictionary;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActionBarFragment extends Fragment {

    static CustomTextView mHeader;
    static ImageButton mMenu, mBack, mSearchIcon, mMic, mRefresh;
    static CustomEditText mSearchBox;
    static LinearLayout mSearchOn, mSearchOff, mContainer;
    private View v;

    public static boolean isSearching = false;
    public static final int REQUEST_CODE = 1234;

    private int mDurationTime = 500;
    private boolean continueToRun = true;

    Handler mHandler = new Handler();

    public ActionBarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_action_bar, container, false);
        init();
        checkIfSpeechAvailable();
        setListeners();
        return v;
    }

    public void init() {
        mHeader = (CustomTextView) v.findViewById(R.id.action_bar_header);

        mSearchBox = (CustomEditText) v.findViewById(R.id.action_bar_search_box);

        mMenu = (ImageButton) v.findViewById(R.id.action_bar_toggle);
        mBack = (ImageButton) v.findViewById(R.id.action_bar_back);
        mSearchIcon = (ImageButton) v.findViewById(R.id.action_bar_search_icon);
        mMic = (ImageButton) v.findViewById(R.id.action_bar_mic);
        mRefresh = (ImageButton) v.findViewById(R.id.action_bar_refresh);

        mSearchOn = (LinearLayout) v.findViewById(R.id.action_bar_search_on);
        mSearchOff = (LinearLayout) v.findViewById(R.id.action_bar_search_off);
        mContainer = (LinearLayout) v.findViewById(R.id.action_bar_container);
    }

    public void checkIfSpeechAvailable() {
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
            mMic.setVisibility(View.GONE);
    }

    public void setListeners() {
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NavigationDrawerFragment.mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    NavigationDrawerFragment.mDrawerLayout.closeDrawer(Gravity.LEFT);
                else
                    NavigationDrawerFragment.mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchOff.setVisibility(View.GONE);
                mSearchOn.setVisibility(View.VISIBLE);
                isSearching = true;
                mSearchBox.requestFocus();
                NavigationDrawerFragment.mDrawerLayout.closeDrawer(Gravity.LEFT);
                InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(mContainer.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchOff.setVisibility(View.VISIBLE);
                mSearchOn.setVisibility(View.GONE);
                mSearchBox.setText("");
                isSearching = false;
                InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mContainer.getWindowToken(), 0);
            }
        });

        mSearchBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    MainFragment.suggestions.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment.loadSearchResults("");
                continueToRun = true;
                mHandler.postDelayed(mRunnable, mDurationTime);
            }
        });

        mMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if (!ConnectionHelper.hasFinished) {
                RotateAnimation ra = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(500);
                mRefresh.startAnimation(ra);
            } else
                continueToRun = false;

            if (continueToRun == true)
                mHandler.postDelayed(mRunnable, mDurationTime);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results.size() != 0) {
                mSearchBox.setText(results.get(0));
                mSearchBox.setSelection(results.get(0).length());
            }
            else
                Toast.makeText(getActivity(), getActivity().getString(R.string.speech_no_results), Toast.LENGTH_SHORT);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

