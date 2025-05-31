package com.example.projetharmonisation.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TemplateSelectorView extends LinearLayout {

    private TemplatePreviewView previewView;
    private TextView label;
    private boolean selected = false;

    public TemplateSelectorView(Context context, String labelText, boolean[] templateMask) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(8, 8, 8, 8);

        previewView = new TemplatePreviewView(context);
        LayoutParams previewParams = new LayoutParams(250, 250);
        previewView.setLayoutParams(previewParams);
        previewView.setTemplate(templateMask);
        addView(previewView);

        label = new TextView(context);
        label.setText(labelText);
        label.setTextSize(14);
        label.setGravity(Gravity.CENTER);
        addView(label);

        setBackground(createBackground(false));
    }

    public void setSelectedState(boolean selected) {
        this.selected = selected;
        setBackground(createBackground(selected));
        invalidate();
    }

    private Drawable createBackground(boolean selected) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(16f);
        drawable.setColor(Color.TRANSPARENT);
        drawable.setStroke(selected ? 6 : 2, selected ? Color.BLUE : Color.LTGRAY);
        return drawable;
    }

    public String getLabelText() {
        return label.getText().toString();
    }
}
