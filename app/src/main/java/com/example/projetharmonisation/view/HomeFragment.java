package com.example.projetharmonisation.view;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetharmonisation.R;
import com.example.projetharmonisation.viewmodel.ImageViewModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    private ImageViewModel viewModel;
    private ImageButton buttonNext;
    RatioImageView previewImage;

    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri imageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView called");
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        previewImage = root.findViewById(R.id.previewImage);

        ImageButton buttonGallery = root.findViewById(R.id.buttonGallery);
        ImageButton buttonCamera = root.findViewById(R.id.buttonCamera);
        ImageButton buttonCloud = root.findViewById(R.id.buttonCloud);
        buttonNext = root.findViewById(R.id.buttonNext);
        ImageButton buttonGalleryInternal = root.findViewById(R.id.buttonGalleryInternal);
        //Button buttonClearGallery = root.findViewById(R.id.buttonClearGallery);


        viewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

        buttonGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_GALLERY);
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                            viewModel.setOriginalImage(bitmap);
                            previewImage.setImageBitmap(bitmap);
                            previewImage.setVisibility(View.VISIBLE);
                            buttonNext.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getContext(), "Capture annulée", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        buttonCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                openCamera();
            }
        });

        buttonCloud.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setHint("https://exemple.com/monimage.jpg");

            new AlertDialog.Builder(requireContext())
                    .setTitle("Importer depuis un lien")
                    .setMessage("Entrez l'URL d'une image")
                    .setView(input)
                    .setPositiveButton("Importer", (dialog, which) -> {
                        String url = input.getText().toString();
                        downloadImageFromUrl(url);
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        // Aller à la suite
        buttonNext.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_homeFragment_to_harmonySelectionFragment);
        });

        buttonGalleryInternal.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_internalGalleryFragment);
        });
/*
        buttonClearGallery.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Vider la galerie")
                    .setMessage("Voulez-vous vraiment supprimer toutes les images enregistrées ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            MainActivity.db.savedImageDao().deleteAll();
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Galerie vidée", Toast.LENGTH_SHORT).show()
                            );
                        });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });*/

        return root;
    }

    private File createTempImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = null;

            if (requestCode == REQUEST_GALLERY && data != null) {
                selectedImageUri = data.getData();
            } else if (requestCode == REQUEST_CAMERA) {
                selectedImageUri = imageUri;
            }

            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                    viewModel.setOriginalImage(bitmap);
                    previewImage.setImageBitmap(bitmap);
                    previewImage.setVisibility(View.VISIBLE);

                    buttonNext.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadImageFromUrl(String urlString) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();

                requireActivity().runOnUiThread(() -> {
                    viewModel.setOriginalImage(bitmap);
                    previewImage.setVisibility(View.VISIBLE);
                    previewImage.setImageBitmap(bitmap);
                    buttonNext.setEnabled(true);
                    Toast.makeText(getContext(), "Image importée avec succès", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Échec du téléchargement", Toast.LENGTH_SHORT).show()
                );
                e.printStackTrace();
            }
        });
    }

    private void openCamera() {
        try {
            File photoFile = createTempImageFile();
            imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    photoFile);
            cameraLauncher.launch(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur de création de fichier", Toast.LENGTH_SHORT).show();
        }
    }


}

