package de.silbaer.dmlcalc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * TODO: document your custom view class.
 */
public class MyImageView extends ImageView {
    private static final String TAG = MyImageView.class.getSimpleName();
    int myHeight = 0;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable mDrawable = getDrawable();
        if (mDrawable == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int mDrawableWidth = mDrawable.getIntrinsicWidth();
        int mDrawableHeight = mDrawable.getIntrinsicHeight();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);







        if(heightMode == MeasureSpec.EXACTLY) {
            myHeight = heightSize;
        }
        if(heightMode == MeasureSpec.AT_MOST) {
            if(heightSize < myHeight || myHeight == 0) {
                myHeight = heightSize;
            }
        }
     //   Log.d(TAG, "onMeasure: " + wMode + " / "  + widthSize + " / "  + hMode + " / "  + heightSize + " => " + myHeight);
     //   Log.d("atga", myHeight + " !! " +  mDrawableHeight + "/" + mDrawableWidth + " "  + MeasureSpec.toString(heightMode) + " (" + heightSize + ")" + " / "  + MeasureSpec.toString(widthMode) + " (" + widthSize + ")");

        setMeasuredDimension(myHeight, myHeight);
    }

}
