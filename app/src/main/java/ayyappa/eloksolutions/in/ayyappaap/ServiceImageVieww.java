package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ServiceImageVieww extends ImageView {
    public ServiceImageVieww(Context context) {
        super(context);
    }

    public ServiceImageVieww(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ServiceImageVieww(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
