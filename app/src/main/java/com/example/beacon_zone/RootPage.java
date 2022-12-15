package com.example.beacon_zone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RootPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_page);

        setTitle("Root Version");
    }
}