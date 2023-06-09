package com.example.clearmind;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clearmind.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private DatabaseReference db;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.usernameInput = (EditText)(findViewById(R.id.username_input));
        this.passwordInput = (EditText)(findViewById(R.id.password_input));
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        login = findViewById(R.id.entry_button);
    }

    public void openMain(View view){
        boolean toMainPage = false;
        String username = usernameInput.getText().toString(); //get username & password
        String password = passwordInput.getText().toString();


        //DatabaseReference db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        this.db.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //error, we might want to prompt a toast
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    boolean toMainPage = false;
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Map<String, User> map = (Map<String, User>) task.getResult().getValue();
                    //Log.d("Login", map.toString());
                    //Log.d("Login", Integer.toString(map.size()));

                    User user = task.getResult().getValue(User.class);
                    //Log.d("Login", user.toString());
                    if (user == null){
                        // the username doesn't exist in db
                        // need to register
                        register(username, password);
                        toMainPage = true;
                    }
                    else
                    { //the username exists in db
                        //need to login
                        boolean verify = user.getPassword().equals(password);
                        //check if the entered password == the password in db
                        toMainPage = verify;
                        Log.d("Login", user.getPassword());
                        Log.d("Login", password);
                        Log.d("Login", Boolean.toString(verify));
                        Log.d("Login", Boolean.toString(toMainPage));
                    }

                    if (toMainPage){
                        Intent intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);  //go to main page (activity)
                    }

                }
            }
        });

//        ValueEventListener userListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //User user = dataSnapshot.getValue(User.class);
//                Map<String, User> map = (Map<String, User>) dataSnapshot.getValue();
//                //Log.w("firebase", user.toString());
//                Log.w("firebase", map.toString());
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        db.child("users").child(username).addValueEventListener(userListener);


        //db.child("users").child("user3").setValue(new User("name", "pswd", "id1"));


    }

    private void register(String username, String password){
        this.db.child("users").child(username).setValue(new User(username, password, "1"));
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected  void onPause() {

        super.onPause();
    }

    @Override
    protected  void onStop() {
        super.onStop();
    }

    @Override
    protected  void onRestart() {
        super.onRestart();
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
    }



}
