package com.example.projetharmonisation.view.gallery;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetharmonisation.R;
import com.example.projetharmonisation.model.data.SavedImage;
import com.example.projetharmonisation.view.MainActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class InternalGalleryFragment extends Fragment {

    private GalleryAdapter adapter;

    public InternalGalleryFragment() {
        super(R.layout.fragment_internal_gallery);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.recyclerViewGallery);
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        adapter = new GalleryAdapter();
        recycler.setAdapter(adapter);

        // Charger les images depuis Room
        Executors.newSingleThreadExecutor().execute(() -> {
            List<SavedImage> images = MainActivity.db.savedImageDao().getAllImages();
            requireActivity().runOnUiThread(() -> adapter.setImages(images));
        });
    }
}
