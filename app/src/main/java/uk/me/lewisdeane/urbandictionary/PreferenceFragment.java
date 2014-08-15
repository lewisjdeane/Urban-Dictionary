package uk.me.lewisdeane.urbandictionary;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PreferenceFragment extends Fragment {

    View v;
    ListView mListView;
    static LinearLayout mBackground;
    private ArrayList<String[]> mSettingsLists;
    static String[][] mPreferences = new String[3][2];
    public static SettingsAdapter mSettingsAdapter;
    public static ArrayList<SettingsItem> mSettingItems;
    private SharedPreferences mSharedPreferences;

    public PreferenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_preference, container, false);
        init();
        setListeners();
        return v;
    }

    public void init() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mListView = (ListView) v.findViewById(R.id.preference_list);

        mBackground = (LinearLayout) v.findViewById(R.id.preferences_container);

        mSettingItems = new ArrayList<SettingsItem>();
        mSettingItems.add(new SettingsItem(getActivity(), getResources().getStringArray(R.array.settings_array)[0], mSharedPreferences.getString("PREF_COLOUR", getString(R.string.theme1))));
        mSettingItems.add(new SettingsItem(getActivity(), getResources().getStringArray(R.array.settings_array)[1], mSharedPreferences.getString("PREF_DEFAULT", getString(R.string.heading_section1))));
        mSettingItems.add(new SettingsItem(getActivity(), getString(R.string.rate_header), getString(R.string.rate_content)));
        mSettingItems.add(new SettingsItem(getActivity(), getString(R.string.more_apps_header), getString(R.string.more_apps_content)));

        mSettingsAdapter = new SettingsAdapter(getActivity(), R.layout.setting_item, mSettingItems);
        mListView.setAdapter(mSettingsAdapter);

        mSettingsLists = new ArrayList<String[]>();
        mSettingsLists.add(getActivity().getResources().getStringArray(R.array.colours_array));
        mSettingsLists.add(getActivity().getResources().getStringArray(R.array.defaults_array));

        mPreferences[0] = new String[]{"PREF_COLOUR", getActivity().getResources().getString(R.string.theme1)};
        mPreferences[1] = new String[]{"PREF_DEFAULT", getActivity().getResources().getString(R.string.heading_section1)};
    }

    public void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int j, long l) {
                if (j < 2) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setItems(mSettingsLists.get(j), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(mPreferences[j][0], mSettingsLists.get(j)[i]).commit();
                            MainActivity.mThemes.applyBaseTheme();
                            MainActivity.mThemes.applyPreferenceTheme();
                            SettingsItem si = mSettingItems.get(j);
                            si.setSubtitle(sharedPreferences.getString(mPreferences[j][0], mPreferences[j][1]));

                            mSettingItems.set(j, si);
                            mSettingsAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDialog.show();
                } else {
                    String url = "https://play.google.com/store/search?q=pub%3ASimplex%20Devs";
                    if (j == 2)
                        url = "https://play.google.com/store/apps/details?id=uk.me.lewisdeane.urbandictionary";
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }
            }
        });
    }
}

