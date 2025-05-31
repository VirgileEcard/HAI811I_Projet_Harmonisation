package com.example.projetharmonisation.view.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetharmonisation.R;
import com.example.projetharmonisation.model.data.SavedImage;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<SavedImage> images = new ArrayList<>();

    public void setImages(List<SavedImage> newImages) {
        images.clear();
        images.addAll(newImages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        SavedImage img = images.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img.imageData, 0, img.imageData.length);
        holder.imageView.setImageBitmap(bitmap);
        holder.ratingBar.setRating(img.rating);

        holder.imageView.setOnClickListener(v -> {
            FragmentManager fm = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            ImageDialogFragment.newInstance(img.imageData).show(fm, "image_dialog");
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RatingBar ratingBar;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            ratingBar = itemView.findViewById(R.id.ratingBarItem);
        }
    }
}
