package com.jxnu.cure.geekbrowser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;

/**
 * Created by asus on 2017/12/1.
 */

public class AdjustDrawButton extends AppCompatButton{

    public AdjustDrawButton(Context context) {
        super(context);
    }

    public AdjustDrawButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdjustDrawButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void setBackgroundSize(int direction, int width, int height){
        Drawable[] drawables = getCompoundDrawables();
        if(drawables[direction] == null)
            return;
        Drawable drawable = drawables[direction];
        drawable.setBounds(0,0,width,height);
        drawables[direction] = drawable;
        super.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
    }
}
