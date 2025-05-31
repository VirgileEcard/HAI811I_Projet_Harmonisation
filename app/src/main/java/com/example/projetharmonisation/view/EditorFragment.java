package com.example.projetharmonisation.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.projetharmonisation.viewmodel.ImageViewModel;
import com.example.projetharmonisation.model.ImageProcessor;
import com.example.projetharmonisation.R;

public class EditorFragment extends Fragment {

    private ImageViewModel viewModel;
    private SeekBar seekBar;
    private int adjustmentValue = 50;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editor, container, false);


        seekBar = root.findViewById(R.id.seekBarAdjustment);

        ImageButton buttonClassic = root.findViewById(R.id.buttonApplyClassic);
        //ImageButton buttonCNN = root.findViewById(R.id.buttonApplyCNN);
        ImageButton buttonNext = root.findViewById(R.id.buttonNext);

        viewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

        viewModel.applyTraditionalHarmony(adjustmentValue);

        BeforeAfterView beforeAfterView = root.findViewById(R.id.beforeAfterView);
        SeekBar slider = root.findViewById(R.id.seekBarSlider);

        viewModel.getOriginalImage().observe(getViewLifecycleOwner(), original -> {
            if (original != null && viewModel.getProcessedImage().getValue() != null) {
                beforeAfterView.setImages(original, viewModel.getProcessedImage().getValue());
            }
        });

        viewModel.getProcessedImage().observe(getViewLifecycleOwner(), processed -> {
            if (processed != null && viewModel.getOriginalImage().getValue() != null) {
                beforeAfterView.setImages(viewModel.getOriginalImage().getValue(), processed);
            }
        });

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                beforeAfterView.setSliderPosition(progress / 100f);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ColorWheelPickerView rotationPicker = root.findViewById(R.id.rotationPicker);

        rotationPicker.setOnAngleSelectedListener(angle -> {
            viewModel.setTemplateRotation(angle);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustmentValue = progress;
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        buttonClassic.setOnClickListener(v -> viewModel.applyTraditionalHarmony(adjustmentValue));
        //buttonCNN.setOnClickListener(v -> viewModel.applyCNNHarmony(adjustmentValue));
        buttonNext.setOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_editorFragment_to_resultFragment));

        return root;
    }
}
