package com.example.dotbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class profile_activity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button logout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener( menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.main) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else if (itemId == R.id.content) {
                startActivity(new Intent(getApplicationContext(), content_activity.class));
                finish();

            } else if (itemId == R.id.note) {
                startActivity(new Intent(getApplicationContext(), notes_activity.class));
                finish();
            } else if (itemId == R.id.chatbot) {
                startActivity(new Intent(getApplicationContext(), profile_activity.class));
                finish();
            } else if (itemId == R.id.profile) {
               return true;
            }
            else {
                // Handle other menu items if needed
                return false;
            }
            return true;

        });

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login_activity.class));
                finish();
            }
        });
    }
}