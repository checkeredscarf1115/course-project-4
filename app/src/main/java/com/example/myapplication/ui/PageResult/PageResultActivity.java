package com.example.myapplication.ui.PageResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.databinding.ActivityPageResultBinding;

public class PageResultActivity extends AppCompatActivity {
    private ActivityPageResultBinding binding;
    //private PageResultViewModel viewModel;
    //private List<List<List<String>>> res = new ArrayList<>();
    private PageResultAdapter adapter;
//    List<List<String>> meaningList;
//    List<List<String>> synsList;
//    List<List<String>> antsList;
//    List<List<String>> contextList;
//    List<Integer> ids;
//    List<String> defs;
    private enum DictionaryLanguages {
        EN,
        RU,
    }
    //int countRequests;
    private RecyclerView rv;
    private ResultRepository repository;
    private String dictFrom, dictTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPageResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //viewModel = new ViewModelProvider(this).get(PageResultViewModel.class);

        //Get intent extras
        String word = getIntent().getStringExtra("word");
        int langFrom = getIntent().getIntExtra("langFrom", 0);
        int langTo = getIntent().getIntExtra("langTo", 1);

        //Choose databases
        dictFrom = GetDatabaseName(DictionaryLanguages.values()[langFrom]);
        dictTo = GetDatabaseName(DictionaryLanguages.values()[langTo]);

        //Set for meaning textview
        TextView tvOrigWord = binding.contentPageResult.textviewOrigWord;
        tvOrigWord.setText(word);

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        //Get layout and parse
        LinearLayout ll = binding.contentPageResult.linLayout;
        //jsonParse(this, ll, word);



//



//        List<List<String>> meaningList = (List<List<String>>) getIntent().getBundleExtra("extra").getSerializable("meaning");
//        List<List<String>> synsList = (List<List<String>>) getIntent().getBundleExtra("extra").getSerializable("syns");
//        List<List<String>> antsList = (List<List<String>>) getIntent().getBundleExtra("extra").getSerializable("ants");
//        List<List<String>> contextList = (List<List<String>>) getIntent().getBundleExtra("extra").getSerializable("contexts");

//        List<List<String>> meaningList = (List<List<String>>) getIntent().getSerializableExtra("meaning");
//        List<List<String>> synsList = (List<List<String>>) getIntent().getSerializableExtra("syns");
//        List<List<String>> antsList = (List<List<String>>) getIntent().getSerializableExtra("ants");
//        List<List<String>> contextList = (List<List<String>>) getIntent().getSerializableExtra("contexts");



//        if (res.size() > 0) {
//            for (int i = 0; i < ((res.size() - 1) / res.get(0).size()); i++) {
//                meaningList.get(i).add(res.get(i).get(i).get(0));
//                synsList.get(i + 1).add(res.get(i + 1).get(i).get(0));
//                antsList.get(i + 2).add(res.get(i + 2).get(i).get(0));
//                contextList.get(i + 3).add(res.get(i + 3).get(i).get(0));
//            }



//        meaningList = new ArrayList<>();
//        synsList = new ArrayList<>();
//        antsList = new ArrayList<>();
//        contextList = new ArrayList<>();
//        ids = new ArrayList<>();
//
//        String URL = BuildURL("dict_en", "SELECT word_id, word_definition FROM words WHERE word_id IN (SELECT word_id FROM words WHERE word_comp = '" + word + "');");
//        JSONParse123(URL);
//
        repository = new ResultRepository();
        repository.setCompFrom(word);
        rv = binding.contentPageResult.pageresultRecyclerview;
        //adapter = new PageResultAdapter(meaningList, synsList, antsList, contextList, this);
        //adapter = new PageResultAdapter(repository.getMeaningList(), repository.getSynsList(), repository.getAntsList(), repository.getContextList(), this, langTo);
        adapter = new PageResultAdapter(this, langTo, repository);
        rv.setAdapter(adapter);

        ImageViewBookmarkLogic imageViewBookmarkLogic = new ImageViewBookmarkLogic(binding.bookmark, repository, tvOrigWord.getText().toString(), GetLanguageName(DictionaryLanguages.values()[langFrom]), GetLanguageName(DictionaryLanguages.values()[langTo]));
        binding.bookmark.setOnClickListener(imageViewBookmarkLogic.getListener());

