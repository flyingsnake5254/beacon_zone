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
    String[] class_time = {null, "第一節（8:10~9:00)\n", "第二節（9:10~10:00)\n", "第三節（10:10~11:00)\n"
            , "第四節（11:10~12:00)\n", "第五節\n", "第六節（13:30~14:20)\n", "第七節（14:30~15:20)\n"
            , "第八節（15:30~16:20)\n", "第九節（16:30~17:20)\n"};
    String[] w1 = {null, "    無課程"
            , "    課程名稱：網路行銷\n    老師：張瑞觀\n    出席狀況："
            , "    課程名稱：網路行銷\n    老師：張瑞觀\n    出席狀況："
            , "    課程名稱：網路行銷\n    老師：張瑞觀\n    出席狀況："
            , "    無課程"
            , "    課程名稱：作業系統\n    老師：余遠澤\n    出席狀況："
            , "    課程名稱：作業系統\n    老師：余遠澤\n    出席狀況："
            , "    課程名稱：作業系統\n    老師：余遠澤\n    出席狀況："
            , "    無課程"};
    String[] w2 = {null, "    無課程"
            , "    課程名稱：軟體分析\n    老師：李文廷\n    出席狀況："
            , "    課程名稱：軟體分析\n    老師：李文廷\n    出席狀況："
            , "    課程名稱：軟體分析\n    老師：李文廷\n    出席狀況："
            , "    無課程"
            , "    課程名稱：輔助科技\n    老師：鄭伯壎\n    出席狀況："
            , "    課程名稱：輔助科技\n    老師：鄭伯壎\n    出席狀況："
            , "    課程名稱：輔助科技\n    老師：鄭伯壎\n    出席狀況："
            , "    無課程"};
    String[] w3 = {null, "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程"};
    String[] w4 = {null, "    無課程"
            , "    課程名稱：線性代數\n    老師：葉道明\n    出席狀況："
            , "    課程名稱：線性代數\n    老師：葉道明\n    出席狀況："
            , "    課程名稱：線性代數\n    老師：葉道明\n    出席狀況："
            , "    無課程"
            , "    課程名稱：木工實做\n    老師：王炳文\n    出席狀況："
            , "    課程名稱：木工實做\n    老師：王炳文\n    出席狀況："
            , "    課程名稱：歷史與文化\n    老師：王文裕\n    出席狀況："
            , "    課程名稱：歷史與文化\n    老師：王文裕\n    出席狀況："};
    String[] w5 = {null, "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程", "    無課程"};
    String[][] dates = {
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
            , {null, "2022-12-12", "2022-12-13", "2022-12-14", "2022-12-15", "2022-12-16"}
            , {null, "2022-12-19", "2022-12-20", "2022-12-21", "2022-12-22", "2022-12-23"}
            , {null, "2022-12-26", "2022-12-27", "2022-12-28", "2022-12-29", "2022-12-30"}
            , {null, "2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05", "2023-01-06"}
            , {null, "2023-01-09", "2023-01-10", "2023-01-11", "2023-01-12", "2023-01-13"}
    };

    String[] times = {null, "09:00:00", "10:00:00", "11:00:00", "12:00:00", null, "14:20:00", "15:20:00", "16:20:00", "17:20:00"};
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
                    String[][][] states = {
                            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
                            , {null, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}}
                            , {null, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}}
                            , {null, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}}
                            , {null, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}}
                            , {null, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null, null, null}}
                    };

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