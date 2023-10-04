package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailText,passwordText,confirmPasswordText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        emailText=findViewById(R.id.email_edit_text);
        passwordText=findViewById(R.id.password_edit_text);
        confirmPasswordText=findViewById(R.id.confirm_password_edit_text);
        createAccountBtn=findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtnTextView=findViewById(R.id.login_text_view);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());

    }
    void createAccount(){
        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();
        String confirmPassword=confirmPasswordText.getText().toString();

        boolean isDataValidated=validateData(email,password,confirmPassword);
    }

    boolean validateData(String email,String password,String confirmPassword)
    {
        //The values entered by user while creating the account will be verified.

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordText.setError("Minimum length of password is 6");
            return false;
        }
        if (!password.equals(confirmPassword)){
            confirmPasswordText.setError("Passwords are not matching");
            return false;
        }
        return true;
    }
}