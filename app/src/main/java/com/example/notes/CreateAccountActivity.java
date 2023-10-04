package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        if (!isDataValidated)
        {
            return;
        }

        createAccountInFirebase(email,password);
    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    //Account is successfully created
                    Toast.makeText(CreateAccountActivity.this, "Account created successfully,Check email to verify", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else {
                    //In case of failure
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void changeInProgress(boolean inProgress)
    {
        if (inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
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