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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        String wMode = "??";
        String hMode = "??";

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST) wMode = "AT_MOST";
        if(widthMode == MeasureSpec.EXACTLY) wMode = "EXACTLY";
        if(widthMode == MeasureSpec.UNSPECIFIED) wMode = "UNSPECIFIED";

        if(heightMode == MeasureSpec.AT_MOST) hMode = "AT_MOST";
        if(heightMode == MeasureSpec.EXACTLY) hMode = "EXACTLY";
        if(heightMode == MeasureSpec.UNSPECIFIED) hMode = "UNSPECIFIED";




        if(heightMode == MeasureSpec.EXACTLY) {
            myHeight = heightSize;
        }
        if(heightMode == MeasureSpec.AT_MOST) {
            if(heightSize < myHeight || myHeight == 0) {
                myHeight = heightSize;
            }
        }
     //   Log.d(TAG, "onMeasure: " + wMode + " / "  + widthSize + " / "  + hMode + " / "  + heightSize + " => " + myHeight);
        setMeasuredDimension(myHeight, myHeight);
    }

}
