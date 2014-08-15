package uk.me.lewisdeane.urbandictionary;

import android.content.Context;
import android.util.Log;

/**
 * Created by Lewis on 11/07/2014.
 */
public class SearchItem {

    private String title = "", up = "", down = "", meaning = "", example = "";
    private boolean isExpanded = false, isFavourite = false;
    Context context;

    public SearchItem() {

    }

    public SearchItem(Context _context, String _title, String _meaning, String _example, String _up, String _down, boolean _isExpanded) {
        setTitle(_title);
        setMeaning(_meaning);
        setExample(_example);
        setUp(_up);
        setDown(_down);
        setIsExpanded(_isExpanded);
        context = _context;
    }

    public void setTitle(String _title) {
        title = _title;
    }

    public void setExample(String _example) {
        example = _example;
    }

    public void setMeaning(String _meaning) {
        meaning = _meaning;
    }

    public void setUp(String _up) {
        up = _up;
    }

    public void setDown(String _down) {
        down = _down;
    }

    public void setIsExpanded(boolean _isExpanded) {
        isExpanded = _isExpanded;
    }

    public void setIsFavourite(boolean _isFavourite) {
        isFavourite = _isFavourite;

        Favourites fav = new Favourites(context, title, meaning, example, up, down);

        if (!fav.isFavourite() && isFavourite)
            fav.addFavourite();
        else if (!isFavourite && fav.isFavourite())
            fav.removeFavourite();
    }

    public String getTitle() {
        return title;
    }

    public String getExample() {
        return example;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getUp() {
        return up;
    }

    public String getDown() {
        return down;
    }

    public boolean getIsExpanded() {
        return isExpanded;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }
}
