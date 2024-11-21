package com.example.rapidroute;

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

public class SignupPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText s_up_username,s_up_password,s_up_conf_password;
    Button signup;

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

        s_up_username=findViewById(R.id.s_up_username);
        s_up_password=findViewById(R.id.s_up_password);
        s_up_conf_password=findViewById(R.id.s_up_conf_password);
        signup=findViewById(R.id.signup_btn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s_up_username.getText().toString().isEmpty())
                    s_up_username.setError("Please enter username");
                else if(s_up_password.getText().toString().isEmpty())
                    s_up_password.setError("please enter password");
                else if(s_up_conf_password.getText().toString().isEmpty())
                    s_up_conf_password.setError("please confirm password");
                else
                    Toast.makeText(SignupPage.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
            }
        });

        Spinner spinner_s_up=findViewById(R.id.dropdown_roles_s_up);
        spinner_s_up.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
