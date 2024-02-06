package com.example.myapplication.ui.translation_result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.FragmentTranslaionResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TranslationResultFragment extends Fragment {
    private FragmentTranslaionResultBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TranslationResultViewModel translationResultViewModel =
                new ViewModelProvider(this).get(TranslationResultViewModel.class);

        binding = FragmentTranslaionResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.wordComp;
        translationResultViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        LinearLayout ll = (LinearLayout) binding.layoutTranslationHm;

        TextView tv = new TextView(getActivity());
        tv.setText("");
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ll.addView(tv);

//        android:text="sfdsdf"
//        android:id="@+id/text_gallery"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginStart="8dp"
//        android:layout_marginTop="8dp"
//        android:layout_marginEnd="8dp"
//        android:textAlignment="center"
//        android:textSize="20sp"
//        app:layout_constraintBottom_toBottomOf="parent"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toTopOf="parent"

        Button btn = binding.button3;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParseFile(tv);
            }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    private JsonObjectRequest JSONOnjReq(String json) {
//        return new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
//    }

    private void jsonParse(TextView tv) {
        String url = "http://10.0.2.2:80/!andr/sth.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("words");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String firstName = employee.getString("word_comp");
                        int age = employee.getInt("word_id");
                        String mail = employee.getString("word_definition");
                        tv.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }



    private void jsonParseFile(TextView tv) {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("words.json")));
            stringBuilder = new StringBuilder();

            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                stringBuilder.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            JSONObject response = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = response.getJSONArray("words");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject employee = jsonArray.getJSONObject(i);
                String firstName = employee.getString("word_comp");
                int age = employee.getInt("word_id");
                String mail = employee.getString("word_definition");
                tv.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
