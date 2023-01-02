package com.example.beacon_zone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class TeacherShowAbsent extends AppCompatActivity {

    Spinner spinner;
    Button bSearch;
    String[] className = Variable.className;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_show_absent);

        spinner = (Spinner) findViewById(R.id.sp_absent);
        bSearch = (Button) findViewById(R.id.b_search);
        textView = (TextView) findViewById(R.id.tAbsent) ;

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.classes, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem().toString().equals(className[0])){
                    String output = Variable.output1;
                    textView.setText(output);

                }
                else if (spinner.getSelectedItem().toString().equals(className[1])){
                    String output = Variable.output2;
                    textView.setText(output);
                }
            }
        });
    }
}