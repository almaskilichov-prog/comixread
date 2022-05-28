package kz.almas.comixread.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kz.almas.comixread.R;
import kz.almas.comixread.classes.Auth;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseDatabase DB;
    private DatabaseReference users;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // присваивание бд (Firebase) к переменной
        DB = FirebaseDatabase.getInstance();
        users = DB.getReference().child("users");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // проверка на существование аккаунта
                auth = new Auth(getApplicationContext());
                if (auth.getUsername() == null) {
                    Intent intent = new Intent(getApplicationContext(),
                            AuthorizationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }, 500);


    }
}