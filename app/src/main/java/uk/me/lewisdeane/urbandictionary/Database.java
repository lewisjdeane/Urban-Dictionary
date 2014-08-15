package uk.me.lewisdeane.urbandictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lewis on 19/07/2014.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    public static String POPULAR_SUGGESTIONS_TABLE = "PopularSuggestionsTable";
    public static String FAVOURITES_TABLE = "FavouritesTable";

    private static final String POPULAR_SUGGESTIONS_CREATE = "create table " + POPULAR_SUGGESTIONS_TABLE
            + " (LETTER TEXT, WORD TEXT);";
    private static final String FAVOURITES_CREATE = "create table " + FAVOURITES_TABLE
            + " (WORD TEXT, MEANING TEXT, EXAMPLE TEXT, UP TEXT, DOWN TEXT);";

    Database(Context context) {
        super(context, "Database", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POPULAR_SUGGESTIONS_CREATE);
        db.execSQL(FAVOURITES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POPULAR_SUGGESTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITES_TABLE);
        this.onCreate(db);
    }

}
