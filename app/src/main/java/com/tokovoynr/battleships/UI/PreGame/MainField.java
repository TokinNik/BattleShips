package com.tokovoynr.battleships.UI.PreGame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TableLayout;

public class MainField extends TableLayout
{
    public static final String TAG = "MAIN_FIELD";
    private float scale = 1f;
    private ScaleGestureDetector detector;

    public MainField(Context context)
    {
        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public MainField(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        return super.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        float onScaleBegin = 0;
        float onScaleEnd = 0;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            Log.d(TAG, "START");
            onScaleBegin = scale;

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale *= detector.getScaleFactor();

            setScaleX(scale);
            setScaleY(scale);

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            Log.d(TAG, "END");

            onScaleEnd = scale;

            if (onScaleEnd > onScaleBegin)
            {
                Log.d(TAG, "Scaled Up by a factor of " + String.valueOf( onScaleEnd / onScaleBegin ));
            }

            if (onScaleEnd < onScaleBegin)
            {
                Log.d(TAG,"Scaled Down by a factor of " + String.valueOf( onScaleBegin / onScaleEnd ));
            }

            super.onScaleEnd(detector);
        }
    }
}
