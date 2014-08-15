package uk.me.lewisdeane.urbandictionary;

import android.content.Context;

/**
 * Created by Lewis on 29/07/2014.
 */
public class SettingsItem {

    private Context mContext;
    private String mTitle = "", mSubtitle = "";

    public SettingsItem(Context _context, String _title, String _subtitle){
        mContext = _context;
        setTitle(_title);
        setSubtitle(_subtitle);
    }

    public void setTitle(String _title){
        mTitle = _title;
    }

    public void setSubtitle(String _subtitle){
        mSubtitle = _subtitle;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getSubtitle(){
        return mSubtitle;
    }
}

