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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText phone,password;
    Button login_btn,gotoSignup_btn;
    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        Spinner spinner=findViewById(R.id.dropdown_roles);
        gotoSignup_btn=findViewById(R.id.goto_signup_btn);

        Button dummy = findViewById(R.id.dummy);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.58.71:3001/") // Replace with your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        gotoSignup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupPage.class);
                startActivity(intent);
            }
        });


        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DeliveryAgentActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String phoneno=phone.getText().toString().trim();
                String pass=password.getText().toString();
                String role=spinner.getSelectedItem().toString();

                User user = new User(phoneno,pass);
                login(user);
                }
        });
        spinner.setOnItemSelectedListener(this);
    }

    private void login(User user) {
        apiService.login(user).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse.UserData loggedInUser = response.body().getUser();
                    if (loggedInUser != null) {
                        String role = loggedInUser.getRole();

                        // Redirect based on role
                        switch (role) {
                            case "admin":
                                Toast.makeText(MainActivity.this, "Admin can only be accessed through web app", Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(MainActivity.this, AdminDashboardActivity.class));
                                break;
                            case "delivery partner":
//                                Toast.makeText(MainActivity.this, "Delivery partner", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, DeliveryAgentActivity.class));
                                break;
                            case "customer":
//                                Toast.makeText(MainActivity.this, "Customer", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, CustomerActivity.class));
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Unknown role", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "User data is missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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