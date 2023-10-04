package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailText,passwordText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createBtnTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText=findViewById(R.id.email_edit_text);
        passwordText=findViewById(R.id.password_edit_text);
        loginBtn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        createBtnTextView=findViewById(R.id.create_account_text_view);

        loginBtn.setOnClickListener((v)->loginUser());
        createBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    private void loginUser() {

        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();

        boolean isDataValidated=validateData(email,password);

        if (!isDataValidated)
        {
            return;
        }

        loginAccountInFirebase(email,password);
    }

    void loginAccountInFirebase(String email,String password)
    {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful())
                {
                    //Login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //Go to mainActivity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }else {
                        Utility.showToast(LoginActivity.this,"Email is not verified, Please check your mail");
                    }

                }else {
                    //Login failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress)
    {
        if (inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password )
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

        return true;
    }
}