        CallBackVolley callBackVolley = new CallBackVolley() {
            @Override
            public void onEmpty() {
                Toast toast = Toast.makeText(binding.getRoot().getContext(),
                        "Oops... it appears there's no such word in our database",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }

            @Override
            public void OnCountMax(ResultRepository repository) {
                //rv = binding.contentPageResult.pageresultRecyclerview;
                //rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.i("adapter", "adapter notified");

                //Change the star's appearance
                imageViewBookmarkLogic.Execute();

            }
        };

//        VolleyManager volleyManager = VolleyManager.getInstance(this, callBackVolley, repository);
        VolleyManager volleyManager = new VolleyManager(this, callBackVolley, repository);
        String URL;

        if (langTo != langFrom) {
            URL = volleyManager.BuildURL(dictFrom, "SELECT other_word_id, word_definition FROM " + GetEquivs(DictionaryLanguages.values()[langTo]) + " JOIN words ON word_id=this_word_id WHERE this_word_id IN (SELECT word_id FROM words WHERE word_comp = '" + word + "');");
            volleyManager.StartJSONParseDiffLangs(URL, dictTo);
        }
        else {
            URL = volleyManager.BuildURL(dictTo, "SELECT word_id, word_definition, word_comp FROM words WHERE word_comp = '" + word + "';");
            volleyManager.StartJSONParseSameLang(URL, dictTo, new VolleyCallBackSameLang() {
                @Override
                public void onSuccess() {

                }
            });
        }
    }

    private String GetLanguageName(DictionaryLanguages lang) {
        switch (lang) {
            case EN: {
                return "English";
            }
            case RU: {
                return "Русский";
            }
            default: {
                return null;
            }
        }
    }

