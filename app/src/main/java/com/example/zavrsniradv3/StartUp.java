package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        findViewById(R.id.con).setOnClickListener(view -> {
            startActivity(new Intent(StartUp.this,LOGIN.class));
            overridePendingTransition(R.anim.anim1,R.anim.anim2);
        });
    }
}