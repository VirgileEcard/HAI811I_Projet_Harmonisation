package com.example.projetharmonisation.model.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SavedImage.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedImageDao savedImageDao();
}