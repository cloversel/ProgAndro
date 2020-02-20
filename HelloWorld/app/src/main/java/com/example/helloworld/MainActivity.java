package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        button = findViewById((R.id.button));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("admin@ti.ukdw.ac.id") && password.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                    GoToHomePage();
                }else{
                    Toast.makeText(getApplicationContext(), "Email or Password is wrong!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void GoToHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }


}
