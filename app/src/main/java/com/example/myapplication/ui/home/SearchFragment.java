package com.example.myapplication.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.ui.PageResult.PageResultActivity;

public class SearchFragment extends Fragment {
    private Spinner spinnerTo, spinnerFrom;
    //private List<String> spinnerArray = Arrays.asList("English", "Русский");
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinnerTo = binding.spinnerTo;
        spinnerFrom = binding.spinnerFrom;

//        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTo.setAdapter(adapter);
//        spinnerFrom.setAdapter(adapter);

        if (!getActivity().getSharedPreferences("HomeSpinnerTo", 0).getBoolean("firstRun", false)) {
            int selVal = getActivity().getSharedPreferences("HomeSpinnerTo", 0).getInt("HomeSpinnerTo", -1);
            if (selVal != -1) {
                spinnerTo.setSelection(selVal);
            }
        }

        if (!getActivity().getSharedPreferences("HomeSpinnerFrom", 0).getBoolean("firstRun", false)) {
            int selVal = getActivity().getSharedPreferences("HomeSpinnerFrom", 0).getInt("HomeSpinnerFrom", -1);
            if (selVal != -1) {
                spinnerFrom.setSelection(selVal);
            }
        }

        //SetPrefs
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerOnItemSelected(spinnerTo, "HomeSpinnerTo", spinnerFrom);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerOnItemSelected(spinnerFrom, "HomeSpinnerFrom", spinnerTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText et = binding.editTextWordToTransl;
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Intent intent = new Intent(getActivity(), PageResultActivity.class);
                intent.putExtra("word", et.getText().toString());
                intent.putExtra("langFrom", spinnerFrom.getSelectedItemPosition());
                intent.putExtra("langTo", spinnerTo.getSelectedItemPosition());
                startActivity(intent);

                return false;
            }
        });



        return root;
    }

    private void SpinnerOnItemSelected(Spinner spn, String homeSpinner, Spinner spn2) {
        int selVal = spn.getSelectedItemPosition();
        int selVal2 = spn2.getSelectedItemPosition();

        //Swap values if same
//        if (selVal == selVal2) {
//            selVal = selVal2;
//            selVal2 = getActivity().getSharedPreferences(homeSpinner,0).getInt(homeSpinner, 0);
//            spn.setSelection(selVal);
//            spn2.setSelection(selVal2);
//        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences(homeSpinner,0);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.clear();
        prefEditor.putInt(homeSpinner, selVal);
        prefEditor.putBoolean("firstRun", false);
        prefEditor.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}