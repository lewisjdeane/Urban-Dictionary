package uk.me.lewisdeane.urbandictionary;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    ArrayList<DrawerItem> drawerItems = new ArrayList<DrawerItem>();
    Context mContext;
    ArrayList<Typeface> mTypefaces = new ArrayList<Typeface>();
    NavigationDrawerFragment mNavigationDrawerFragment;

    public DrawerAdapter(Context context, int resource,
                         ArrayList<DrawerItem> _drawerItems) {

        super(context, resource, _drawerItems);
        this.mContext = context;
        this.drawerItems = _drawerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            if ((MainActivity.NETWORK_AVAILABLE && position < 3) || (!MainActivity.NETWORK_AVAILABLE && position == 0))
                v = vi.inflate(R.layout.drawer_item, null);
            else
                v = vi.inflate(R.layout.drawer_item_small, null);
        }

        DrawerItem di = drawerItems.get(position);

        if (!di.getIsSmall()) {
            CustomTextView titleView = (CustomTextView) v.findViewById(R.id.drawer_item_title);
            TextView divider = (TextView) v.findViewById(R.id.drawer_item_bottom_divider);

            if (di.getIsSelected())
                titleView.setTypeface(Typeface.createFromAsset(getContext().getResources().getAssets(), "Roboto-Medium.ttf"));
            else
                titleView.setTypeface(Typeface.createFromAsset(getContext().getResources().getAssets(), "Roboto-Light.ttf"));

            titleView.setText(di.getTitle());

            if ((MainActivity.NETWORK_AVAILABLE && position == 2) || (MainActivity.NETWORK_AVAILABLE && position == 0)) {
                //divider.setBackgroundColor(Color.parseColor("#474747"));
                //Toast.makeText(getContext(), "yep" , Toast.LENGTH_SHORT).show();
            }
        } else {
            CustomTextView titleView = (CustomTextView) v.findViewById(R.id.drawer_item_small_title);
            ImageView imageView = (ImageView) v.findViewById(R.id.drawer_item_small_image);
            TextView topDivider = (TextView) v.findViewById(R.id.drawer_item_small_top_divider);

            if(position == 2 || position == 4)
                topDivider.setBackgroundColor(Color.parseColor("#F7F7F7"));

            titleView.setText(di.getTitle());
            imageView.setBackgroundDrawable(getContext().getResources().getDrawable(di.getRes()));
        }

        return v;
    }
}
