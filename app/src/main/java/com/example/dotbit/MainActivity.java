package com.example.dotbit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.main);
        bottomNavigationView.setOnItemSelectedListener( menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.main) {
                return true;
            } else if (itemId == R.id.content) {
                startActivity(new Intent(getApplicationContext(), content_activity.class));
                finish();

            } else if (itemId == R.id.note) {
                startActivity(new Intent(getApplicationContext(), notes_activity.class));
                finish();
            } else if (itemId == R.id.chatbot) {
                startActivity(new Intent(getApplicationContext(), chat_acitivity.class));
                finish();
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), profile_activity.class));
                finish();
            }
            else {
                // Handle other menu items if needed
                return false;
            }
            return true;

        });
    }
}