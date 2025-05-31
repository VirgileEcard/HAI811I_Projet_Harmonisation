package com.example.projetharmonisation.model;

public class HarmonizationTemplates {

    public static boolean[] getTemplate(String harmonyType, float rotationDeg) {
        boolean[] mask = new boolean[360];

        switch (harmonyType.toLowerCase()) {
            case "mono": // i
                fillSector(mask, rotationDeg, 30);
                break;
            case "complémentaire": // I
                fillSector(mask, rotationDeg, 30);
                fillSector(mask, (rotationDeg + 180) % 360, 30);
                break;
            case "split":
                fillSector(mask, rotationDeg, 30);
                fillSector(mask, (rotationDeg + 150) % 360, 30);
                fillSector(mask, (rotationDeg + 210) % 360, 30);
                break;
            case "analogues": // V
                fillSector(mask, (rotationDeg - 30 + 360) % 360, 30);
                fillSector(mask, rotationDeg, 30);
                fillSector(mask, (rotationDeg + 30) % 360, 30);
                break;
            case "triadiques": // Y
                fillSector(mask, rotationDeg, 30);
                fillSector(mask, (rotationDeg + 120) % 360, 30);
                fillSector(mask, (rotationDeg + 240) % 360, 30);
                break;
            case "tétradiques": // X
                fillSector(mask, rotationDeg, 30);
                fillSector(mask, (rotationDeg + 90) % 360, 30);
                fillSector(mask, (rotationDeg + 180) % 360, 30);
                fillSector(mask, (rotationDeg + 270) % 360, 30);
                break;
        }
        return mask;
    }

    private static void fillSector(boolean[] mask, float center, float width) {
        int start = Math.round((center - width / 2 + 360) % 360);
        int end = Math.round((center + width / 2) % 360);
        for (int i = 0; i < 360; i++) {
            int angle = (i + 360) % 360;
            if ((start < end && angle >= start && angle <= end) ||
                    (start > end && (angle >= start || angle <= end))) {
                mask[i] = true;
            }
        }
    }
}
