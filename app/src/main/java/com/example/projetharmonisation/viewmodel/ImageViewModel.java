package com.example.projetharmonisation.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projetharmonisation.model.ImageProcessor;
import com.example.projetharmonisation.model.CNNProcessor;

public class ImageViewModel extends AndroidViewModel {
    private MutableLiveData<Bitmap> processedImage = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> originalImage = new MutableLiveData<>();
    private final MutableLiveData<String> harmonyType = new MutableLiveData<>();
    private final MutableLiveData<Float> templateRotation = new MutableLiveData<>(0f);


    public ImageViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Bitmap> getProcessedImage() {
        return processedImage;
    }

    public void setOriginalImage(Bitmap bitmap) {
        originalImage.setValue(bitmap);
    }
    public LiveData<Bitmap>  getOriginalImage() {
        return originalImage;
    }

    public void loadImage(Activity activity) {
    }

    public void setHarmonyType(String type) {
        harmonyType.setValue(type);
    }

    public LiveData<String> getHarmonyType() {
        return harmonyType;
    }

    public void applyTraditionalHarmony(int adjustment) {
        Bitmap input = originalImage.getValue();
        String type = harmonyType.getValue();
        Float rawRotation = templateRotation.getValue();
        float rotation = (rawRotation != null) ? (rawRotation % 360f) : 0f;

        if (input != null && type != null) {
            Bitmap result = ImageProcessor.applyHarmony(input, type, rotation, adjustment);
            processedImage.setValue(result);
        }
    }

    public void applyCNNHarmony(int adjustment) {
        Bitmap input = originalImage.getValue();
        String type = harmonyType.getValue();
        if (input != null && type != null) {
            Bitmap result = CNNProcessor.applyHarmonyCNN(input, type, adjustment);
            processedImage.setValue(result);
        }
    }


    public void setTemplateRotation(float angle) {
        templateRotation.setValue(angle);
    }

    public float getTemplateRotation() {
        Float value = templateRotation.getValue();
        return (value != null) ? value : 0f;
    }
}