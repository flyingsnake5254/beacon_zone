package com.example.beacon_zone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherPage extends AppCompatActivity {

    Button bAbsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_page);

        setTitle("Teacher Version");
        bAbsent = (Button) findViewById(R.id.bAbsent);
        bAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherPage.this, TeacherShowAbsent.class));
            }
        });
    }
}