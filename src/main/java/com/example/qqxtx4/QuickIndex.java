package com.example.qqxtx4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wh on 2017/5/22.
 */

public class QuickIndex extends View {
    private String[] letterArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    public QuickIndex(Context context) {
        this(context,null);
    }

    public QuickIndex(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    Paint paint;
    int ColorDefault= Color.WHITE;
    int Colorpressed=Color.BLACK;
    public QuickIndex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint(paint.ANTI_ALIAS_FLAG);
        paint.setColor(ColorDefault);
        int textSize = getResources().getDimensionPixelSize(R.dimen.text_size);
        paint.setTextSize(textSize);
        //由于文字绘制的起点默认是左下角，
        paint.setTextAlign(Paint.Align.CENTER);//设置起点为底边的中心，

    }
    //一定用float来保证精度
    float cellHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellHeight = getMeasuredHeight() * 1f / letterArr.length;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        for (int i = 0; i < letterArr.length; i++) {
            String text =letterArr[i];
            float x = getMeasuredWidth() / 2;//当前view宽度的一半;
            float y = getTextHeight(text) / 2 + cellHeight / 2 + i * cellHeight;
            paint.setColor(index==i?Colorpressed:ColorDefault);
            canvas.drawText(text,x,y,paint);
        }

    }
    int index = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
        //使用y坐标除以格子的高，得到的是字母的索引
        int current = (int) (event.getY() /cellHeight);
        if(current!=index){
            index = current;

            //对代码进行安全性的检查
            if(index>=0 && index<letterArr.length){
                String letter = letterArr[index];
                if(listener!=null){
                    listener.onLetterChange(letter);
                }
                //Log.e("***************",letter);
            }
        }
        break;
        case MotionEvent.ACTION_UP:
            index = -1;
            if(listener!=null){
                listener.onRelease();
            }

            break;
        }
        invalidate();
        return  true;
    }

    private int getTextHeight(String text) {
        Rect bounds=new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        return  bounds.height();
    }



    //回调方法
    private OnLetterChangeListener listener;

    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        this.listener = listener;
    }

    public interface OnLetterChangeListener{
        void onLetterChange(String letter);
        //当抬起的时候执行
        void onRelease();
    }
}
