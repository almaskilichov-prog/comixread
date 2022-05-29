package kz.almas.comixread.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import kz.almas.comixread.R;
import kz.almas.comixread.classes.Auth;
import kz.almas.comixread.classes.LoadingDialog;
import kz.almas.comixread.classes.User;

public class AuthorizationActivity extends AppCompatActivity implements Serializable {
    // переменные для видимых элементов до нажатия кнопки "Далее"
    TextView username_textView;
    TextView textView2;
    TextView textView3;
    EditText username_editText;
    Button btnNextInAuthorization;
    String username;

    // переменные для невидимых элементов до нажатия кнопки "Далее"
    TextView password_textView;
    TextView textView_password;
    EditText password_editText;
    Button btnSignUp;
    String password;
    TextView password_textView_signin;
    TextView textView_password_signin;
    TextView textView4;
    Button btnSignIn;

    // Firebase
    FirebaseDatabase db;
    DatabaseReference users;

    // стандартная аватарка
    ImageView defaultAvatar;

    // Progress Bar
    LoadingDialog loadingDialog = new LoadingDialog(AuthorizationActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        // Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference().child("users");



        // видимые элементы до нажатия кнопки "Далее"
        username_editText = findViewById(R.id.username_editText);
        btnNextInAuthorization = findViewById(R.id.btnNextInAuthorization);
        username_textView = findViewById(R.id.username_textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        // невидимые элементы до нажатия кнопки "Далее" p.s - элементы для регистрации
        password_textView = findViewById(R.id.password_textView);
        textView_password = findViewById(R.id.textView_password_signup);
        password_editText = findViewById(R.id.password_editText);
        btnSignUp = findViewById(R.id.btnSignUp);

        // невидимые элементы до нажатия кнопки "Далее" p.s - элементы для авторизации
        password_textView_signin = findViewById(R.id.password_textView_signin);
        textView_password_signin = findViewById(R.id.textView_password_signin);
        textView4 = findViewById(R.id.textView4);
        btnSignIn = findViewById(R.id.btnSignIn);

        // стандартная аватарка
        defaultAvatar = findViewById(R.id.avatar);


    }

    public void btnNextClicked(View view) {
        if (username_editText.getText().toString().isEmpty()) {
            username_editText.setError("Вы не ввели юзернейм!");
        } else {
            // Запускаем прогресс бар
            loadingDialog.startLoadingDialog();

            // помещаем текст из поле ввода username_editText в стринговую переменную username
            username = username_editText.getText().toString();

            // скрываем все элементы которые были видимы до нажатия кнопки "Далее"
            username_editText.setVisibility(View.INVISIBLE);
            btnNextInAuthorization.setVisibility(View.INVISIBLE);
            username_textView.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);

            Query query = users.orderByChild("username").equalTo(username);
            // делаем видимыми элементы которые были скрыты до нажатия кнопки "Далее"
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        password_textView_signin.setVisibility(View.VISIBLE);
                        textView_password_signin.setVisibility(View.VISIBLE);
                        textView3.setVisibility(View.INVISIBLE);
                        textView4.setText(username);
                        textView4.setVisibility(View.VISIBLE);
                        password_editText.setVisibility(View.VISIBLE);
                        btnSignIn.setVisibility(View.VISIBLE);
                        loadingDialog.dismissDialog();
                    } else {
                        password_textView.setVisibility(View.VISIBLE);
                        textView_password.setVisibility(View.VISIBLE);
                        password_editText.setVisibility(View.VISIBLE);
                        btnSignUp.setVisibility(View.VISIBLE);
                        loadingDialog.dismissDialog();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    public void btnSignUpClicked(View view) {
        if (password_editText.getText().toString().isEmpty()) {
            password_editText.setError("Вы не ввели пароль!");
        } else {
            password = password_editText.getText().toString();

            DatabaseReference push = users.push();
            String key = push.getKey();

            // создаем объект класса User
            User user = new User(key, null, username, password, null);

            // пуш юзера в Firebase
            push.setValue(user);

            // сохранение аккаунта и переход в MainActivity
            Auth auth = new Auth(getApplicationContext());
            auth.saveUsername(user.getUsername());
            auth.setCurrentUser(user);
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public void btnSignInClicked(View view) {
        if (password_editText.getText().toString().isEmpty()) {
            password_editText.setError("Вы не ввели пароль!");
        } else {
            password = password_editText.getText().toString();
            loadingDialog.startLoadingDialog();
            Query query = users.orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean flag = false;
                    for (DataSnapshot data : snapshot.getChildren()) {
                        User user = data.getValue(User.class);
                        if (user.getPassword().equals(password)) {
                            Auth auth = new Auth(getApplicationContext());
                            auth.saveUsername(user.getUsername());
                            auth.setCurrentUser(user);
                            Intent intent = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            flag = true;
                        }
                    }
                    if (!flag) {
                        Toast.makeText(getApplicationContext(),
                                "Вы ввели неправильный пароль!",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}