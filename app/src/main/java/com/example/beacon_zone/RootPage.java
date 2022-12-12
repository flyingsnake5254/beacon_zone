package com.example.beacon_zone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RootPage extends AppCompatActivity {

    Button bRestSutdent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_page);

        setTitle("Root Version");




        bRestSutdent = (Button) findViewById(R.id.b_rest_student);
        bRestSutdent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("account");
                for (int i = 15 ; i <= 19 ; i ++){
                    for ( int j = 1 ; j <= 5 ; j ++){
                        for (int k = 1 ; k <= 9 ; k ++){
                            databaseReference.child("student1").child("sign").child(String.valueOf(i)).child(String.valueOf(j)).child(String.valueOf(k)).setValue("null");
                        }
                    }
                }
            }
        });
    }


}