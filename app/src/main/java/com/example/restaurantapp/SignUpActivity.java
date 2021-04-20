package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText name, phNo, password;
    Button signUp;
    ImageView logo;
    Spinner spinner;
    String[] states_list = {"Choose Your State", "West Bengal", "Uttar Pradesh", "Andhra Pradesh", "Madhya Pradesh", "Orissa"};
    String state;
    FirebaseDatabase database;
    DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.name);
        phNo = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        signUp = (Button) findViewById(R.id.btn_sign_up);

//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(SignUpActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                states_list);
//        spinner.setAdapter(spinnerAdapter);


//        ArrayList<String> states = new ArrayList<String>();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1 = name.getText().toString();
                String s2 = phNo.getText().toString();
                String s3 = password.getText().toString();


//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        state = adapterView.getItemAtPosition(i).toString();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) { }
//
//                });


                database = FirebaseDatabase.getInstance();
                usersRef = database.getReference("users");

                if (s1.compareTo("") == 0) {
                    Toast.makeText(SignUpActivity.this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s2.compareTo("") == 0) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s3.compareTo("") == 0) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = usersRef.push().getKey();
                User user = new User(s1, s2, s3);
                usersRef.child(userId).setValue(user);

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}