package com.senierr.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView = findViewById(R.id.tv_text);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            textView.setText(bundle.getString("key"));
        }
    }
}
