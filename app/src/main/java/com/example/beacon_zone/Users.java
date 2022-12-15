package com.example.beacon_zone;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Users {
    private ArrayList<User> users;
    private Context context;
    public Users(Context context){
        this.context = context;
        users = new ArrayList<>();
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

                            users.add(new User(name, account, password, permission, email));
                        }
                    }
                }
                else{
                    Toast.makeText(context, "fail to connect database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
