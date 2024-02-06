package com.example.myapplication.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTranslateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslateFragment extends Fragment {
    private FragmentTranslateBinding binding;
    private EditText etSourceLang;
    private TextView tvDestinationLang;
    private MaterialButton mbSourceLang;
    private MaterialButton mbDestinationLang;
    private MaterialButton mbTranslate;

    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private ArrayList<ModelLanguage> languageArrayList;

    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "ur";
    private String destinationLanguageTitle = "Urdu";
    private String sourceLangText = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentTranslateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etSourceLang = root.findViewById(R.id.et_source_lang);
        tvDestinationLang = root.findViewById(R.id.tv_destination_lang);
        mbSourceLang = root.findViewById(R.id.btn_choose_source_lang);
        mbDestinationLang = root.findViewById(R.id.btn_choose_destination_lang);
        mbTranslate = root.findViewById(R.id.btn_translate);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadAvailableLanguages();

        mbSourceLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseSourceLanguage();
            }
        });

        mbDestinationLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDestinationLanguage();
            }
        });

        mbTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        return root;
    }


    private void validateData() {
        sourceLangText = etSourceLang.getText().toString().trim();

        if (sourceLangText.isEmpty()) {
            Toast.makeText(getActivity(), "Enter text to translate...", Toast.LENGTH_SHORT).show();
        } else {
            startTranslation();
        }
    }

    private void startTranslation() {
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();
        translator = Translation.getClient(translatorOptions);

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                //.requireWifi()
                .build();

        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.setMessage("Translating...");

                        translator.translate(sourceLangText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        progressDialog.dismiss();
                                        tvDestinationLang.setText(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Failed to translate due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed to ready model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void chooseSourceLanguage() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), mbSourceLang);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLangTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position = menuItem.getItemId();
                sourceLanguageCode = languageArrayList.get(position).getLangCode();
                sourceLanguageTitle = languageArrayList.get(position).getLangTitle();

                mbSourceLang.setText(sourceLanguageTitle);
                etSourceLang.setHint("Enter " + sourceLanguageTitle);
                return false;
            }
        });
    }

    private void chooseDestinationLanguage() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), mbDestinationLang);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLangTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position = menuItem.getItemId();
                destinationLanguageCode = languageArrayList.get(position).getLangCode();
                destinationLanguageTitle = languageArrayList.get(position).getLangTitle();

                mbDestinationLang.setText(destinationLanguageTitle);

                return false;
            }
        });
    }

    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();
        List<String> langCodeList = TranslateLanguage.getAllLanguages();

        for (String langCode: langCodeList) {
            String langTitle = new Locale(langCode).getDisplayLanguage();
            ModelLanguage modelLanguage = new ModelLanguage(langCode, langTitle);
            languageArrayList.add(modelLanguage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}