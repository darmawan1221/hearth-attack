package com.example.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Tekanan extends AppCompatActivity {

    private String TAG = PerokokMain.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    SharedPreferences sharedpreferences;

    // URL to get contacts JSON
    private static String url = "http://www.limagangsal.com/project/ppns_sickness/api/get/question";
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> tekananList;
    ArrayList checkList;
    Intent intent;
    SharedPrefManager mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_perokok);
        mPref = new SharedPrefManager(this);
        tekananList = new ArrayList<>();
        checkList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        intent = new Intent(Tekanan.this,Recomend.class);

        final CheckBox check = (CheckBox)findViewById(R.id.checkBox);

        Button btn = (Button)findViewById(R.id.btn_save);
        sharedpreferences = getSharedPreferences("send", MODE_PRIVATE);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList list = ((CheckboxAdapter) adapter).getCheckedQuestion();
                mPref.save_array("blood pressure",list);
                Toast.makeText(getApplicationContext(), "TERSIMPAN "+mPref.get_array("blood pressure").toString(), Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        new GetContacts().execute();

    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Tekanan.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        if(c.getString("description").equalsIgnoreCase("tekanan darah")){
                            String id = c.getString("id");
                            String name = c.getString("name");
                            String description = c.getString("description");

                            // Phone node is JSON Object
                            JSONArray questions_arr = c.getJSONArray("questions");
                            for (int j = 0; j < questions_arr.length(); j++) {
                                JSONObject questions = questions_arr.getJSONObject(j);
                                String id2 = questions.getString("id");
                                String title = questions.getString("title");
                                String codex = questions.getString("codex");
                                String question = questions.getString("question");
                                // tmp hash map for single contact
                                HashMap<String, String> response = new HashMap<>();
                                // adding each child node to HashMap key => value
                                response.put("id", id);
                                response.put("id2", id2);
                                response.put("name", name);
                                response.put("description", description);
                                response.put("question", question);
                                checkList.add(id2);
                                // adding contact to contact list
                                tekananList.add(response);
                            }
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            adapter = new CheckboxAdapter(
                    Tekanan.this,
                    tekananList,
                    R.layout.perokok_list, new String[]{"description",
                    "question"}, new int[]{R.id.description,
                    R.id.question}
            );

            lv.setAdapter(adapter);
        }

    }
}