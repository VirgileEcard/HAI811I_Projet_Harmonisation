package com.example.projetharmonisation.view;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.projetharmonisation.model.HarmonizationTemplates;
import com.example.projetharmonisation.viewmodel.ImageViewModel;
import com.example.projetharmonisation.R;

import java.util.ArrayList;
import java.util.List;

public class HarmonySelectionFragment extends Fragment {

    private ImageViewModel viewModel;
    private String selectedHarmony;

    private RatioImageView previewImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_harmony_selection, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

        ImageButton buttonContinue = root.findViewById(R.id.buttonContinue);


        String[] types = {"Mono", "Complémentaire", "Split", "Analogues", "Triadiques", "Tétradiques"};
        GridLayout container_templates = root.findViewById(R.id.templateContainer);


        List<TemplateSelectorView> selectorViews = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            boolean[] mask = HarmonizationTemplates.getTemplate(type, 0f);
            TemplateSelectorView selector = new TemplateSelectorView(requireContext(), type, mask);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setGravity(Gravity.FILL_HORIZONTAL);
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3);
            selector.setLayoutParams(params);

            selector.setOnClickListener(v -> {
                selectedHarmony = type;
                buttonContinue.setEnabled(true);

                for (TemplateSelectorView view : selectorViews) {
                    view.setSelectedState(view == selector);
                }
            });

            selectorViews.add(selector);
            container_templates.addView(selector);
        }


        buttonContinue.setOnClickListener(v -> {
            viewModel.setHarmonyType(selectedHarmony);
            Navigation.findNavController(v).navigate(R.id.action_harmonySelectionFragment_to_editorFragment);
        });

        RatioImageView previewImage = root.findViewById(R.id.previewImage);
        viewModel.getOriginalImage().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                previewImage.setImageBitmap(bitmap);
            }
        });

        return root;
    }
}
