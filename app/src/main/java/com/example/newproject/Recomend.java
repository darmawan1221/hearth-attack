package com.example.newproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class Recomend extends AppCompatActivity {
    SharedPreferences prf;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomend);
        SharedPrefManager mPref = new SharedPrefManager(this);

        TextView result = (TextView)findViewById(R.id.resultView);
        Button btnLogOut = (Button)findViewById(R.id.btnLogOut);
        prf = getSharedPreferences("send",MODE_PRIVATE);
        String str = "Tidak ada";
        try {
            JSONObject json = new JSONObject(mPref.get_string("result"));
            str = json.getString("recomendation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT));
        }
//        result.setText(Html.fromHtml(str));
//        result.setText(str);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Recomend.this, Register.class));
                finish();
            }
        });
    }

}
