package com.example.projetharmonisation.view.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projetharmonisation.R;

public class ImageDialogFragment extends DialogFragment {

    private static final String ARG_IMAGE_DATA = "image_data";

    public static ImageDialogFragment newInstance(byte[] imageData) {
        ImageDialogFragment fragment = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARG_IMAGE_DATA, imageData);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_image_viewer, container, false);
        ImageView imageView = view.findViewById(R.id.dialogImageView);
        ImageButton closeButton = view.findViewById(R.id.buttonClose);

        byte[] imageData = getArguments().getByteArray(ARG_IMAGE_DATA);
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
        }

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }
}
