package com.superurl.magneturl.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.superurl.magneturl.R;

/**
 * @author hxk <br/>
 *         功能：
 *         创建日期   2017/3/21
 *         修改者：
 *         修改日期：
 *         修改内容:
 */

public class ProgressBarEx extends View {

    private static final String TAG = "ProgressBarEx";
    private static final boolean DEBUG = false;

    private static final int DEFAULT_MIN_WIDTH = 60;
    private static final int DEFAULT_MIN_HEIGHT = 60;
    private static final int BG_COLOR = Color.parseColor("#00aaaaaa");
    private static final int PG_COLOR = Color.parseColor("#23ca76");
    private static final int DIRECTION_UP = 0;
    private static final int DIRECTION_DOWN = 1;
    private static final int PADDING = 0;
    private static final int STROKE_WIDTH = 1;
    private static final float MAX_ANGLE = 80;
    private static final float MIN_ANGLE = 0;
    private static final float DELTA_ANGLE = 1.6f;
    private static final int INIT_ANGLE_ONE = 10;
    private static final int INIT_ANGLE_TWO = 190;

    private Paint arc0, bg;
    private int width, height;
    private int extra;
    private int radius;
    private Point center;
    private RectF rect;
    private int direction = DIRECTION_UP;
    private float sweepAgl = MIN_ANGLE;
    private int startAgl = INIT_ANGLE_ONE, startAgl2 = INIT_ANGLE_TWO;
    private float ra;
    private float deltaRa1, deltaRa2;

    private long maxTime, minTime;

    private int pgWidth = STROKE_WIDTH;
    private int pgColor = PG_COLOR;
    private int bgColor = BG_COLOR;

    public ProgressBarEx(Context context) {
        super(context);
        initAll(context, null);
    }

    public ProgressBarEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAll(context, attrs);
    }

    public ProgressBarEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAll(context, attrs);
    }

    private void initAll(Context context, AttributeSet attrs){
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarEx);
        pgWidth = ta.getDimensionPixelOffset(R.styleable.ProgressBarEx_pb_width, dp2px(context, STROKE_WIDTH) );
        pgColor = ta.getColor(R.styleable.ProgressBarEx_pb_color, PG_COLOR);
        bgColor = ta.getColor(R.styleable.ProgressBarEx_pb_background, BG_COLOR);
        ta.recycle();
    }

    private void init(){
        arc0 = new Paint(Paint.ANTI_ALIAS_FLAG);
        arc0.setStyle(Paint.Style.STROKE);
        arc0.setColor(pgColor);
        arc0.setStrokeWidth(pgWidth);
        arc0.setStrokeCap(Paint.Cap.ROUND);

        bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        bg.setStyle(Paint.Style.FILL);
        bg.setColor(bgColor);
        bg.setStrokeWidth(pgWidth);

        center = new Point();

        rect = new RectF();
        deltaRa2 = 180.0f * DELTA_ANGLE / MAX_ANGLE;
        deltaRa1 = deltaRa2 * 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode != MeasureSpec.EXACTLY){
            widthSize = dp2px(getContext(), DEFAULT_MIN_WIDTH);
        }
        if(heightMode != MeasureSpec.EXACTLY){
            heightSize = dp2px(getContext(), DEFAULT_MIN_HEIGHT);
        }
        extra = (int) arc0.getStrokeWidth();
        int w = widthSize;
        int h = heightSize;
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        center.x = width/2;
        center.y = height/2;
        int aw = width - getPaddingLeft() - getPaddingRight();
        int ah = height - getPaddingTop() - getPaddingBottom();
        int r = aw>ah?ah:aw;
        radius = r/2 - extra;
        rect = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc0(canvas);
        postInvalidate();
    }

    private void drawArc0(Canvas canvas){
        if(sweepAgl >= MAX_ANGLE){
            sweepAgl = MAX_ANGLE;
            direction = DIRECTION_DOWN;
            if(DEBUG){
                if(System.currentTimeMillis() - maxTime >100){
                    Log.i(TAG,"sweep comes to max value, time is: "+(System.currentTimeMillis()- minTime));
                    Log.i(TAG,"a max circle time --> "+(System.currentTimeMillis()-maxTime));
                }
                maxTime = System.currentTimeMillis();
            }
        } else if(sweepAgl <= MIN_ANGLE) {
            sweepAgl = MIN_ANGLE;
            direction = DIRECTION_UP;
            if(DEBUG){
                if(System.currentTimeMillis() - minTime >100){
                    Log.i(TAG,"sweep comes to min value, time is: "+(System.currentTimeMillis()- maxTime));
                    Log.i(TAG,"a min circle time --> "+(System.currentTimeMillis()-minTime));
                }
                minTime = System.currentTimeMillis();
            }
        }
        if(DIRECTION_UP == direction && sweepAgl < MAX_ANGLE){
            sweepAgl += DELTA_ANGLE;
        }
        if(DIRECTION_DOWN == direction && sweepAgl > MIN_ANGLE){
            sweepAgl -= DELTA_ANGLE;
        }
        if(ra < 360){
            if(DIRECTION_DOWN == direction){
                ra += deltaRa2;
            }else{
                ra += deltaRa1;
            }
        } else{
            ra -= 360;
        }
        canvas.drawArc(rect, 0, 360,false, bg);
        canvas.rotate(ra, center.x, center.y);
        canvas.drawArc(rect, startAgl- sweepAgl, 2*sweepAgl, false, arc0);
        canvas.drawArc(rect, startAgl2 -sweepAgl, 2*sweepAgl, false, arc0);
    }

    private static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
