package com.example.rapidroute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText s_up_email,s_up_password,s_up_phone;
    Button signup_btn,gotoLogin_btn;
    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_up_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        s_up_email=findViewById(R.id.s_up_email);
        s_up_phone = findViewById(R.id.s_up_phone);
        s_up_password=findViewById(R.id.s_up_password);
        signup_btn=findViewById(R.id.signup_btn);
        Spinner spinner_s_up=findViewById(R.id.dropdown_roles_s_up);
        gotoLogin_btn=findViewById(R.id.goto_login_btn);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.58.71:3001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        gotoLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = s_up_email.getText().toString();
                String phone = s_up_phone.getText().toString();
                String password = s_up_password.getText().toString();
                String role = spinner_s_up.getSelectedItem().toString();

                User user = new User(email,phone,password,role);
                signup(user);
            }
        });
        spinner_s_up.setOnItemSelectedListener(this);
    }

    private void signup(User user){
        apiService.signUp(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response){
                if (response.isSuccessful()) {
                    Toast.makeText(SignupPage.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = "An unexpected error occurred";
                    if (response.code() == 404) {
                        errorMessage = "User already exists.";
                    } else if (response.code() == 500) {
                        errorMessage = "Server error.";
                    }

                    Toast.makeText(SignupPage.this, "SignUp failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignupPage.this, "SignUp failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
