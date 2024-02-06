package com.example.myapplication.ui.PageResult;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolleyManager {
    //private static VolleyManager INSTANCE;
    private ResultRepository repository;
    public RequestQueue requestQueue;
    private int countRequests = 0;
    private CallBackVolley callBackVolley = null;
    private int countDefs = 0;

    public VolleyManager(Context context, CallBackVolley callBackVolley, ResultRepository repository) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.callBackVolley = callBackVolley;
        this.repository = repository;
    }

//    public static synchronized VolleyManager getInstance(Context context, CallBackVolley callBackVolley, ResultRepository repository) {
//        if (INSTANCE == null) {
//            synchronized (VolleyManager.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new VolleyManager(context, callBackVolley, repository);
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    public static synchronized VolleyManager getInstance() {
//        if (INSTANCE == null) {
//            throw new IllegalStateException(VolleyManager.class.getSimpleName() + "is not initialized, call getInstance(...) first");
//        }
//        return null;
//    }

    public void StartJSONParseDiffLangs(String URL, String dictTo) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("array");
                    List<Integer> wordIDs = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        wordIDs.add(jsonObject.getInt("other_word_id"));
                        repository.getDefsFrom().add(jsonObject.getString("word_definition"));
                        //Log.d("volley_man", jsonObject.getString("word_definition") + jsonObject.getInt("other_word_id"));
                    }

                    if (repository.getDefsFrom().size() == 0) {
                        callBackVolley.onEmpty();
                        return;
                    }


                    String idsToString = wordIDs.stream().map(Object::toString)
                            .collect(Collectors.joining(", "));

                    //count until all requests finish, then update adapter
//                    countRequests = 0;
                    String url;
//                    for (int i = 0; i < wordIDs.size(); i++) {
//                        url = BuildURL(dictTo, "SELECT word_id, word_definition, word_comp FROM words WHERE word_id = " + wordIDs.get(i) + ";");
//                        Log.i("volley_man", "got a word");
//                        JSONParse123(url, dictTo);
//                    }
                    VolleyCallBackSameLang volleyCallBackSameLang = new VolleyCallBackSameLang() {
                        @Override
                        public void onSuccess() {
                            if (countDefs < repository.getDefsFrom().size()) {
                                //Log.d("volley_man", Integer.toString(wordIDs.get(countDefs)));
                                String url = BuildURL(dictTo, "SELECT word_id, word_definition, word_comp FROM words WHERE word_id = " + wordIDs.get(countDefs) + ";");
                                //Log.i("volley_man", "got a word");
                                StartJSONParseSameLang(url, dictTo, this);
                                countDefs++;
                            }
                        }
                    };
                    volleyCallBackSameLang.onSuccess();
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
        requestQueue.add(request);
    }


    public void StartJSONParseSameLang(String URL, String dictTo, VolleyCallBackSameLang volleyCallBackSameLang) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("array");
                    List<Integer> wordIDs = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        wordIDs.add(jsonObject.getInt("word_id"));
                        repository.getDefsTo().add(jsonObject.getString("word_definition"));
                        repository.getCompsTo().add(jsonObject.getString("word_comp"));
                    }

                    if (repository.getDefsTo().size() == 0) {
                        callBackVolley.onEmpty();
                        return;
                    }

                    String idsToString = wordIDs.stream().map(Object::toString)
                            .collect(Collectors.joining(", "));

                    //initialize arrays so we can replace them instead of adding -- volley is async
                    for (int i = 0; i < wordIDs.size(); i++) {
                        repository.getSynsList().add(new ArrayList<>());
                        repository.getAntsList().add(new ArrayList<>());
                        repository.getContextList().add(new ArrayList<>());
                    }
                    volleyCallBackSameLang.onSuccess();
                    //count until all requests finish, then update adapter
                    //countRequests = 0;
                    String url;
                    VolleyCallBackSameLang volleyCallBackSameLang = new VolleyCallBackSameLang() {
                        @Override
                        public void onSuccess() {
                            countRequests++;
                            //Log.d("volley_man", Integer.toString(countRequests));
                            if (countRequests == wordIDs.size() * 3) {
                                //Log.i("volley_man", "notified adapter");
                                callBackVolley.OnCountMax(repository);
                                countRequests = 0;
                            }
                        }
                    };
                    for (int i = 0; i < wordIDs.size(); i++) {
                        url = BuildURL(dictTo, "SELECT word_comp, word_id, syn1_id FROM words " +
                                "JOIN syns ON syn2_id=word_id WHERE syn1_id = " +
                                wordIDs.get(i) + ";");
                        ContinueJSONParse(url, 1, "word_comp", i, volleyCallBackSameLang);
                    }


                    for (int i = 0; i < wordIDs.size(); i++) {
                        url = BuildURL(dictTo, "SELECT word_comp, word_id, ant1_id FROM words " +
                                "JOIN ants ON ant2_id=word_id WHERE ant1_id = " +
                                wordIDs.get(i) + ";");
                        ContinueJSONParse(url, 2, "word_comp", i, volleyCallBackSameLang);
                    }
                    for (int i = 0; i < wordIDs.size(); i++) {
                        url = BuildURL(dictTo, "SELECT example FROM contexts WHERE word_id IN (" +
                                "SELECT word_id FROM words WHERE word_id = " +
                                wordIDs.get(i) + ");");

                        ContinueJSONParse(url, 3, "example", i, volleyCallBackSameLang);
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
        requestQueue.add(request);
    }

        private void ContinueJSONParse(String URL, int array, String valueName, int position, VolleyCallBackSameLang callBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<String> list = new ArrayList<>();
                try {
                    JSONArray jsonArray = response.getJSONArray("array");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list.add(jsonObject.getString(valueName));
                    }

//                    for (int i = 0; i < list.size(); i++) {
//                        for (int j = 0; j < list.size(); j++) {
//                            if (list.get(j).equals(list.get(i)) && i != j) {
//                                list.remove(j);
//                            }
//                        }
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    switch (array) {
                        case 1: {
                            repository.getSynsList().set(position, list);
                            callBack.onSuccess();
                            break;
                        }
                        case 2: {
                            repository.getAntsList().set(position, list);
                            callBack.onSuccess();
                            break;
                        }
                        case 3: {
                            repository.getContextList().set(position, list);
                            callBack.onSuccess();
                            break;
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    public String BuildURL(String Database, String SQL) {
        //return "http://10.0.2.2:80/!andr/get_vs.php?db=" + Database + "&sql=" + SQL;
        //return "http://192.168.0.147/!andr/get_vs.php?db=" + Database + "&sql=" + SQL;
        return "http://192.168.42.163/!andr/get_vs.php?db=" + Database + "&sql=" + SQL;
    }
}
