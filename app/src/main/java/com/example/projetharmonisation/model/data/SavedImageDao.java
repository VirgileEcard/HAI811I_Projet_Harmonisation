package com.example.projetharmonisation.model.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedImageDao {

    @Insert
    void insert(SavedImage image);

    @Query("SELECT * FROM saved_images ORDER BY timestamp DESC")
    List<SavedImage> getAllImages();

    @Query("DELETE FROM saved_images")
    void deleteAll();

}