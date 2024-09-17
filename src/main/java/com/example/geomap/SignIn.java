package com.example.geomap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private EditText txtFullName, txtEmail, txtPhone, txtUsername, txtPassword, txtConfirmPass;


    FirebaseDatabase rootNode;

    SignInClass signInClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);

        Button btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(v -> SignIn());


    }


    public void SignIn() {


        String strUser = txtUsername.getText().toString().trim();


        if (validName() & validEmail() & validPhone() & validUsername() & validPass()) {

            rootNode = FirebaseDatabase.getInstance();

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference UsernameRef = rootRef.child("Users").child(strUser);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {


                    if (!snapshot.exists()) {

                        txtUsername.setError(null);

                        String strUsername = txtUsername.getText().toString().trim();
                        String strPassword = txtPassword.getText().toString().trim();
                        String strName = txtFullName.getText().toString().trim();
                        String strEmail = txtEmail.getText().toString().trim();
                        String strPhone = txtPhone.getText().toString().trim();
                        String strConfirm = txtConfirmPass.getText().toString().trim();

                        if (strPassword.equals(strConfirm)) {

                            DatabaseReference writetoDb = rootNode.getReference("Users");

                            signInClass = new SignInClass(strName, strEmail, strPhone, strUsername, strPassword);

                            writetoDb.child(strUsername).setValue(signInClass);


                            Toast.makeText(getApplicationContext(), "You have been register", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignIn.this, LogIn.class);

                            startActivity(intent);

                        }else{

                            txtConfirmPass.setError("Password does not match");
                            txtConfirmPass.requestFocus();

                        }

                    } else {
                        txtUsername.setError("User Already Exist ");
                        txtUsername.requestFocus();
                    }


                }

                public void onCancelled(DatabaseError error) {

                }
            };
            UsernameRef.addListenerForSingleValueEvent(eventListener);

        } else return;

    }


    public boolean validName() {

        String name = txtFullName.getText().toString().trim();

        if (name.isEmpty()) {
            txtFullName.setError("Field cannot be empty");
            return false;

        } else {
            txtFullName.setError(null);
            return true;
        }
    }


    private boolean validEmail() {

        String email = txtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            txtEmail.setError("Field cannot be empty");
            return false;

        } else {
            txtEmail.setError(null);
            return true;
        }

    }


    private boolean validPhone() {

        String phone = txtPhone.getText().toString().trim();

        if (phone.isEmpty()) {
            txtPhone.setError("Field cannot be empty");
            return false;

        } else if (phone.length() != 10) {
            txtPhone.setError("Enter 10 digits");
            return false;

        } else {
            txtPhone.setError(null);
            return true;
        }


    }


    private boolean validUsername() {

        String username = txtUsername.getText().toString().trim();

        if (username.isEmpty()) {
            txtUsername.setError("Field cannot be empty");
            return false;

        } else {
            txtUsername.setError(null);
            return true;
        }


    }


    private boolean validPass() {

        String password = txtPassword.getText().toString().trim();

        if (password.isEmpty()) {
            txtPassword.setError("Field cannot be empty");
            return false;

        } else {
            txtPassword.setError(null);
            return true;
        }


    }


}