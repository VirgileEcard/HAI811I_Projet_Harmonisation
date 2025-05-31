package com.example.projetharmonisation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BeforeAfterView extends View {

    private Bitmap beforeImage;
    private Bitmap afterImage;
    private float sliderPosition = 0.5f;
    private Paint paint = new Paint();
    private float imageAspectRatio = 1f;


    public BeforeAfterView(Context context) {
        super(context);
    }

    public BeforeAfterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImages(Bitmap before, Bitmap after) {
        this.beforeImage = before;
        if (before != null) {
            imageAspectRatio = (float) before.getWidth() / before.getHeight();
        }
        this.afterImage = after;
        requestLayout();
        invalidate();
    }

    public void setSliderPosition(float pos) {
        this.sliderPosition = Math.max(0f, Math.min(1f, pos));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (afterImage == null || beforeImage == null) return;

        Rect dst = new Rect(0, 0, getWidth(), getHeight());

        // dessiner l'image harmonisée au fond
        canvas.drawBitmap(afterImage, null, dst, paint);

        // calculer la partie visible de l'image originale
        int clipWidth = (int) (getWidth() * sliderPosition);
        canvas.save();
        canvas.clipRect(0, 0, clipWidth, getHeight());

        // dessiner l'image originale
        canvas.drawBitmap(beforeImage, null, dst, paint);
        canvas.restore();

        // dessiner la ligne de séparation
        Paint linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(4f);
        canvas.drawLine(clipWidth, 0, clipWidth, getHeight(), linePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int availableWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec);

        int targetHeight = (int) (availableWidth / imageAspectRatio);

        // si l'image est trop haute pour l'espace dispo, on ajuste à la hauteur max
        if (targetHeight > availableHeight) {
            targetHeight = availableHeight;
            availableWidth = (int) (targetHeight * imageAspectRatio);
        }

        int finalWidthSpec = MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY);
        int finalHeightSpec = MeasureSpec.makeMeasureSpec(targetHeight, MeasureSpec.EXACTLY);
        super.onMeasure(finalWidthSpec, finalHeightSpec);
    }

}