    private String GetEquivs(DictionaryLanguages lang) {
        switch (lang) {
            case EN: {
                return "equivs_en";
            }
            case RU: {
                return "equivs_ru";
            }
            default: {
                return null;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

//    private String getFileContent(String fileName) {
//        BufferedReader bufferedReader = null;
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
//            stringBuilder = new StringBuilder();
//
//            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
//                stringBuilder.append(line);
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return stringBuilder.toString();
//    }
//
//    private void jsonParseFile(LinearLayout ll, String word) {
//        try {
//        String words = getFileContent("words.json");
//
////        String contexts = getFileContent("contexts.json");
////        JSONObject responseContexts = new JSONObject(contexts);
////        JSONArray jsonArrayContexts = responseContexts.getJSONArray("contexts");
////        JSONObject JSONcontext;
////
////        String ants = getFileContent("ants.json");
////        JSONObject responseAnts = new JSONObject(ants);
////        JSONArray jsonArrayAnts = responseAnts.getJSONArray("ants");
////        JSONObject JSONant;
////
////        String syns = getFileContent("syns.json");
////        JSONObject responseSyns = new JSONObject(syns);
////        JSONArray jsonArraySyns = responseSyns.getJSONArray("syns");
////        JSONObject JSONsyn;
//
//
//
//
//
//            binding.contentPageResult.textviewMeaning.setText(word);
//            ContextThemeWrapper contextLine = new ContextThemeWrapper(this, R.style.line_under_item);
////            TextView viewM = new TextView(context);
////            ll.addView(viewM);
//            ContextThemeWrapper contextTextDF = new ContextThemeWrapper(this, R.style.text_df_margin);
//            ContextThemeWrapper contextTextBold = new ContextThemeWrapper(this, R.style.text_bold);
//
//            JSONObject responseWords = new JSONObject(words);
//            JSONArray jsonArrayWords = responseWords.getJSONArray("words");
//            JSONObject JSONword = new JSONObject();
//            int wordID = 0;
//            String wordComp = "";
//            String wordDef = "";
//            int number = 0;
//            //tv.append(String.format("%s", word));
//            for (int i = 0; i < jsonArrayWords.length(); i++) {
//                JSONword = jsonArrayWords.getJSONObject(i);
//                if ((wordComp = JSONword.getString("word_comp")).equals(word)) {
//                    TextView tv = new TextView(contextTextDF);
//                    ll.addView(tv);
//
//                    //wordComp = JSONword.getString("word_comp");
//                    wordID = JSONword.getInt("word_id");
//                    wordDef = JSONword.getString("word_definition");
//                    number++;
//
//
//
//                    tv.append(String.format("%s. %s\n", number, wordDef));
//
//                    TextView view = new TextView(contextLine);
//                    ll.addView(view);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void jsonParse(PageResultActivity activity, LinearLayout ll, String word) {
//        ContextThemeWrapper contextLine = new ContextThemeWrapper(activity, R.style.line_under_item);
//        ContextThemeWrapper contextTextDF = new ContextThemeWrapper(activity, R.style.text_df_margin);
//        binding.contentPageResult.textviewMeaning.setText(word);
//
//        String url = "http://10.0.2.2:80/!andr/get_words_23.php?table=words&where=word_comp&arg="+ word;
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("words");
//
//                    if (jsonArray.length() <= 0) {
//                        TextView textView = new TextView(activity);
//                        textView.setText("Ooops... it appears there is no such word in our database");
//                        textView.setGravity(Gravity.CENTER);
//                        ll.addView(textView);
//                        return;
//                    }
//
//                    int wordID = 0;
//                    String wordComp = "";
//                    String wordDef = "";
//                    int number = 0;
//                    JSONObject JSONword;
//                    TextView tv;
//                    TextView view;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        tv = new TextView(contextTextDF);
//                        ll.addView(tv);
//
//                        JSONword = jsonArray.getJSONObject(i);
//
//                        wordID = JSONword.getInt("word_id");
//                        wordDef = JSONword.getString("word_definition");
//                        number++;
//
//                        viewModel.getWords(wordDef, number).observe(activity, tv::setText);
//                        //tv.append(String.format("%s. %s\nWahahah", number, wordDef));
//
//                        //Layout for horizontal appearance
//                        LinearLayout ll2 = new LinearLayout(activity);
//                        ll2.setOrientation(LinearLayout.HORIZONTAL);
//                        ll.addView(ll2);
//
//                        //Synonyms hor
//                        TextView tvSyn = new TextView(contextTextDF);
//                        tvSyn.setText("synonyms:");
//                        ll2.addView(tvSyn);
//                        JSONParseIntoButtons(activity, ll2, wordID, "syns", "syn1_id", "syn2_id");
//
//                        //Layout for horizontal appearance
//                        LinearLayout ll3 = new LinearLayout(activity);
//                        ll3.setOrientation(LinearLayout.HORIZONTAL);
//                        ll.addView(ll3);
//
//                        //Antonyms hor
//                        TextView tvAnt = new TextView(contextTextDF);
//                        tvAnt.setText("antonyms:");
//                        ll3.addView(tvAnt);
//                        JSONParseIntoButtons(activity, ll3, wordID, "ants", "ant1_id", "ant2_id");
//
//                        //Context
//                        TextView tvContexts = new TextView(contextTextDF);
//                        ll.addView(tvContexts);
//                        tvContexts.setText("contexts:\n");
//                        JSONParseIntoSingleView(activity, tvContexts, wordID);
//
//                        //Delimiter between meanings
//                        view = new TextView(contextLine);
//                        ll.addView(view);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(request);
//    }
//
//    private void JSONParseIntoSingleView(PageResultActivity activity, TextView tvContexts, int wordID) {
//        ContextThemeWrapper contextLine = new ContextThemeWrapper(activity, R.style.line_under_item);
//        ContextThemeWrapper contextTextDF = new ContextThemeWrapper(activity, R.style.text_df_margin);
//
//        String url = "http://10.0.2.2:80/!andr/get_words_23.php?table=contexts&where=word_id&arg=" + wordID;
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("contexts");
//                    JSONObject JSONcontext;
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONcontext = jsonArray.getJSONObject(i);
//                        tvContexts.append(JSONcontext.getString("example"));
//                        if (i < jsonArray.length() - 1) {
//                            tvContexts.append("\n");
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(request);
//    }
//
//    private void JSONParseIntoButtons(PageResultActivity activity, LinearLayout ll2, int wordID, String tableName, String criteriaID1, String criteriaID2) {
//        ContextThemeWrapper contextLine = new ContextThemeWrapper(activity, R.style.line_under_item);
//        ContextThemeWrapper contextTextDF = new ContextThemeWrapper(activity, R.style.text_df_margin);
//
//        String url = BuildURL("dictionary", tableName, criteriaID1, Integer.toString(wordID));
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray(tableName);
//                    //int param1 = 0;
//                    int param2 = 0;
//
//                    JSONObject JSONword;
//                    Button btn;
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        btn = new Button(contextTextDF);
//                        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                        ll2.addView(btn);
//
//                        JSONword = jsonArray.getJSONObject(i);
//
//                        param2 = JSONword.getInt(criteriaID2);
//
//                        //Format the button and make it call intent
//                        JSONParseWordsWordID(activity, btn, param2);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(request);
//    }
//
//    private void JSONParseWordsWordID(PageResultActivity activity, Button btn, int wordID) {
//        ContextThemeWrapper contextLine = new ContextThemeWrapper(activity, R.style.line_under_item);
//        ContextThemeWrapper contextTextDF = new ContextThemeWrapper(activity, R.style.text_df_margin);
//
//        String url = BuildURL("dictionary","words", "word_id", Integer.toString(wordID));
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("words");
//                    JSONObject JSONword;
//                    String wordComp = "";
//                        JSONword = jsonArray.getJSONObject(0);
//                        wordComp = JSONword.getString("word_comp");
//                        btn.setTransformationMethod(null);
//                        btn.setText(wordComp);
//
//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(activity, PageResultActivity.class);
//                            intent.putExtra("word", btn.getText().toString());
//                            startActivity(intent);
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(request);
//    }

//    private void JSONParse(String URL) {
//
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray array = response.getJSONArray("array");
////                    Iterator x = array.;
////                    JSONArray arrays = new JSONArray();
////
////                    while (x.hasNext()) {
////                        String key = (String) x.next();
////                        arrays.put(array.get(key));
////                    }
//
//                    //JSONArray jsonArrayWordIDs = array.getJSONArray(0);
//                    JSONObject objsadsadas = array.getJSONObject(0);
//                    JSONArray jsonArrayWordIDs = objsadsadas.getJSONArray(GetArrayNameWord());
//                    JSONObject jsonWordID;
//                    JSONObject jsonArray;
//                    JSONObject jsonObject;
//                    int i, j, k = 0;
//
//                    for (i = 0; i < jsonArrayWordIDs.length(); i++) {
//                        res.add(new ArrayList<>());
//                        for (j = 0; j < GetArrayNames().size(); j++) {
//                            res.get(i).add(new ArrayList<>());
//                        }
//                    }
//
//                    for (i = 0; i < jsonArrayWordIDs.length(); i++) {
//                        jsonWordID = jsonArrayWordIDs.getJSONObject(i);
//                        int wordID = jsonWordID.getInt("word_id");
//
//                        for (j = 0; j < GetArrayNames().size(); j++) {
//                            //jsonArray = response.getJSONArray(GetArrayNames().get(j) + "_" + wordID);
//                            //jsonArray = array.getJSONArray(j + 1);
//                            jsonArray = array.getJSONObject(i + j + 1);
//                            JSONArray jsonArray1 = jsonArray.getJSONArray(GetArrayNames().get(j) + "_" + wordID);
//
//                            for (k = 0; k < GetItemNames().size(); k++) {
//
//                                jsonObject = jsonArray1.getJSONObject(k);
//                                Object val = jsonObject.get(GetItemNames().get(k));
//
//                                if (val.getClass().getName() == Integer.class.getName()) {
//                                    int val2 = (Integer) val;
//                                    res.get(j).get(k).add(Integer.toString(val2));
//                                } else if (val.getClass().getName() == String.class.getName()) {
//                                    String val2 = (String) val;
//                                    res.get(j).get(k).add(val2);
//                                }
//                            }
//                        }
//                    }
//                    //adapter.notifyItemRangeInserted();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//
//
//    }

//    private void JSONParse123(String URL) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("array");
//                    List<String> wordDefs = new ArrayList<>();
//                    List<Integer> wordIDs = new ArrayList<>();
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        wordDefs.add(jsonObject.getString("word_definition"));
//                        wordIDs.add(jsonObject.getInt("word_id"));
//                        meaningList.add(new ArrayList<>());
//                        meaningList.get(i).add(jsonObject.getString("word_definition"));
//                    }
//
//                    if (meaningList.size() == 0) {
//                        //TextView textView = new TextView(inflater.getContext());
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                        "Oops... it appears there's no such word in our database",
//                        Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                        return;
//                    }
//
//                    defs = wordDefs;
//
//                    String idsToString = wordIDs.stream().map(Object::toString)
//                                .collect(Collectors.joining(", "));
//
//                    //initialize arrays so we can replace them instead of adding -- volley is async
//                    for (int i = 0; i < wordIDs.size(); i++) {
//                        synsList.add(new ArrayList<>());
//                        antsList.add(new ArrayList<>());
//                        contextList.add(new ArrayList<>());
//                    }
//
//                    //count until all requests finish, then update adapter
//                    countRequests = 0;
//                    String url;
//                    for (int i = 0; i < wordIDs.size(); i++) {
//                        url = BuildURL("dict_en", "SELECT word_comp, word_id, syn1_id FROM words " +
//                                "JOIN syns ON syn2_id=word_id WHERE syn1_id = " +
//                                wordIDs.get(i) + ";");
//                        JSONParse12345(url, 1, "word_comp", i, new VolleyCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                //adapter.notifyItemInserted(pos);
//                                countRequests++;
//                                if (countRequests == wordIDs.size() * 3) {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//                    }
//
//                    for (int i = 0; i < wordIDs.size(); i++) {
//                        url = BuildURL("dict_en", "SELECT word_comp, word_id, ant1_id FROM words " +
//                                "JOIN ants ON ant2_id=word_id WHERE ant1_id = " +
//                                wordIDs.get(i) + ";");
//                        JSONParse12345(url, 2, "word_comp", i, new VolleyCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                //adapter.notifyItemInserted(pos);
//                                countRequests++;
//                                if (countRequests == wordIDs.size() * 3) {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//                    }
//                    for (int i = 0; i < wordIDs.size(); i++) {
//                        url = BuildURL("dict_en", "SELECT example FROM contexts WHERE word_id IN (" +
//                                "SELECT word_id FROM words WHERE word_id = " +
//                                wordIDs.get(i) + ");");
//
//                        JSONParse12345(url, 3, "example", i, new VolleyCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                //adapter.notifyItemInserted(pos);
//                                countRequests++;
//                                if (countRequests == wordIDs.size() * 3) {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }
//
//    private void JSONParse12345(String URL, int array, String valueName, int position, VolleyCallBack callBack) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                List<String> list = new ArrayList<>();
//                try {
//                    JSONArray jsonArray = response.getJSONArray("array");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        list.add(jsonObject.getString(valueName));
//                    }
//
////                    for (int i = 0; i < list.size(); i++) {
////                        for (int j = 0; j < list.size(); j++) {
////                            if (list.get(j).equals(list.get(i)) && i != j) {
////                                list.remove(j);
////                            }
////                        }
////                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                    switch (array) {
//                        case 1: {
//                            synsList.set(position, list);
//                            callBack.onSuccess();
//                            break;
//                        }
//                        case 2: {
//                            antsList.set(position, list);
//                            callBack.onSuccess();
//                            break;
//                        }
//                        case 3: {
//                            contextList.set(position, list);
//                            callBack.onSuccess();
//                            break;
//                        }
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }
//
//    private String BuildURL(String Database, String SQL) {
//        return "http://10.0.2.2:80/!andr/get_vs.php?db=" + Database + "&sql=" + SQL;
//    }

    private String GetDatabaseName(DictionaryLanguages lang) {
        switch (lang) {
            case EN: {
                return "dict_en";
            }
            case RU: {
                return "dict_ru";
            }
            default: {
                return null;
            }
        }
    }
}