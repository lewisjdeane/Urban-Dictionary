package uk.me.lewisdeane.urbandictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lewis on 19/07/2014.
 */
public class ConnectionHelper {

    Context mContext;
    String mUrl = MainActivity.BASE_URL;
    static AsyncTask<String, Void, ArrayList<SearchItem>> runningTask;
    public static boolean hasFinished = true;
    public static String mCurrentSearch = "";

    public ConnectionHelper(Context _context) {
        mContext = _context;
    }

    public void getSearchResults(String _search) {

        hasFinished = false;

        mCurrentSearch = _search;

        if(_search.length() > 0){
            mUrl = "http://www.urbandictionary.com/define.php?term=" + _search;
        }

        if(!MainActivity.NETWORK_AVAILABLE)
            mUrl = null;

        if(runningTask != null){
            runningTask.cancel(true);
        }

        if(mUrl == null){
            MainFragment.mNoResults.setVisibility(View.VISIBLE);
            MainFragment.results.setVisibility(View.GONE);
            MainFragment.mNoResults.setText(mContext.getString(R.string.no_favourites));
            for(SearchItem item : new Favourites(mContext).getFavourites()){
                MainFragment.searchItems.add(item);
                MainFragment.mNoResults.setVisibility(View.GONE);
                MainFragment.results.setVisibility(View.VISIBLE);
            }
            MainFragment.customAdapter.notifyDataSetChanged();
        } else{
            runningTask = new DownloadResults().execute(mUrl);
        }
    }

    private class DownloadResults extends AsyncTask<String, Void, ArrayList<SearchItem>> {

        @Override
        protected ArrayList<SearchItem> doInBackground(String... _url) {

            ArrayList<SearchItem> items = new ArrayList<SearchItem>();

            try {
                Document doc = Jsoup.connect(_url[0]).get();

                Elements titles = doc.select("a[class=word]");
                Elements meanings = doc.select("div[class=meaning]");
                Elements examples = doc.select("div[class=example]");
                Elements rating = doc.select("span[class=count]");

                for(int i = 0; i < titles.size(); i++) {
                    items.add(new SearchItem(mContext, titles.get(i).text(), meanings.get(i).text(), examples.get(i).text(), "+" + rating.get(i*2).text(), "-" + rating.get(i*2+1).text(), false));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<SearchItem> _items) {
            super.onPostExecute(_items);

            MainFragment.mNoResults.setVisibility(View.VISIBLE);
            MainFragment.results.setVisibility(View.GONE);
            MainFragment.mNoResults.setText(String.format((mContext.getString(R.string.no_results_prefix) + " %s."), mCurrentSearch));

            for(SearchItem item : _items){
                item.setIsFavourite(new Favourites(mContext, item).isFavourite());
                MainFragment.searchItems.add(item);
                MainFragment.mNoResults.setVisibility(View.GONE);
                MainFragment.results.setVisibility(View.VISIBLE);
            }

            MainFragment.customAdapter.notifyDataSetChanged();

            hasFinished = true;
        }

    }

}