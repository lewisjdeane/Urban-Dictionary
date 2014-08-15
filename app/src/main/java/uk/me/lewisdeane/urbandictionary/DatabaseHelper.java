package uk.me.lewisdeane.urbandictionary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lewis on 19/07/2014.
 */
public class DatabaseHelper {

    Database mDatabase;
    Context mContext;
    SQLiteDatabase mSQLiteDatabase;
    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    boolean isOpen = false;
    ProgressDialog mProgressDialog;
    boolean isFinished = false;

    public DatabaseHelper(Context _context) {
        mContext = _context;
        //mDatabase = new Database(mContext); dont think its needed
    }

    public ArrayList<String> readSuggestions(String _searching) {
        open("R");

        ArrayList<String> suggestions = new ArrayList<String>();
        try {
            Cursor C = mSQLiteDatabase.rawQuery("SELECT WORD FROM " + Database.POPULAR_SUGGESTIONS_TABLE + " WHERE WORD LIKE '" + _searching + "%'", null);

            if (!(C == null || !C.moveToFirst() || C.getColumnCount() == 0)) {
                C.moveToFirst();
                do {
                    if (!C.getString(0).trim().toLowerCase().equals(ActionBarFragment.mSearchBox.getText().toString().trim().toLowerCase()))
                        suggestions.add(C.getString(0));
                } while (C.moveToNext() && suggestions.size() < 3);
            }
            C.close();
        } catch(SQLiteException exception){
            exception.printStackTrace();
        }
        close();
        return suggestions;
    }

    public void checkForDatabase(){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMax(26);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("Downloading suggestions database needed for app - this won't take long!");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        Database database = new Database(mContext);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();

        for(char c : alphabet) {
            Cursor C = sqLiteDatabase.query(Database.POPULAR_SUGGESTIONS_TABLE, new String[]{"LETTER", "WORD"}, "LETTER=?", new String[]{c+""}, null, null, "WORD COLLATE NOCASE DESC");
            if(C == null || !C.moveToFirst() || C.getColumnCount() == 0) {
                mProgressDialog.show();
                new AddPopularWordToDatabase().execute(c + "");
            } else{
                mProgressDialog.incrementProgressBy(1);
            }
            C.close();
        }

        sqLiteDatabase.close();
        database.close();
    }

    public void close() {
        isOpen = false;
        mDatabase.close();
        mSQLiteDatabase.close();
    }

    public void open(String _mode) {
        isOpen = true;
        mDatabase = new Database(mContext);
        mSQLiteDatabase = _mode.equals("R") ? mDatabase.getReadableDatabase() : mDatabase.getWritableDatabase();
    }


    public class AddPopularWordToDatabase extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... _letter) {
            open("W");

            try {
                Document doc = Jsoup.connect("http://www.urbandictionary.com/popular.php?character=" + _letter[0].toUpperCase()).get();
                Elements words = doc.select("li[class=popular]");

                for (Element word : words) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("LETTER", _letter[0]);
                    contentValues.put("WORD", word.text());
                    mSQLiteDatabase.insert(Database.POPULAR_SUGGESTIONS_TABLE, null, contentValues);
                }

                close();

                return String.format("Added %d words beginning with %s the database!", words.size(), _letter[0]);
            } catch (IOException e) {

                close();
                e.printStackTrace();
                return "Error downloading popular words - App may not  be connected to internet.";
            }
        }

        protected void onPostExecute(String _record) {
            mProgressDialog.incrementProgressBy(1);
            if(mProgressDialog.getProgress() == mProgressDialog.getMax()) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, mContext.getString(R.string.downloading_complete), Toast.LENGTH_SHORT).show();
            }
            Log.i("URBAN DICTIONARY", _record);
        }
    }
}
