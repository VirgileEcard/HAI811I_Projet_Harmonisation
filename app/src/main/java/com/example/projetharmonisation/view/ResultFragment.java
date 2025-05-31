package com.example.projetharmonisation.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.projetharmonisation.model.data.SavedImage;
import com.example.projetharmonisation.viewmodel.ImageViewModel;
import com.example.projetharmonisation.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

public class ResultFragment extends Fragment {

    private ImageViewModel viewModel;
    private RatioImageView imageFinal;
    private RatingBar ratingBar;
    private Bitmap finalBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_result, container, false);

        imageFinal = root.findViewById(R.id.imageFinal);
        ratingBar = root.findViewById(R.id.ratingBar);
        ImageButton buttonSave = root.findViewById(R.id.buttonSave);
        ImageButton buttonShare = root.findViewById(R.id.buttonShare);
        ImageButton buttonDelete = root.findViewById(R.id.buttonDelete);


        viewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

        viewModel.getProcessedImage().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                finalBitmap = bitmap;
                imageFinal.setImageBitmap(bitmap);
            }
        });

        buttonSave.setOnClickListener(v -> {
            if (finalBitmap != null) {
                try {
                    String path = saveImage(finalBitmap);
                    Toast.makeText(getContext(), "Image enregistrée : " + path, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erreur d'enregistrement", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonShare.setOnClickListener(v -> {
            Bitmap image = viewModel.getProcessedImage().getValue();
            int rating = Math.round(ratingBar.getRating());

            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                SavedImage savedImage = new SavedImage(byteArray, rating);

                Executors.newSingleThreadExecutor().execute(() -> {
                    MainActivity.db.savedImageDao().insert(savedImage);
                });

                Toast.makeText(getContext(), "Image exportée vers la galerie", Toast.LENGTH_SHORT).show();
            }

        });

        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            //Toast.makeText(getContext(), "Note enregistrée : " + rating, Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_resultFragment_to_homeFragment));

        return root;
    }

    private String saveImage(Bitmap bitmap) throws IOException {
        File dir = new File(requireContext().getExternalFilesDir(null), "images");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, "harmonised_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close();
        return file.getAbsolutePath();
    }

    private File saveImageToCache(Bitmap bitmap) throws IOException {
        File cachePath = new File(requireContext().getCacheDir(), "images");
        if (!cachePath.exists()) cachePath.mkdirs();

        File file = new File(cachePath, "shared_image.jpg");
        FileOutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
        return file;
    }
}
