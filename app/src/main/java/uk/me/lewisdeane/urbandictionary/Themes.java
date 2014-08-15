package uk.me.lewisdeane.urbandictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

/**
 * Created by Lewis on 21/07/2014.
 */
public class Themes {

    static String mTheme;

    static int mThemeNumber;
    private static int mPrimaryColour;
    private static int mSecondaryColour;
    private static SharedPreferences mSharedPreferences;

    static Context mContext;

    public Themes(Context _context){
        mContext = _context;
        init();
    }

    private static void init(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mTheme = mSharedPreferences.getString("PREF_COLOUR", mContext.getResources().getString(R.string.theme1));
        mThemeNumber = getThemeNumber(mTheme);
        mPrimaryColour = Color.parseColor(getPrimaryHex(mThemeNumber));
        mSecondaryColour = Color.parseColor(getSecondaryHex(mThemeNumber));
    }

    private static int getThemeNumber(String _theme) {
        if (_theme.equals(mContext.getString(R.string.theme1)))
            return 1;
        else if (_theme.equals(mContext.getString(R.string.theme2)))
            return 2;
        else if(_theme.equals(mContext.getString(R.string.theme3)))
            return 3;
        else if(_theme.equals(mContext.getString(R.string.theme4)))
            return 4;
        else if(_theme.equals(mContext.getString(R.string.theme5)))
            return 5;
        else if(_theme.equals(mContext.getString(R.string.theme6)))
            return 6;
        else if(_theme.equals(mContext.getString(R.string.theme7)))
            return 7;
        else if(_theme.equals(mContext.getString(R.string.theme8)))
            return 8;
        else
            return 1;
    }

    private static String getPrimaryHex(int mThemeNumber){
        switch (mThemeNumber){
            case 1: return "#009688";
            case 2: return "#E51C23";
            case 3: return "#673AB7";
            case 4: return "#FF9800";
            case 5: return "#259b24";
            case 6: return "#e91e63";
            case 7: return "#03a9f4";
            case 8: return "#607d8b";
            default: return "#009688";
        }
    }

    private static String getSecondaryHex(int mThemeNumber){
        switch (mThemeNumber){
            case 1: return "#00796b";
            case 2: return "#c41411";
            case 3: return "#4527a0";
            case 4: return "#F57C00";
            case 5: return "#0a7e07";
            case 6: return "#c2185b";
            case 7: return "#0288d1";
            case 8: return "#455a64";
            default: return "#008888";
        }
    }

    public static void applyBaseTheme(){
        ActionBarFragment.mContainer.setBackgroundColor(mPrimaryColour);
        MainActivity.mContainer.setBackgroundColor(mSecondaryColour);
    }

    public static void applyPreferenceTheme(){
        init();
        PreferenceActionBarFragment.mContainer.setBackgroundColor(mPrimaryColour);
        PreferenceFragment.mBackground.setBackgroundColor(mSecondaryColour);
    }

    public static void applyInfoTheme(){
        init();
        PreferenceActionBarFragment.mContainer.setBackgroundColor(mPrimaryColour);
        InfoActivity.mScroll.setBackgroundColor(mSecondaryColour);
    }

    public static Drawable getFavouriteIcon(boolean _isFav){
        switch (mThemeNumber) {
            case 1:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_aqua : R.drawable.ic_action_favorite_outline_aqua);
            case 2:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_rouge : R.drawable.ic_action_favorite_outline_rouge);
            case 3:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_deep_purple : R.drawable.ic_action_favorite_outline_deep_purple);
            case 4:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_orange : R.drawable.ic_action_favorite_outline_orange);
            case 5:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_forest_green : R.drawable.ic_action_favorite_outline_forest_green);
            case 6:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_pink : R.drawable.ic_action_favorite_outline_pink);
            case 7:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_blue : R.drawable.ic_action_favorite_outline_blue);
            case 8:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_grey : R.drawable.ic_action_favorite_outline_grey);
            default:
                return mContext.getResources().getDrawable(_isFav ? R.drawable.ic_action_favorite_aqua : R.drawable.ic_action_favorite_outline_aqua);
        }
    }

    public static int getPrimaryColour(){
        return mPrimaryColour;
    }

    public static int getSecondaryColour(){
        return mSecondaryColour;
    }
}
