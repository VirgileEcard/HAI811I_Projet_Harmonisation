package com.example.projetharmonisation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

public class RatioImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float aspectRatio = 1f;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            aspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();
        }
        super.setImageBitmap(bitmap);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int availableWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec);

        int targetHeight = (int) (availableWidth / aspectRatio);
        if (targetHeight > availableHeight) {
            targetHeight = availableHeight;
            availableWidth = (int) (targetHeight * aspectRatio);
        }

        int finalWidth = MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.EXACTLY);
        int finalHeight = MeasureSpec.makeMeasureSpec(targetHeight, MeasureSpec.EXACTLY);
        super.onMeasure(finalWidth, finalHeight);
    }
}
