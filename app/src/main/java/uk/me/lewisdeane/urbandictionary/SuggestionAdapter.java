package uk.me.lewisdeane.urbandictionary;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionAdapter extends ArrayAdapter<String> {

    ArrayList<String> suggestions = new ArrayList<String>();
    Context mContext;

    public SuggestionAdapter(Context context, int resource,
                              ArrayList<String> _suggestions) {

        super(context, resource, _suggestions);
        this.mContext = context;
        this.suggestions = _suggestions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.suggestion_item, null);
        }

        CustomTextView title = (CustomTextView) v.findViewById(R.id.suggestion_item_title);
        LinearLayout container = (LinearLayout) v.findViewById(R.id.suggestion_item_container);

        if (suggestions.get(position) != null) {
            title.setText(suggestions.get(position));
            container.setBackgroundColor(MainActivity.mThemes.getPrimaryColour());
        }

        return v;
    }

    public void toast(String _text){
        Toast.makeText(mContext, _text, Toast.LENGTH_SHORT).show();
    }
}
