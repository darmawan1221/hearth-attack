package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class Register extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final SharedPrefManager mPref = new SharedPrefManager(this);

        Button btn = (Button)findViewById(R.id.btn_next);
        final EditText age = (EditText) findViewById(R.id.umur);
        final EditText name = (EditText) findViewById(R.id.nama);
        final Spinner marriage = (Spinner) findViewById(R.id.spinner2);
        final DatePicker date = (DatePicker) findViewById(R.id.date);
        final Spinner gender = (Spinner) findViewById(R.id.spinner);
        final EditText nik = (EditText) findViewById(R.id.nik);
        date.setMaxDate(System.currentTimeMillis());
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Register_con regis = new Register_con(
                        age.getText().toString(),
                        name.getText().toString(),
                        marriage.getSelectedItem().toString(),
                        date.getYear()+"-"+date.getMonth()+"-"+date.getDayOfMonth(),
                        "",gender.getSelectedItem().toString(),nik.getText().toString());
                mPref.userLogin(regis);
                startActivity(new Intent(Register.this, Category_Main.class));
                finish();
            }
        });
    }
}