package uk.me.lewisdeane.urbandictionary;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsAdapter extends ArrayAdapter<SettingsItem> {

    ArrayList<SettingsItem> mItems = new ArrayList<SettingsItem>();
    Context mContext;
    ArrayList<Typeface> mTypefaces = new ArrayList<Typeface>();

    public SettingsAdapter(Context context, int resource,
                         ArrayList<SettingsItem> _items) {

        super(context, resource, _items);
        this.mContext = context;
        this.mItems = _items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.setting_item, null);
        }

        CustomTextView titleView = (CustomTextView) v.findViewById(R.id.setting_item_title);
        CustomTextView subtitleView = (CustomTextView) v.findViewById(R.id.setting_item_subtitle);

        if(mItems != null){
            titleView.setText(mItems.get(position).getTitle());
            titleView.setTextColor(MainActivity.mThemes.getPrimaryColour());

            subtitleView.setText(mItems.get(position).getSubtitle());
        }

        return v;
    }
}
