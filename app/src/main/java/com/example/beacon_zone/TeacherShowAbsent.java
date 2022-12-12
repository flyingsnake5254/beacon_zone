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
    String[] className = {"(001)軟體工程", "(025)微積分"};
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
                    String output = 
                              "2022-12-05 星期1 第2節\n學號：410911111\n姓名：AAA\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-05 星期1 第3節\n學號：410911111\n姓名：AAA\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-05 星期1 第4節\n學號：410911111\n姓名：AAA\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-05 星期1 第2節\n學號：410911122\n姓名：BBB\n系級：軟體四\n出席狀況：缺席\n\n"
                            + "2022-12-05 星期1 第3節\n學號：410911122\n姓名：BBB\n系級：軟體四\n出席狀況：缺席\n\n"
                            + "2022-12-12 星期1 第2節\n學號：410911107\n姓名：CCC\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-12 星期1 第3節\n學號：410911107\n姓名：CCC\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-12 星期1 第2節\n學號：410911108\n姓名：DDD\n系級：軟體四\n出席狀況：缺席\n\n";
                    textView.setText(output);

                }
                else if (spinner.getSelectedItem().toString().equals(className[1])){
                    String output =
                              "2022-12-08 星期4 第6節\n學號：410911101\n姓名：aaa\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-08 星期4 第7節\n學號：410911101\n姓名：aaa\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-08 星期4 第6節\n學號：410911105\n姓名：bbb\n系級：軟體三\n出席狀況：缺席\n\n"
                            + "2022-12-08 星期4 第6節\n學號：410911109\n姓名：ccc\n系級：軟體四\n出席狀況：缺席\n\n"
                            + "2022-12-08 星期4 第6節\n學號：410911122\n姓名：ddd\n系級：軟體四\n出席狀況：缺席\n\n"
                            + "2022-12-08 星期4 第7節\n學號：410911122\n姓名：ddd\n系級：軟體四\n出席狀況：缺席\n\n"
                            + "2022-12-08 星期4 第8節\n學號：410911122\n姓名：ddd\n系級：軟體四\n出席狀況：缺席\n\n"

                            + "2022-12-15 星期4 第6節\n學號：410911108\n姓名：eee\n系級：軟體三\n出席狀況：缺席\n\n"
                          + "2022-12-15 星期4 第6節\n學號：410911111\n姓名：fff\n系級：軟體四\n出席狀況：缺席\n\n"
                          + "2022-12-15 星期4 第7節\n學號：410911111\n姓名：fff\n系級：軟體四\n出席狀況：缺席\n\n"
                          + "2022-12-15 星期4 第6節\n學號：410911133\n姓名：ggg\n系級：軟體四\n出席狀況：缺席\n\n"
                          + "2022-12-15 星期4 第6節\n學號：410911136\n姓名：hhh\n系級：軟體四\n出席狀況：缺席\n\n";
                    textView.setText(output);
                }
            }
        });
    }
}