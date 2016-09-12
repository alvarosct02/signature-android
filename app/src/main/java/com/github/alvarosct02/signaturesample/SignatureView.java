package com.github.alvarosct02.signaturesample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alvaro on 9/8/2016.
 */
public class SignatureView extends View implements View.OnTouchListener {
    private static final float STROKE_WIDTH = 5f;

    private Paint paint = new Paint();
    private Path path = new Path();
    private final RectF signatureRegion = new RectF();

    private float lastTouchX, lastTouchY;

    public SignatureView(Context context) {
        super(context);
        initView();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

//    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void initView() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

        this.setBackgroundColor(Color.WHITE);

//        Not sure if needed
//        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getHistorySize(); i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    updateSignatureRegion(historicalX, historicalY);
                    path.lineTo(eventX, eventY);
                }
        }

        invalidate(
                (int) (signatureRegion.left - STROKE_WIDTH / 2),
                (int) (signatureRegion.top - STROKE_WIDTH / 2),
                (int) (signatureRegion.right + STROKE_WIDTH / 2),
                (int) (signatureRegion.bottom + STROKE_WIDTH / 2)
        );

        lastTouchX = eventX;
        lastTouchY = eventY;
        return true;
    }

    private void updateSignatureRegion(float touchX, float touchY) {
        signatureRegion.left = Math.min(lastTouchX, touchX);
        signatureRegion.right = Math.max(lastTouchX, touchX);
        signatureRegion.top = Math.min(lastTouchY, touchY);
        signatureRegion.bottom = Math.max(lastTouchY, touchY);
    }

//    PUBLIC METHODS
    public void clearView(){
        path.reset();
        this.invalidate();
    }

    protected Bitmap getSignature() {
        Bitmap signatureBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(signatureBitmap);
        this.draw(canvas);
        return signatureBitmap;
    }
}
