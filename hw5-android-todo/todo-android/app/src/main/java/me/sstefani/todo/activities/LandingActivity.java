package me.sstefani.todo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.sstefani.todo.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);

        final Button button = findViewById(R.id.login_redirect_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginActivity= new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });
    }
}
