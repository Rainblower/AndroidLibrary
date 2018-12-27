package com.example.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.models.DefaultResponse;
import com.example.myapplication.R;
import com.example.myapplication.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private String[] group = {"kek", "pep", "pup"};

    private EditText editTextLogin, editTextPassword, editTextRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, group);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        editTextLogin = findViewById(R.id.editText8);
        editTextPassword = findViewById(R.id.editText9);
        editTextRePassword = findViewById(R.id.editText);

        findViewById(R.id.button9).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button9:
                userSingUp();
                break;
        }
    }

    private void userSingUp() {
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editTextRePassword.getText().toString().trim();

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

        if (password.length() < 6) {
            editTextPassword.setError("Пароль должен быть длинее 6 символов");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(rePassword)) {
            editTextRePassword.setError("Пароли должны совпадать");
            editTextRePassword.requestFocus();
            return;
        }

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(login, password);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.code() == 201){

                    DefaultResponse dr = response.body();
                    Toast.makeText(Register.this, dr.getMsg(), Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(response.code() == 422){
                    Toast.makeText(Register.this, "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }
}
