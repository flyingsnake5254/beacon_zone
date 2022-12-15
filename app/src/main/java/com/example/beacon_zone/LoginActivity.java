package com.example.beacon_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登入");

        bLogin = (Button) findViewById(R.id.b_login);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("click");

                ArrayList<User> users = new ArrayList<>();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("account");
                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            String name = "", account = "", password = "", permission = "", email = "";
                            for (DataSnapshot ds : task.getResult().getChildren()){
                                account = ds.getKey();
                                for (DataSnapshot ds2 : ds.getChildren()){
                                    if (ds2.getKey().equals("password"))
                                        password = ds2.getValue().toString();
                                    else if (ds2.getKey().equals("permission"))
                                        permission = ds2.getValue().toString();
                                    else if (ds2.getKey().equals("name"))
                                        name = ds2.getValue().toString();
                                    else if (ds2.getKey().equals("email")){
                                        email = ds2.getValue().toString();
                                        email = email.replaceAll("\\*", ".");
                                    }
                                }
                                users.add(new User(name, account, password, permission, email));
                            }

                            String sAccount = "" , sPassword = "", sName = "", sPermission = "", sEmail = "";
                            User user;
                            EditText eEmail = (EditText) findViewById(R.id.e_email);
                            EditText ePassword = (EditText) findViewById(R.id.e_password);
                            sAccount = eEmail.getText().toString();
                            sPassword = ePassword.getText().toString();


                            boolean loginSuccess = false;
                            STOP:
                            for (User u : users){
                                System.out.println("sAccount : " + sAccount + " sPassword : " + sPassword);
                                System.out.println("user account : " + u.getAccount() + " user password : " + u.getPassword());
                                if (u.getAccount().equals(sAccount) && u.getPassword().equals(sPassword)){
                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!correct");
                                    loginSuccess = true;
                                    sName = u.getName();
                                    sPermission = u.getPermission();
                                    sEmail = u.getEmail();
                                    System.out.println(sAccount+" "+sPassword+" "+sName+" "+sPermission+" "+sEmail);
                                    Intent intent;
                                    if (sPermission.equals("root"))
                                        intent = new Intent(LoginActivity.this, RootPage.class);
                                    else if(sPermission.equals("teacher"))
                                        intent = new Intent(LoginActivity.this, TeacherPage.class);
                                    else
                                        intent = new Intent(LoginActivity.this, StudentPage.class);

                                    intent.putExtra("account", sAccount);
                                    intent.putExtra("password", sPassword);
                                    intent.putExtra("name", sName);
                                    intent.putExtra("email", sEmail);
                                    intent.putExtra("permission", sPermission);
                                    startActivity(intent);

                                    finish();
                                    break STOP;
                                }
                            }

                            if (!loginSuccess)
                                Toast.makeText(LoginActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();


                        }
                        else{
                            Toast.makeText(LoginActivity.this, "fail to connect database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}