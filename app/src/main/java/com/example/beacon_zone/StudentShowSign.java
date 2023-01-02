package com.example.beacon_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentShowSign extends AppCompatActivity {

    Spinner spinner, spinner2;
    ScrollView scrollView;
    Button bSearch;
    String[] class_time = Variable.class_time;
    String[] w1 = Variable.w1;
    String[] w2 = Variable.w2;
    String[] w3 = Variable.w3;
    String[] w4 = Variable.w4;
    String[] w5 = Variable.w5;

    String[][] dates = Variable.dates;
    ArrayList<String[]> W;

    String[] times = Variable.times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_show_sign);
        setTitle("顯示出缺勤紀錄");

        W = new ArrayList<>();
        W.add(null);
        W.add(w1);
        W.add(w2);
        W.add(w3);
        W.add(w4);
        W.add(w5);

        spinner = (Spinner) findViewById(R.id.sp_week);
        spinner2 = (Spinner) findViewById(R.id.sp_week2);
        scrollView = (ScrollView) findViewById(R.id.sc_show_sign);
        bSearch = (Button) findViewById(R.id.bSearch);
        scrollView.setVisibility(View.INVISIBLE);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(StudentShowSign.this, R.array.show_sign_week, android.R.layout.simple_dropdown_item_1line);
        ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(StudentShowSign.this, R.array.show_sign_week2, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter2);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String week = "15";
                String week2 = "1";
                String selectItem = spinner.getSelectedItem().toString();
                String selectItem2 = spinner2.getSelectedItem().toString();
                selectItem = selectItem.replaceAll("第", "");
                week = selectItem.replaceAll("週", "");
                week2 = selectItem2.replaceAll("禮拜", "");
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("account").child("student1").child("sign").child(week).child(week2);
                
                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            String selectItem = spinner.getSelectedItem().toString();
                            String selectItem2 = spinner2.getSelectedItem().toString();
                            selectItem = selectItem.replaceAll("第", "");
                            selectItem = selectItem.replaceAll("週", "");
                            selectItem2 = selectItem2.replaceAll("禮拜", "");

                            TextView t1 = (TextView) findViewById(R.id.c1);
                            TextView t2 = (TextView) findViewById(R.id.c2);
                            TextView t3 = (TextView) findViewById(R.id.c3);
                            TextView t4 = (TextView) findViewById(R.id.c4);
                            TextView t5 = (TextView) findViewById(R.id.c5);
                            TextView t6 = (TextView) findViewById(R.id.c6);
                            TextView t7 = (TextView) findViewById(R.id.c7);
                            TextView t8 = (TextView) findViewById(R.id.c8);
                            TextView t9 = (TextView) findViewById(R.id.c9);
                            ArrayList<TextView> textViews = new ArrayList<>();
                            textViews.add(null);
                            textViews.add(t1);
                            textViews.add(t2);
                            textViews.add(t3);
                            textViews.add(t4);
                            textViews.add(t5);
                            textViews.add(t6);
                            textViews.add(t7);
                            textViews.add(t8);
                            textViews.add(t9);
                            ArrayList<String> state = new ArrayList<>();
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            state.add("null");
                            for (DataSnapshot ds : task.getResult().getChildren()){
                                state.set(Integer.parseInt(ds.getKey()), ds.getValue().toString());
                            }

                            for (int i = 1; i <=9 ; i ++){

                                if (W.get(Integer.parseInt(selectItem2))[i].equals("    無課程")){
                                    textViews.get(i).setText(class_time[i] + W.get(Integer.parseInt(selectItem2))[i]);
                                    textViews.get(i).setTextColor(Color.LTGRAY);
                                }
                                else{
                                    if (state.get(i).equals("null")){
                                        Date d1 = new Date(); // now time
                                        Date d2 = new Date(); // target time
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String s1 = simpleDateFormat.format(d1);
//                                            s1 = "2022-12-12 23:00:00";
                                        String s2 = dates[Integer.parseInt(selectItem)][Integer.parseInt(selectItem2)] + " " + times[i];

                                        try {
                                            d1 = simpleDateFormat.parse(s1);
                                            d2 = simpleDateFormat.parse(s2);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            System.out.println(e.toString());
                                        }
                                        if (d1.getTime() > d2.getTime()){
                                            textViews.get(i).setText(class_time[i] + W.get(Integer.parseInt(selectItem2))[i] + "缺席");
                                            textViews.get(i).setTextColor(Color.RED);
                                        }
                                        else{
                                            textViews.get(i).setText(class_time[i] + W.get(Integer.parseInt(selectItem2))[i] + "尚未簽到");
                                            textViews.get(i).setTextColor(Color.LTGRAY);
                                        }
                                    }
                                    else if (state.get(i).equals("false")){
                                        textViews.get(i).setText(class_time[i] + W.get(Integer.parseInt(selectItem2))[i] + "缺席");
                                        textViews.get(i).setTextColor(Color.RED);
                                    }
                                    else{
                                        textViews.get(i).setText(class_time[i] + W.get(Integer.parseInt(selectItem2))[i] + state.get(i));
                                        textViews.get(i).setTextColor(Color.GREEN);
                                    }
                                }
                            }
                            scrollView.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(StudentShowSign.this, "fail to connect database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}