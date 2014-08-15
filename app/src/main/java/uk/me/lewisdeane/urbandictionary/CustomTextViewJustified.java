package uk.me.lewisdeane.urbandictionary;

import uk.me.lewisdeane.urbandictionary.TextJustifyUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.TextView;
import android.os.Build;
import android.util.AttributeSet;



public class CustomTextViewJustified extends CustomTextView
{
    private Paint paint = new Paint();

    private String [] blocks;
    private float spaceOffset = 0;
    private float horizontalOffset = 0;
    private float verticalOffset = 0;
    private float horizontalFontOffset = 0;
    private float dirtyRegionWidth = 0;
    private boolean wrapEnabled = false;
    int left,top,right,bottom=0;
    private Align _align=Align.LEFT;
    private float strecthOffset;
    private float wrappedEdgeSpace;
    private String block;
    private String wrappedLine;
    private String [] lineAsWords;
    private Object[] wrappedObj;

    private Bitmap cache = null;
    private boolean cacheEnabled = false;

    public CustomTextViewJustified(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomTextViewJustified(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomTextViewJustified(Context context)
    {
        super(context);
    }

    @Override
    public void setDrawingCacheEnabled(boolean cacheEnabled)
    {
        this.cacheEnabled = cacheEnabled;
    }

    public void setText(String st, boolean wrap)
    {
        wrapEnabled = wrap;
        super.setText(st);
    }
    public void setTextAlign(Align align)
    {
        _align=align;
    }
    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas)
    {
        // If wrap is disabled then,
        // request original onDraw
        if(!wrapEnabled)
        {
            super.onDraw(canvas);
            return;
        }

        // Active canas needs to be set
        // based on cacheEnabled
        Canvas activeCanvas = null;

        // Set the active canvas based on
        // whether cache is enabled
        if (cacheEnabled) {

            if (cache != null)
            {
                // Draw to the OS provided canvas
                // if the cache is not empty
                canvas.drawBitmap(cache, 0, 0, paint);
                return;
            }
            else
            {
                // Create a bitmap and set the activeCanvas
                // to the one derived from the bitmap
                cache = Bitmap.createBitmap(getWidth(), getHeight(),
                        Config.ARGB_4444);
                activeCanvas = new Canvas(cache);
            }
        }
        else
        {
            // Active canvas is the OS
            // provided canvas
            activeCanvas = canvas;
        }

        // Pull widget properties
        paint.setColor(getCurrentTextColor());
        paint.setTypeface(getTypeface());
        paint.setTextSize(getTextSize());
        paint.setTextAlign(_align);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //minus out the paddings pixel         
        dirtyRegionWidth = getWidth()-getPaddingLeft()-getPaddingRight();
        int maxLines = Integer.MAX_VALUE;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
            maxLines = getMaxLines();
        }
        int lines = 1;
        blocks = getText().toString().split("((?<=\n)|(?=\n))");
        verticalOffset = horizontalFontOffset = getLineHeight() - 0.5f; // Temp fix
        spaceOffset = paint.measureText(" ");

        for(int i = 0; i < blocks.length && lines <= maxLines; i++)
        {
            block = blocks[i];
            horizontalOffset = 0;

            if(block.length() == 0)
            {
                continue;
            }
            else if(block.equals("\n"))
            {
                verticalOffset += horizontalFontOffset;
                continue;
            }

            block = block.trim();

            if(block.length() == 0)
            {
                continue;
            }

            wrappedObj = TextJustifyUtils.createWrappedLine(block, paint, spaceOffset, dirtyRegionWidth);

            wrappedLine = ((String) wrappedObj[0]);
            wrappedEdgeSpace = (Float) wrappedObj[1];
            lineAsWords = wrappedLine.split(" ");
            strecthOffset = wrappedEdgeSpace != Float.MIN_VALUE ? wrappedEdgeSpace/(lineAsWords.length - 1) : 0;

            for(int j = 0; j < lineAsWords.length; j++)
            {
                String word = lineAsWords[j];
                if (lines == maxLines && j == lineAsWords.length - 1)
                {
                    activeCanvas.drawText("...", horizontalOffset, verticalOffset, paint);


                }
                else if(j==0){
                    //if it is the first word of the line, text will be drawn starting from right edge of textview
                    if (_align==Align.RIGHT)
                    {
                        activeCanvas.drawText(word, getWidth()-(getPaddingRight()), verticalOffset, paint);
                        // add in the paddings to the horizontalOffset
                        horizontalOffset+=getWidth()-(getPaddingRight());
                    }
                    else
                    {
                        activeCanvas.drawText(word, getPaddingLeft(), verticalOffset, paint);
                        horizontalOffset+=getPaddingLeft();
                    }

                }
                else
                {
                    activeCanvas.drawText(word, horizontalOffset, verticalOffset, paint);
                }
                if (_align==Align.RIGHT)
                    horizontalOffset -=  paint.measureText(word) + spaceOffset + strecthOffset;
                else
                    horizontalOffset += paint.measureText(word) + spaceOffset + strecthOffset;
            }

            lines++;

            if(blocks[i].length() > 0)
            {
                blocks[i] = blocks[i].substring(wrappedLine.length());
                verticalOffset += blocks[i].length() > 0 ? horizontalFontOffset : 0;
                i--;
            }
        }

        if (cacheEnabled)
        {
            // Draw the cache onto the OS provided
            // canvas.
            canvas.drawBitmap(cache, 0, 0, paint);
        }
    }
}