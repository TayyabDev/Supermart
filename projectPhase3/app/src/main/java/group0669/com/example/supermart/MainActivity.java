package group0669.com.example.supermart;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.b07.store.DatabaseDriverExtender;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonLogin, buttonInitialize;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        buttonInitialize = findViewById(R.id.buttonInitialize);
        buttonInitialize.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPager);
        ImageSwipeAdapter imageSwipeAdapter = new ImageSwipeAdapter(this);
        viewPager.setAdapter(imageSwipeAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonInitialize:
                startActivity(new Intent(this, InitializationActivity.class));
                break;

            case R.id.buttonLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }



}

