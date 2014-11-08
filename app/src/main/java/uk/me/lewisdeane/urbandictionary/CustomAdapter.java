package uk.me.lewisdeane.urbandictionary;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends ArrayAdapter<SearchItem> {

    ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
    Context mContext;
    MainActivity ma;
    String title = "", meaning = "", example = "", up = "", down = "";

    public CustomAdapter(Context context, int resource,
                         ArrayList<SearchItem> _searchItems) {

        super(context, resource, _searchItems);
        this.mContext = context;
        this.searchItems = _searchItems;
        ma = (MainActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.search_item, null);
        }

        CustomTextView searchTitle = (CustomTextView) v.findViewById(R.id.search_item_title);
        CustomTextViewJustified searchMeaning = (CustomTextViewJustified) v.findViewById(R.id.search_item_meaning);
        CustomTextViewJustified searchExample = (CustomTextViewJustified) v.findViewById(R.id.search_item_example);
        CustomTextView searchUp = (CustomTextView) v.findViewById(R.id.search_item_up_rating);
        CustomTextView searchDown = (CustomTextView) v.findViewById(R.id.search_item_down_rating);
        TextView searchDivider = (TextView) v.findViewById(R.id.search_item_divider);

        LinearLayout searchExpanded = (LinearLayout) v.findViewById(R.id.search_item_linear_layout_expanded);
        LinearLayout searchContainer = (LinearLayout) v.findViewById(R.id.search_item_container);

        final ImageButton searchFavourite = (ImageButton) v.findViewById(R.id.search_item_favourite);

        try {
            title = searchItems.get(position).getTitle();
            meaning = searchItems.get(position).getMeaning();
            example = searchItems.get(position).getExample();
            up = searchItems.get(position).getUp();
            down = searchItems.get(position).getDown();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Error loading, trying again...", Toast.LENGTH_SHORT).show();
            MainFragment.searchItems.clear();
            new ConnectionHelper(mContext).getSearchResults(ActionBarFragment.mSearchBox.getText().toString());
        }

        searchTitle.setText(title);
        searchMeaning.setText(meaning, true);
        searchExample.setText(example, true);
        searchUp.setText(up);
        searchDown.setText(down);

        searchTitle.setTextColor(MainActivity.mThemes.getPrimaryColour());
        searchUp.setTextColor(MainActivity.mThemes.getPrimaryColour());
        searchDown.setTextColor(MainActivity.mThemes.getPrimaryColour());

        searchDivider.setBackgroundColor(MainActivity.mThemes.getPrimaryColour());

        searchExpanded.setVisibility(searchItems.get(position).getIsExpanded() ? View.VISIBLE : View.GONE);

        searchFavourite.setBackgroundDrawable(searchItems.get(position).getIsFavourite() ? MainActivity.mThemes.getFavouriteIcon(true) : getContext().getResources().getDrawable(R.drawable.ic_action_favorite_translucent));

        searchFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchItem si = searchItems.get(position);
                si.setIsFavourite(!si.getIsFavourite());

                if (MainActivity.BASE_URL == null && ActionBarFragment.mSearchBox.getText().toString().trim().length() == 0)
                    searchItems.remove(position);
                else
                    searchItems.set(position, si);

                notifyDataSetChanged();
            }
        });

        searchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchItem si = searchItems.get(position);
                si.setIsExpanded(!si.getIsExpanded());
                searchItems.set(position, si);
                notifyDataSetChanged();
            }
        });

        searchContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SearchItem si = searchItems.get(position);
                MainActivity.share(si);
                return false;
            }
        });
        return v;
    }

}
