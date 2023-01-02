package com.example.beacon_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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

public class ShowAbsent extends AppCompatActivity {

    TextView tAbsent;

    String[] w1 = Variable.w1;
    String[] w2 = Variable.w2;
    String[] w3 = Variable.w3;
    String[] w4 = Variable.w4;
    String[] w5 = Variable.w5;
    String[][] dates = Variable.dates;
    String[] times = Variable.times;
    ArrayList<String[]> W;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_absent);

        setTitle("顯示缺席紀錄");
        W = new ArrayList<>();
        W.add(null);
        W.add(w1);
        W.add(w2);
        W.add(w3);
        W.add(w4);
        W.add(w5);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("account").child("student1").child("sign");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    // now time
                    Date dNow = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sNow = simpleDateFormat.format(dNow);

                    // get state
                    String[][][] states = Variable.statesReset;

                    for (DataSnapshot ds : task.getResult().getChildren()){
                        for (DataSnapshot ds2 : ds.getChildren()){
                            for (DataSnapshot ds3 : ds2.getChildren()){
                                String week1 = ds.getKey(); // 周次
                                String week2 = ds2.getKey(); // 禮拜幾
                                String section = ds3.getKey(); // 第幾節
                                states[Integer.parseInt(week1)][Integer.parseInt(week2)][Integer.parseInt(section)] = ds3.getValue().toString();
                            }
                        }
                    }

                    for (int i = 15 ; i <= 19 ; i ++){
                        for (int j = 1 ; j <= 5 ; j ++){
                            for (int k = 1 ; k <= 9 ; k ++){
                                if (k == 5)
                                    continue;
                                if (states[i][j][k].equals("null") && !(W.get(j)[k].equals("    無課程"))){
                                    try {
                                        dNow = simpleDateFormat.parse(sNow);
                                        String s = dates[i][j] + " " + times[k];
                                        Date d1 = new Date();
                                        d1 = simpleDateFormat.parse(s);
                                        if (dNow.getTime() > d1.getTime()){
                                            TextView textView = (TextView) findViewById(R.id.t_absent);
                                            String result = textView.getText().toString();
                                            result += dates[i][j] + " [星期 " + String.valueOf(j) + " ] (第" + String.valueOf(k) + "節)\n";
                                            result += W.get(j)[k] + "缺席\n\n";
                                           textView.setText(result);
                                           textView.setTextColor(Color.RED);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }
                else{
                    Toast.makeText(ShowAbsent.this, "fail to connect database", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}