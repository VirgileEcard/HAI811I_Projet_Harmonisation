package com.example.projetharmonisation.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.projetharmonisation.R;
import com.example.projetharmonisation.model.data.AppDatabase;

public class MainActivity extends AppCompatActivity {

    public static AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "image-db")
                .fallbackToDestructiveMigration()
                .build();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }
}