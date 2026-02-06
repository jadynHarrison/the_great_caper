package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_select_screen);

        TextView[] options = {
                findViewById(R.id.p2),
                findViewById(R.id.p3),
                findViewById(R.id.p4),
                findViewById(R.id.p5),
                findViewById(R.id.p6),
                findViewById(R.id.p7)
        };

        for (TextView tv : options) {
            tv.setOnClickListener(v -> {
                int numPlayers = Integer.parseInt(tv.getText().toString().trim());

                Intent intent = new Intent(PlayerSelectActivity.this, MainActivity.class);
                intent.putExtra("numPlayers", numPlayers);
                startActivity(intent);
            });
        }
    }
}