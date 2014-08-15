package uk.me.lewisdeane.urbandictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lewis on 15/07/2014.
 */
public class Favourites extends DatabaseHelper{

    Context context;
    private SearchItem mSearchItem;

    public Favourites(Context _context){
        super(_context);
    }

    public Favourites(Context _context, String _title, String _meaning, String _example, String _up, String _down) {
        super(_context);
        mSearchItem = new SearchItem(_context, _title, _meaning, _example, _up, _down, false);
        context = _context;
    }

    public Favourites(Context _context, SearchItem _searchItem){
        super(_context);
        mSearchItem = _searchItem;
        context = _context;
    }

    public boolean isFavourite() {
        open("W");

        Cursor C = mSQLiteDatabase.query(Database.FAVOURITES_TABLE, new String[]{"WORD, MEANING, EXAMPLE"}, "WORD=? AND MEANING=? AND EXAMPLE=?", new String[]{mSearchItem.getTitle(), mSearchItem.getMeaning(), mSearchItem.getExample()}, null, null, null);

        boolean isFav = true;

        if (C == null || !C.moveToFirst() || C.getColumnCount() == 0) {
            isFav =  false;
        }

        C.close();
        close();

        return isFav;
    }

    public void addFavourite() {
        open("W");

        ContentValues vals = new ContentValues();
        vals.put("WORD", mSearchItem.getTitle());
        vals.put("MEANING", mSearchItem.getMeaning());
        vals.put("EXAMPLE", mSearchItem.getExample());
        vals.put("UP", mSearchItem.getUp());
        vals.put("DOWN", mSearchItem.getDown());
        mSQLiteDatabase.insert(Database.FAVOURITES_TABLE, null, vals);

        close();
    }

    public void removeFavourite() {
        open("W");
        mSQLiteDatabase.delete(Database.FAVOURITES_TABLE, "WORD=? AND MEANING=? AND EXAMPLE=?", new String[]{mSearchItem.getTitle(), mSearchItem.getMeaning(), mSearchItem.getExample()});
        close();
    }

    public ArrayList<SearchItem> getFavourites(){

        open("R");

        ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();

        Cursor C = mSQLiteDatabase.query(Database.FAVOURITES_TABLE, new String[]{"WORD, MEANING, EXAMPLE", "UP", "DOWN" }, null, null, null, null, "WORD COLLATE NOCASE ASC");

        if (!(C == null || !C.moveToFirst() || C.getColumnCount() == 0)) {
            C.moveToFirst();
            do{
                SearchItem si = new SearchItem(mContext, C.getString(0), C.getString(1), C.getString(2), C.getString(3), C.getString(4), false);
                si.setIsFavourite(true);
                searchItems.add(si);
            } while(C.moveToNext());
        }

        C.close();
        close();

        return searchItems;
    }
}
