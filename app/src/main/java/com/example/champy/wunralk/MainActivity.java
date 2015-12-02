package com.example.champy.wunralk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Runtime runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView duration = (TextView) findViewById(R.id.time_duration);
        runtime = new Runtime(duration);

        final ImageButton clickHereBtn = (ImageButton)findViewById(R.id.startstopbtn);
        clickHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),activity_loging.class));
//                clickHereBtn.setSelected(!clickHereBtn.isSelected());
//                if (clickHereBtn.isSelected()) {
//                    runtime.start();
//                    clickHereBtn.setBackgroundResource(R.drawable.stop);
//
//                } else {
//                    runtime.pause();
//                    clickHereBtn.setBackgroundResource(R.drawable.start);
//                }
            }
        });
    }
}
