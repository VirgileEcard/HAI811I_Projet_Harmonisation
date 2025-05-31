package com.example.projetharmonisation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TemplatePreviewView extends View {

    private boolean[] templateMask;
    private Paint paint;

    public TemplatePreviewView(Context context) {
        super(context);
        init();
    }

    public TemplatePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL); // disque plein
    }

    public void setTemplate(boolean[] templateMask) {
        this.templateMask = templateMask;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = Math.min(getWidth(), getHeight()) / 2;
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        canvas.save();
        canvas.rotate(-90, cx, cy);

        for (int i = 0; i < 360; i++) {
            float[] hsv = new float[]{i, 1f, 1f};
            paint.setColor(Color.HSVToColor(templateMask != null && templateMask[i] ? 255 : 40, hsv));

            Path sector = new Path();
            sector.moveTo(cx, cy);
            double angleRad1 = Math.toRadians(i);
            double angleRad2 = Math.toRadians(i + 1);

            float x1 = cx + (float) (radius * Math.cos(angleRad1));
            float y1 = cy + (float) (radius * Math.sin(angleRad1));
            float x2 = cx + (float) (radius * Math.cos(angleRad2));
            float y2 = cy + (float) (radius * Math.sin(angleRad2));

            sector.lineTo(x1, y1);
            sector.lineTo(x2, y2);
            sector.close();

            canvas.drawPath(sector, paint);
        }

        canvas.restore();
    }
}

