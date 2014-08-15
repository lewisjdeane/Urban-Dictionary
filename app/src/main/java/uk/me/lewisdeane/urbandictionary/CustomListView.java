package uk.me.lewisdeane.urbandictionary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Lewis on 22/07/2014.
 */
public class CustomListView extends ListView {

    Context context;

    public CustomListView(Context _context){
        super(_context);
        context = _context;

        init(null);
    }

    public CustomListView(Context _context, AttributeSet _attrs){
        super(_context, _attrs);
        context = _context;

        init(_attrs);
    }

    public CustomListView(Context _context, AttributeSet _attrs, int _defStyle){
        super(_context, _attrs, _defStyle);
        context = _context;

        init(_attrs);
    }

    public void init(AttributeSet _attrs){
        Themes themes = new Themes(context);
        if (_attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(_attrs, R.styleable.CustomListView);
            boolean isThemed = a.getBoolean(R.styleable.CustomListView_isDividerThemed, true);
            setDivider(new ColorDrawable(isThemed ? themes.getPrimaryColour() : Color.WHITE));
            setDividerHeight(3);
            a.recycle();
        }
    }

}
