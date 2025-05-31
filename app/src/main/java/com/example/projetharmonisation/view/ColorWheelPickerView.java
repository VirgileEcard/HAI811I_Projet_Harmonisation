package com.example.projetharmonisation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorWheelPickerView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float selectorAngle = 0f;
    private OnAngleSelectedListener listener;

    public ColorWheelPickerView(Context context) {
        super(context);
    }

    public ColorWheelPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnAngleSelectedListener(OnAngleSelectedListener listener) {
        this.listener = listener;
    }

    public void setAngle(float angle) {
        this.selectorAngle = (angle) % 360f;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // dessiner le cercle chromatique
        int size = Math.min(getWidth(), getHeight());
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = size / 2 - 20;
        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        for (int i = 0; i < 360; i++) {
            paint.setColor(Color.HSVToColor(new float[]{i, 1f, 1f}));
            paint.setAlpha(255);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(30);
            canvas.drawArc(oval, i, 1, false, paint);
        }

        // dessiner le curseur
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        float rad = (float) Math.toRadians(selectorAngle);
        float x = cx + (float) (radius * Math.cos(rad));
        float y = cy + (float) (radius * Math.sin(rad));
        canvas.drawCircle(x, y, 15, paint);

        // cercle au centre avec la couleur sélectionnée
        int sizeCenter = Math.min(getWidth(), getHeight());
        int cxCenter = getWidth() / 2;
        int cyCenter = getHeight() / 2;
        int centerRadius = sizeCenter / 6; // 1/6 du diamètre

        float[] hsvColor = new float[]{selectorAngle, 1f, 1f};
        paint.setColor(Color.HSVToColor(hsvColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cxCenter, cyCenter, centerRadius, paint);

        // contour blanc
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4f);
        canvas.drawCircle(cxCenter, cyCenter, centerRadius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float dx = event.getX() - getWidth() / 2f;
        float dy = event.getY() - getHeight() / 2f;
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

        selectorAngle = (angle + 360f /*+ 90f*/) % 360f; // +90 pour placer 0° en haut
        invalidate();

        if (listener != null) {
            listener.onAngleSelected(selectorAngle);
        }
        return true;
    }

    public interface OnAngleSelectedListener {
        void onAngleSelected(float angleInDegrees);
    }
}
