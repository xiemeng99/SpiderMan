package com.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simple.spiderman.BuildConfig;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_buildType = findViewById(R.id.tv_buildType);
        tv_buildType.setText(BuildConfig.BUILD_TYPE);
        findViewById(R.id.btn_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String text = null;
//                text.toUpperCase();
                Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
