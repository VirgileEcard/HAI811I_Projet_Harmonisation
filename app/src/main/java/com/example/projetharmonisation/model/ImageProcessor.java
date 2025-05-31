package com.example.projetharmonisation.model;

import static androidx.core.math.MathUtils.clamp;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageProcessor {
    public static Bitmap applyHarmony(Bitmap input, String harmonyType, float rotation, int adjustment) {
        int width = input.getWidth();
        int height = input.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, input.getConfig());

        float[][][] hsvMatrix = new float[width][height][3];
        boolean[] templateMask = HarmonizationTemplates.getTemplate(harmonyType, rotation);
        float factor = adjustment / 50f;

        //conversion HSV
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = input.getPixel(x, y);
                float[] hsv = new float[3];
                Color.colorToHSV(pixel, hsv);
                hsvMatrix[x][y] = hsv;
            }
        }

        //harmonisation + pondération spatiale
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                float[] hsv = hsvMatrix[x][y];
                int hueDeg = Math.round(hsv[0]) % 360;

                int targetHue = hueDeg;
                if (!templateMask[hueDeg]) {
                    targetHue = findClosestHueInMask(hueDeg, templateMask);
                }

                // moyenne pondérée avec les voisins
                float smoothedHue = smoothHue(hueDeg, targetHue, hsvMatrix, x, y);
                hsv[0] = smoothedHue;

                //ajustement saturation
                hsv[1] = clamp(hsv[1] * factor, 0f, 1f);

                output.setPixel(x, y, Color.HSVToColor(Color.alpha(input.getPixel(x, y)), hsv));
            }
        }

        return output;
    }

    private static int findClosestHueInMask(int hue, boolean[] mask) {
        int minDist = 360;
        int closest = hue;

        for (int i = 0; i < 360; i++) {
            if (mask[i]) {
                int dist = Math.min(Math.abs(i - hue), 360 - Math.abs(i - hue));
                if (dist < minDist) {
                    minDist = dist;
                    closest = i;
                }
            }
        }

        return closest;
    }

    private static float smoothHue(float originalHue, float targetHue, float[][][] hsvMatrix, int x, int y) {
        float weightCenter = 0.5f;
        float weightNeighbors = 0.5f / 4f;

        float total = weightCenter * targetHue;
        float sumWeights = weightCenter;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (Math.abs(dx) + Math.abs(dy) == 1) { //4-voisins
                    float neighborHue = hsvMatrix[x + dx][y + dy][0];
                    total += weightNeighbors * neighborHue;
                    sumWeights += weightNeighbors;
                }
            }
        }

        return (total / sumWeights + 360f) % 360f;
    }
}