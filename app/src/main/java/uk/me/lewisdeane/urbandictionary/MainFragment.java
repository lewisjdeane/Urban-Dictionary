package uk.me.lewisdeane.urbandictionary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.me.lewisdeane.ldialogs.CustomDialog;

/**
 * Created by Lewis on 18/07/2014.
 */
public class MainFragment extends Fragment {

    View v;
    static ListView suggestions, results;
    ArrayList<String> suggestionItems = new ArrayList<String>();
    static ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
    SuggestionAdapter suggestionAdapter;
    static CustomAdapter customAdapter;
    private static Context mContext;
    public static CustomTextView mNoResults;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        setListeners();
        loadSearchResults("");
        return v;
    }

    public void init() {
        mContext = getActivity();

        suggestions = (ListView) v.findViewById(R.id.suggestions_list);
        results = (ListView) v.findViewById(R.id.results_list);

        mNoResults = (CustomTextView) v.findViewById(R.id.no_favourites);

        suggestionAdapter = new SuggestionAdapter(getActivity(), R.layout.suggestion_item, suggestionItems);
        customAdapter = new CustomAdapter(getActivity(), R.layout.search_item, searchItems);

        suggestions.setAdapter(suggestionAdapter);
        results.setAdapter(customAdapter);
    }

    private void setListeners() {
        suggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActionBarFragment.mSearchBox.setText(suggestionItems.get(i).toString());
                ActionBarFragment.mSearchBox.setSelection(ActionBarFragment.mSearchBox.getText().length());
            }
        });

        results.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                suggestions.setVisibility(View.GONE);
                return false;
            }
        });
    }

    public void updateSuggestions(String _search) {
        suggestionItems.clear();

        if (_search.length() > 0) {
            for (String result : new DatabaseHelper(getActivity()).readSuggestions(_search))
                suggestionItems.add(result);
        }
        suggestionAdapter.notifyDataSetChanged();
    }

    public static void loadSearchResults(String _search) {
        searchItems.clear();
        new ConnectionHelper(mContext).getSearchResults(_search);
    }
}