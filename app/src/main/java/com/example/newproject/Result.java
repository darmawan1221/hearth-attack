package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager mPref = new SharedPrefManager(this);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView text = (TextView) findViewById(R.id.hasil_analisa);
        String str = "Tidak ada";
        try {
            JSONObject json = new JSONObject(mPref.get_string("result"));
            str = json.getString("risk");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        text.setText(str);
        Button btn = (Button)findViewById(R.id.btn_recomend);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Result.this, Recomend.class));
                finish();
            }
        });
    }
}
