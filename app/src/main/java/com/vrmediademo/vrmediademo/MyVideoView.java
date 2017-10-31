package com.vrmediademo.vrmediademo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by djf on 2016/9/19.
 */
public class MyVideoView extends VideoView {
    private Context mContext;

    public MyVideoView(Context context) {
        super(context);
        mContext = context;
    }

    public MyVideoView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //我们重新计算高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
//        super.onMeasure( widthMeasureSpec,  heightMeasureSpec);
    }
}
