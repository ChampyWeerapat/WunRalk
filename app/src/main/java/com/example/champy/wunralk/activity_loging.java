package com.example.champy.wunralk;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLException;

public class activity_loging extends AppCompatActivity {
    private ManageDB db;
    private TextView tvUser;
    private TextView tvPass;
    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_loging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SQLiteDatabase mydatabase = openOrCreateDatabase("wunralk",MODE_PRIVATE,null);
        try {
            db = ManageDB.getInstance(mydatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.createTable("history", "username varchar(16),dateTime datetime,place varchar(50),distance double,time time,calories double");
        db.createTable("member", "Username VARCHAR,Password VARCHAR");
        db.addMember("wunralker", "admin");
        db.addMember("admin", "admin");

        tvUser = (EditText) findViewById(R.id.username);
        tvPass = (EditText) findViewById(R.id.password);



        Button btLogin = (Button) findViewById(R.id.button_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.login(tvUser.getText().toString(),tvPass.getText().toString())){
                    username = tvUser.getText().toString();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "username or password wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Button fab = (Button) findViewById(R.id.button_login);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
////                startActivity(new Intent(getApplicationContext(),MainActivity.class));
////                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
//            }
//        });
    }

    public static String getUsername(){
        return username;
    }

}
