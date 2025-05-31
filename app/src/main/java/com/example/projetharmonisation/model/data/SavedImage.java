package com.example.projetharmonisation.model.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_images")
public class SavedImage {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "image_data", typeAffinity = ColumnInfo.BLOB)
    public byte[] imageData;

    @ColumnInfo(name = "rating")
    public int rating;

    public SavedImage(byte[] imageData, int rating) {
        this.imageData = imageData;
        this.rating = rating;
        this.timestamp = System.currentTimeMillis();
    }
}