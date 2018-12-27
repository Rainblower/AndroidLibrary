package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextPassword, editTextLogin;
    Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPassword = findViewById(R.id.editText9);
        editTextLogin = findViewById(R.id.editText8);
        mySwitch = findViewById(R.id.switch1);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

            Intent intent = new Intent(MainActivity.this, Personal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void userLogin(View view) {
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (login.isEmpty()) {
            editTextLogin.setError("Введите логин");
            editTextLogin.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Введите пароль");
            editTextPassword.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(login, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if (!loginResponse.isError()) {

                    SharedPrefManager.getInstance(MainActivity.this)
                            .saveUser(loginResponse.getUser());

                    Toast.makeText(MainActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, Personal.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button8:
                userLogin(v);
                break;
            case R.id.button9:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}
