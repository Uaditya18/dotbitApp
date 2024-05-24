package com.example.dotbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register_activity extends AppCompatActivity {
    TextView login_link;

    public static final String TAG = "TAG";
    EditText edittext_name,edittext_email,edittext_phone,edittext_password,edittext_rePassword;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    Button register;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login_link = findViewById(R.id.login_link);
        register = findViewById(R.id.register);
        edittext_name = findViewById(R.id.edittext_name);
        edittext_email = findViewById(R.id.edittext_email);
        edittext_phone = findViewById(R.id.edittext_phone);
        edittext_password = findViewById(R.id.edittext_password);
        edittext_rePassword = findViewById(R.id.edittext_rePassword);
        progressBar = findViewById(R.id.progress_bar);

        fAuth = FirebaseAuth.getInstance();



        login_link.setPaintFlags(login_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_activity.this,login_activity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edittext_email.getText().toString();
                String name = edittext_name.getText().toString();
                String phone = edittext_phone.getText().toString();
                String password = edittext_password.getText().toString();
                String con_pass = edittext_rePassword.getText().toString();


                if(TextUtils.isEmpty(name)){
                    edittext_name.setError("name is required");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    edittext_email.setError("email is required");
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    edittext_phone.setError("phone is required");
                    return;
                }

                if(TextUtils.isEmpty(password) ){
                    edittext_password.setError("password is required");
                    return;
                }

                if(password.length()<6){
                    edittext_password.setError("password must be of greater then 6 characters");
                    return;
                }

                if(con_pass.isEmpty() || !password.equals(con_pass)){
                    edittext_rePassword.setError("Invalid password ");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(register_activity.this, "User Created go for login", Toast.LENGTH_SHORT).show();
                            startActivitySecond();
                            // send verification code
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(register_activity.this, "Email verification has been sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: Email not sent"+e.getMessage());
                                }
                            });



                        }
                        else {
                            Toast.makeText(register_activity.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


    }

    public void startActivitySecond(){
        Intent intent =new Intent(register_activity.this,login_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}