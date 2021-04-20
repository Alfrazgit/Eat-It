package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber, password;
    TextView noAccount, noAccountAlert;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    Button logInBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        noAccount = (TextView) findViewById(R.id.no_account);
        noAccountAlert = (TextView) findViewById(R.id.no_account_alert);
        logInBtn = (Button) findViewById(R.id.btn_log_in);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChildren()) {
                            for (final DataSnapshot userSnapShot : snapshot.getChildren()) {

                                User user = userSnapShot.getValue(User.class);
                                String s_phNo = user.getPhNo();
                                String s_password = user.getPassword();

                                if (phoneNumber.getText().toString().equals(s_phNo) && password.getText().toString().equals(s_password)) {

                                    Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                    CurrentUser.currentUser = user;
                                    CurrentUser.userId = userSnapShot.getKey();
                                    startActivity(toMainActivity);
                                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }

                });

            }

        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(toSignUpActivity);
            }
        });

    }
}